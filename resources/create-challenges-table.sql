USE QMT;

-- Create Challenge_Requests table
CREATE TABLE IF NOT EXISTS Challenge_Requests (
    RequestID INT UNSIGNED NOT NULL AUTO_INCREMENT,
    SenderID SMALLINT UNSIGNED NOT NULL,
    ReceiverID SMALLINT UNSIGNED NOT NULL,
    SurahID SMALLINT UNSIGNED NOT NULL,
    Status ENUM('Pending', 'Accepted', 'Rejected') DEFAULT 'Pending',
    Sent_At TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(RequestID),
    CONSTRAINT FK_ChallengeRequestSender FOREIGN KEY(SenderID) 
        REFERENCES Users(UserID) ON DELETE CASCADE,
    CONSTRAINT FK_ChallengeRequestReceiver FOREIGN KEY(ReceiverID) 
        REFERENCES Users(UserID) ON DELETE CASCADE,
    CONSTRAINT FK_ChallengeRequestSurah FOREIGN KEY(SurahID) 
        REFERENCES Surahs(SurahID),
    -- Prevent duplicate challenge requests for the same Surah between users
    CONSTRAINT UQ_ChallengeRequest UNIQUE (SenderID, ReceiverID, SurahID, Status)
);

-- Create Active_Challenges table
CREATE TABLE IF NOT EXISTS Active_Challenges (
    ChallengeID INT UNSIGNED NOT NULL AUTO_INCREMENT,
    User1ID SMALLINT UNSIGNED NOT NULL,
    User2ID SMALLINT UNSIGNED NOT NULL,
    SurahID SMALLINT UNSIGNED NOT NULL,
    Started_At TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(ChallengeID),
    CONSTRAINT FK_ActiveChallengeUser1 FOREIGN KEY(User1ID) 
        REFERENCES Users(UserID) ON DELETE CASCADE,
    CONSTRAINT FK_ActiveChallengeUser2 FOREIGN KEY(User2ID) 
        REFERENCES Users(UserID) ON DELETE CASCADE,
    CONSTRAINT FK_ActiveChallengeSurah FOREIGN KEY(SurahID) 
        REFERENCES Surahs(SurahID),
    -- Prevent users from having multiple active challenges for the same Surah
    CONSTRAINT UQ_ActiveChallenge UNIQUE (User1ID, User2ID, SurahID)
);

-- Create Completed_Challenges table
CREATE TABLE IF NOT EXISTS Completed_Challenges (
    CompletedChallengeID INT UNSIGNED NOT NULL AUTO_INCREMENT,
    ChallengerID SMALLINT UNSIGNED NOT NULL,
    ChallengedID SMALLINT UNSIGNED NOT NULL,
    SurahID SMALLINT UNSIGNED NOT NULL,
    WinnerID SMALLINT UNSIGNED NOT NULL,
    Started_At TIMESTAMP NOT NULL,
    Completed_At TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(CompletedChallengeID),
    CONSTRAINT FK_CompletedChallengeChallenger FOREIGN KEY(ChallengerID) 
        REFERENCES Users(UserID) ON DELETE CASCADE,
    CONSTRAINT FK_CompletedChallengeChallenged FOREIGN KEY(ChallengedID) 
        REFERENCES Users(UserID) ON DELETE CASCADE,
    CONSTRAINT FK_CompletedChallengeWinner FOREIGN KEY(WinnerID) 
        REFERENCES Users(UserID) ON DELETE CASCADE,
    CONSTRAINT FK_CompletedChallengeSurah FOREIGN KEY(SurahID) 
        REFERENCES Surahs(SurahID)
);

-- Create trigger to check if users have already memorized the Surah before allowing challenge request
DELIMITER $$
CREATE TRIGGER before_challenge_request_insert
BEFORE INSERT ON Challenge_Requests
FOR EACH ROW
BEGIN
    DECLARE sender_memorized, receiver_memorized BOOLEAN;
    
    -- Check if sender has already memorized the Surah
    SELECT Is_Memorized INTO sender_memorized
    FROM User_Surah_Progress
    WHERE UserID = NEW.SenderID AND SurahID = NEW.SurahID;
    
    -- Check if receiver has already memorized the Surah
    SELECT Is_Memorized INTO receiver_memorized
    FROM User_Surah_Progress
    WHERE UserID = NEW.ReceiverID AND SurahID = NEW.SurahID;
    
    -- If either user has already memorized the Surah, prevent the challenge request
    IF sender_memorized = TRUE OR receiver_memorized = TRUE THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cannot create challenge: One or both users have already memorized this Surah';
    END IF;
END$$
DELIMITER ;

-- Create trigger to move challenge to completed_challenges when a user completes the Surah
DELIMITER $$
CREATE TRIGGER after_surah_progress_update
AFTER UPDATE ON User_Surah_Progress
FOR EACH ROW
BEGIN
    IF NEW.Is_Memorized = TRUE THEN
        -- Check if this user is part of an active challenge for this Surah
        INSERT INTO Completed_Challenges (
            ChallengerID, 
            ChallengedID, 
            SurahID, 
            WinnerID, 
            Started_At, 
            Completed_At
        )
        SELECT 
            User1ID,
            User2ID,
            SurahID,
            NEW.UserID,
            Started_At,
            CURRENT_TIMESTAMP
        FROM Active_Challenges
        WHERE SurahID = NEW.SurahID 
        AND (User1ID = NEW.UserID OR User2ID = NEW.UserID);
        
        -- Remove the challenge from active challenges
        DELETE FROM Active_Challenges 
        WHERE SurahID = NEW.SurahID 
        AND (User1ID = NEW.UserID OR User2ID = NEW.UserID);
    END IF;
END$$
DELIMITER ;
