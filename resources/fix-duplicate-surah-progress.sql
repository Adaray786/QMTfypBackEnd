USE QMT;

-- First, create a temporary table to store the latest progress for each user and surah
CREATE TEMPORARY TABLE Temp_User_Surah_Progress AS
SELECT 
    UserID,
    SurahID,
    MAX(ProgressID) as LatestProgressID,
    MAX(Is_Memorized) as Is_Memorized,
    MAX(Last_Revised_At) as Last_Revised_At
FROM User_Surah_Progress
GROUP BY UserID, SurahID;

-- Delete all existing records
DELETE FROM User_Surah_Progress;

-- Insert the deduplicated records back
INSERT INTO User_Surah_Progress (ProgressID, UserID, SurahID, Is_Memorized, Last_Revised_At)
SELECT 
    LatestProgressID,
    UserID,
    SurahID,
    Is_Memorized,
    Last_Revised_At
FROM Temp_User_Surah_Progress;

-- Drop the temporary table
DROP TEMPORARY TABLE IF EXISTS Temp_User_Surah_Progress;

-- Add a unique constraint to prevent future duplicates
ALTER TABLE User_Surah_Progress
ADD CONSTRAINT UQ_UserSurahProgress UNIQUE (UserID, SurahID); 