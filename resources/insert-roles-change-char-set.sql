USE QMT;

-- Insert data into the Role table (if already done then make sure these are commented out)
INSERT INTO Auth_Roles(RoleID, Role_Name) VALUES (1, 'Admin');
INSERT INTO Auth_Roles(RoleID, Role_Name) VALUES (2, 'User');

-- Set the database character set to utf8mb4 (if already done then make sure these are commented out)
ALTER DATABASE QMT CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;