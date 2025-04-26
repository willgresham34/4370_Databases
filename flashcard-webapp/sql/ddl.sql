-- drop and create database
DROP DATABASE IF EXISTS flashcards_db;

CREATE DATABASE IF NOT EXISTS flashcards_db;

USE flashcards_db;

-- users table
CREATE TABLE
    IF NOT EXISTS Users (
        userId INT AUTO_INCREMENT,
        username VARCHAR(255) NOT NULL,
        password VARCHAR(255) NOT NULL,
        firstName VARCHAR(255) NOT NULL,
        lastName VARCHAR(255) NOT NULL,
        PRIMARY KEY (userId),
        UNIQUE KEY uq_users_username (username)
    );

-- folders table
CREATE TABLE
    IF NOT EXISTS Folders (
        folderId INT AUTO_INCREMENT,
        userId INT NOT NULL,
        folderName VARCHAR(255),
        PRIMARY KEY (folderId),
        INDEX idx_folders_userId (userId),
        FOREIGN KEY (userId) REFERENCES Users (userId) ON DELETE CASCADE
    );

-- sets table
CREATE TABLE
    IF NOT EXISTS Sets (
        setId INT AUTO_INCREMENT,
        userId INT NOT NULL,
        setName VARCHAR(255) NOT NULL,
        setDescription VARCHAR(500) NOT NULL,
        setCategory VARCHAR(255) NOT NULL,
        PRIMARY KEY (setId),
        INDEX idx_sets_userId (userId),
        FOREIGN KEY (userId) REFERENCES Users (userId) ON DELETE CASCADE
    );

-- sets_folder table
CREATE TABLE
    IF NOT EXISTS Set_Folders (
        setId INT NOT NULL,
        folderId INT NOT NULL,
        PRIMARY KEY (setId, folderId),
        INDEX idx_set_folders_setId (setId),
        INDEX idx_set_folders_folderId (folderId),
        FOREIGN KEY (setId) REFERENCES Sets (setId) ON DELETE CASCADE,
        FOREIGN KEY (folderId) REFERENCES Folders (folderId) ON DELETE CASCADE
    );

-- 5) flashcards table
CREATE TABLE
    IF NOT EXISTS Flashcards (
        cardId INT AUTO_INCREMENT,
        setId INT NOT NULL,
        cardTerm VARCHAR(500) NOT NULL,
        cardDesc VARCHAR(500) NOT NULL,
        PRIMARY KEY (cardId),
        INDEX idx_flashcards_setId (setId),
        FOREIGN KEY (setId) REFERENCES Sets (setId) ON DELETE CASCADE
    );