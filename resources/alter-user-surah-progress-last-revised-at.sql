Use QMT;
-- Add last_revised_at column to User_Surah_Progress table
ALTER TABLE User_Surah_Progress
ADD COLUMN Last_Revised_At TIMESTAMP DEFAULT NULL;