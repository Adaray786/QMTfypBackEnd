USE QMT;

DELIMITER $$

CREATE TRIGGER after_user_insert_scores
AFTER INSERT ON Users
FOR EACH ROW
BEGIN
    INSERT INTO User_Scores (UserID, Total_Score)
    VALUES (NEW.UserID, 0);
END$$

DELIMITER ; 