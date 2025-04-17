USE QMT;

INSERT INTO User_Scores (UserID, Total_Score)
SELECT u.UserID, 0
FROM Users u
LEFT JOIN User_Scores us ON u.UserID = us.UserID
WHERE us.UserID IS NULL; 