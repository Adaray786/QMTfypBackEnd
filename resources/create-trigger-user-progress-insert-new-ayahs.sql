USE QMT;

DELIMITER $$

CREATE TRIGGER after_ayah_insert
AFTER INSERT ON Ayahs
FOR EACH ROW
BEGIN
    INSERT INTO User_Ayah_Progress (UserID, AyahID, Is_Memorized)
    SELECT UserID, NEW.AyahID, FALSE FROM Users;
END$$

DELIMITER ;
