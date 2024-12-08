USE QMT;

DELIMITER $$

CREATE TRIGGER after_user_insert
AFTER INSERT ON Users
FOR EACH ROW
BEGIN
    INSERT INTO User_Surah_Progress (UserID, SurahID, Is_Memorized)
    SELECT NEW.UserID, SurahID, FALSE FROM Surahs;

    INSERT INTO User_Ayah_Progress (UserID, AyahID, Is_Memorized)
    SELECT NEW.UserID, AyahID, FALSE FROM Ayahs;
END$$

DELIMITER ;