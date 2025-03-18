-- Create the database.
create database if not exists cs4370_mb_platform;

-- Use the created database.
use cs4370_mb_platform;

-- Create the user table.
create table if not exists user (
    userId int auto_increment,
    username varchar(255) not null,
    password varchar(255) not null,
    firstName varchar(255) not null,
    lastName varchar(255) not null,
    primary key (userId),
    unique (username),
    constraint userName_min_length check (char_length(trim(userName)) >= 2),
    constraint firstName_min_length check (char_length(trim(firstName)) >= 2),
    constraint lastName_min_length check (char_length(trim(lastName)) >= 2)
);

CREATE TABLE post (
    postId INT PRIMARY KEY AUTO_INCREMENT,
    userId INT NOT NULL,
    postDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    postText TEXT NOT NULL,
    FOREIGN KEY (userId) REFERENCES user(userId) ON DELETE CASCADE
);

CREATE TABLE comment (
    commentId INT PRIMARY KEY AUTO_INCREMENT,
    postId INT NOT NULL,
    userId INT NOT NULL,
    commentDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    commentText TEXT NOT NULL,
    FOREIGN KEY (postId) REFERENCES post(postId) ON DELETE CASCADE,
    FOREIGN KEY (userId) REFERENCES user(userId) ON DELETE CASCADE
);

CREATE TABLE heart (
    postId INT NOT NULL,
    userId INT NOT NULL,
    PRIMARY KEY (postId, userId),
    FOREIGN KEY (postId) REFERENCES post(postId) ON DELETE CASCADE,
    FOREIGN KEY (userId) REFERENCES user(userId) ON DELETE CASCADE
);

CREATE TABLE bookmark (
    postId INT NOT NULL,
    userId INT NOT NULL,
    PRIMARY KEY (postId, userId),
    FOREIGN KEY (postId) REFERENCES post(postId) ON DELETE CASCADE,
    FOREIGN KEY (userId) REFERENCES user(userId) ON DELETE CASCADE
);

CREATE TABLE hashtag (
    hashTag VARCHAR(100) NOT NULL,
    postId INT NOT NULL,
    PRIMARY KEY (hashTag, postId),
    FOREIGN KEY (postId) REFERENCES post(postId) ON DELETE CASCADE
);

CREATE TABLE follow (
    followerUserId INT NOT NULL,
    followeeUserId INT NOT NULL,
    PRIMARY KEY (followerUserId, followeeUserId),
    FOREIGN KEY (followerUserId) REFERENCES user(userId) ON DELETE CASCADE,
    FOREIGN KEY (followeeUserId) REFERENCES user(userId) ON DELETE CASCADE
);
