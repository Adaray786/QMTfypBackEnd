USE QMT;

INSERT INTO User_Ayah_Progress (UserID, AyahID, Is_Memorized)
SELECT u.UserID, a.AyahID, FALSE
FROM Users u
CROSS JOIN Ayahs a
LEFT JOIN User_Ayah_Progress uap ON u.UserID = uap.UserID AND a.AyahID = uap.AyahID
WHERE uap.AyahID IS NULL;
