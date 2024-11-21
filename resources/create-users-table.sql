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

-- Insert data into the Role table
INSERT INTO Auth_Roles(RoleID, Role_Name) VALUES (1, 'Admin');
INSERT INTO Auth_Roles(RoleID, Role_Name) VALUES (2, 'User');
