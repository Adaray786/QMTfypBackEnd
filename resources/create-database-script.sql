-- Create the QMT database
CREATE DATABASE IF NOT EXISTS QMT;

-- Use the QMT database
USE QMT;

-- Create the Role table
CREATE TABLE IF NOT EXISTS `Auth_Roles` (
                                            RoleID SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
                                            Role_Name VARCHAR(70) NOT NULL,
    PRIMARY KEY(RoleID)
    );

-- Create the Users table
CREATE TABLE IF NOT EXISTS Users(
                                    UserID SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
                                    Email VARCHAR(100) NOT NULL,
    Password VARCHAR(255) NOT NULL,
    RoleID SMALLINT UNSIGNED NOT NULL,
    PRIMARY KEY(UserID),
    CONSTRAINT UQ_User UNIQUE(Email),
    CONSTRAINT FK_RoleUser FOREIGN KEY(RoleID)
    REFERENCES Auth_Roles(RoleID)
    );

-- Create the Surahs table
CREATE TABLE IF NOT EXISTS Surahs (
                                      SurahID SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
                                      Surah_Name_Arabic VARCHAR(100) NOT NULL,
    Surah_Name_English VARCHAR(100) NOT NULL,
    Total_Ayahs SMALLINT UNSIGNED NOT NULL,
    PRIMARY KEY(SurahID)
    );

-- Create the Ayahs table
CREATE TABLE IF NOT EXISTS Ayahs (
                                     AyahID INT UNSIGNED NOT NULL AUTO_INCREMENT,
                                     SurahID SMALLINT UNSIGNED NOT NULL,
                                     Ayah_Number SMALLINT UNSIGNED NOT NULL,
                                     Arabic_Text TEXT NOT NULL,
                                     Word_Count SMALLINT UNSIGNED NOT NULL,
                                     PRIMARY KEY(AyahID),
    CONSTRAINT FK_SurahAyah FOREIGN KEY(SurahID)
    REFERENCES Surahs(SurahID)
    );

-- User_Ayah_Progress table tracks per-ayah progress
CREATE TABLE IF NOT EXISTS User_Ayah_Progress (
                                                  ProgressID INT UNSIGNED NOT NULL AUTO_INCREMENT,
                                                  UserID SMALLINT UNSIGNED NOT NULL,
                                                  AyahID INT UNSIGNED NOT NULL,
                                                  Is_Memorized BOOLEAN DEFAULT FALSE,
                                                  PRIMARY KEY(ProgressID),
    CONSTRAINT FK_UserAyahProgressUser FOREIGN KEY(UserID)
    REFERENCES Users(UserID),
    CONSTRAINT FK_UserAyahProgressAyah FOREIGN KEY(AyahID)
    REFERENCES Ayahs(AyahID)
    );

-- User_Surah_Progress table tracks per-surah progress
CREATE TABLE IF NOT EXISTS User_Surah_Progress (
                                                   ProgressID INT UNSIGNED NOT NULL AUTO_INCREMENT,
                                                   UserID SMALLINT UNSIGNED NOT NULL,
                                                   SurahID SMALLINT UNSIGNED NOT NULL,
                                                   Is_Memorized BOOLEAN DEFAULT FALSE,
                                                   PRIMARY KEY(ProgressID),
    CONSTRAINT FK_UserSurahProgressUser FOREIGN KEY(UserID)
    REFERENCES Users(UserID),
    CONSTRAINT FK_UserSurahProgressSurah FOREIGN KEY(SurahID)
    REFERENCES Surahs(SurahID)
    );

-- Create the User_Scores table
CREATE TABLE IF NOT EXISTS User_Scores (
    UserID SMALLINT UNSIGNED NOT NULL,
    Total_Score INT UNSIGNED DEFAULT 0,
    PRIMARY KEY(UserID),
    CONSTRAINT FK_UserScoreUser FOREIGN KEY(UserID) REFERENCES Users(UserID) ON DELETE CASCADE
);

    -- Create the Friends table
    CREATE TABLE IF NOT EXISTS Friends (
											FriendshipID INT UNSIGNED NOT NULL AUTO_INCREMENT,
											User1ID SMALLINT UNSIGNED NOT NULL,
											User2ID SMALLINT UNSIGNED NOT NULL,
											Created_At TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
											PRIMARY KEY(FriendshipID),
    CONSTRAINT FK_FriendUser1 FOREIGN KEY(User1ID) REFERENCES Users(UserID) ON DELETE CASCADE,
    CONSTRAINT FK_FriendUser2 FOREIGN KEY(User2ID) REFERENCES Users(UserID) ON DELETE CASCADE,
    CONSTRAINT UQ_Friendship UNIQUE (User1ID, User2ID)
	);

	-- Create the Friend_Requests table
	CREATE TABLE IF NOT EXISTS Friend_Requests (
											RequestID INT UNSIGNED NOT NULL AUTO_INCREMENT,
											SenderID SMALLINT UNSIGNED NOT NULL,
											ReceiverID SMALLINT UNSIGNED NOT NULL,
											Status ENUM('Pending', 'Accepted', 'Rejected') DEFAULT 'Pending',
											Sent_At TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY(RequestID),
    CONSTRAINT FK_RequestSender FOREIGN KEY(SenderID) REFERENCES Users(UserID) ON DELETE CASCADE,
    CONSTRAINT FK_RequestReceiver FOREIGN KEY(ReceiverID) REFERENCES Users(UserID) ON DELETE CASCADE,
    CONSTRAINT UQ_FriendRequest UNIQUE (SenderID, ReceiverID)
);



