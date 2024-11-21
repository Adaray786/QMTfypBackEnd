-- Create the QMT database
CREATE DATABASE QMT;

-- Use the QMT database
USE QMT;

-- Create the Role table
CREATE TABLE `Role` (
                        roleID SMALLINT NOT NULL,
                        name VARCHAR(64) NOT NULL,
                        PRIMARY KEY (roleID)
);

-- Insert data into the Role table
INSERT INTO Role(roleID, name) VALUES (1, 'Admin');
INSERT INTO Role(roleID, name) VALUES (2, 'Employee');