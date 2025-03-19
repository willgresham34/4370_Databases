-- Create the database.
DROP DATABASE cs4370_mb_platform;

CREATE DATABASE IF NOT EXISTS cs4370_mb_platform;

-- Use the created database.
USE cs4370_mb_platform;

-- Create the user table.
CREATE TABLE
    User (
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

CREATE TABLE
    Post (
        postId INT PRIMARY KEY AUTO_INCREMENT,
        userId INT NOT NULL,
        postDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        postText TEXT NOT NULL,
        FOREIGN KEY (userId) REFERENCES user (userId) ON DELETE CASCADE
    );

CREATE TABLE
    Comment (
        commentId INT PRIMARY KEY AUTO_INCREMENT,
        postId INT NOT NULL,
        userId INT NOT NULL,
        commentDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        commentText TEXT NOT NULL,
        FOREIGN KEY (postId) REFERENCES post (postId) ON DELETE CASCADE,
        FOREIGN KEY (userId) REFERENCES user (userId) ON DELETE CASCADE
    );

CREATE TABLE
    Heart (
        postId INT NOT NULL,
        userId INT NOT NULL,
        PRIMARY KEY (postId, userId),
        FOREIGN KEY (postId) REFERENCES post (postId) ON DELETE CASCADE,
        FOREIGN KEY (userId) REFERENCES user (userId) ON DELETE CASCADE
    );

CREATE TABLE
    Bookmark (
        postId INT NOT NULL,
        userId INT NOT NULL,
        PRIMARY KEY (postId, userId),
        FOREIGN KEY (postId) REFERENCES post (postId) ON DELETE CASCADE,
        FOREIGN KEY (userId) REFERENCES user (userId) ON DELETE CASCADE
    );

CREATE TABLE
    Hashtag (
        hashTag VARCHAR(100) NOT NULL,
        postId INT NOT NULL,
        PRIMARY KEY (hashTag, postId),
        FOREIGN KEY (postId) REFERENCES post (postId) ON DELETE CASCADE
    );

CREATE TABLE
    Follow (
        followerUserId INT NOT NULL,
        followeeUserId INT NOT NULL,
        PRIMARY KEY (followerUserId, followeeUserId),
        FOREIGN KEY (followerUserId) REFERENCES user (userId) ON DELETE CASCADE,
        FOREIGN KEY (followeeUserId) REFERENCES user (userId) ON DELETE CASCADE
    );

INSERT INTO
    User
VALUES
    (
        1,
        "willgresham34",
        "willpass123",
        "Will",
        "Gresham"
    ),
    (
        2,
        "adamwright12",
        "adampass123",
        "Adam",
        "Wright"
    ),
    (
        3,
        "connorstephen56",
        "connorpass123",
        "Connor",
        "Stephens"
    ),
    (
        4,
        "anthonycampo78",
        "anthonypass123",
        "Anthony",
        "Campo"
    );

INSERT INTO
    Post
VALUES
    (1, 1, now (), "Hello World - Will "),
    (2, 1, now (), "SQL is fun - Also Will"),
    (3, 1, now (), "Hello World - Adam"),
    (4, 1, now (), "Hello World - Connor"),
    (5, 1, now (), " Hello World -Anthony");

INSERT INTO
    Comment
VALUES
    (1, 2, 3, now (), "Cool Post - Connor"),
    (2, 2, 2, now (), "Cool Post - Adam"),
    (3, 3, 1, now (), "Cool Post - Will"),
    (4, 4, 1, now (), "Cool Post - Will");

INSERT INTO
    Heart
VALUES
    (2, 3),
    (2, 2),
    (3, 1);

INSERT INTO
    Bookmark
VALUES
    (1, 3),
    (5, 1),
    (3, 4);

INSERT INTO
    Hashtag
VALUES
    ("HelloWorld", 1),
    ("HelloWorld", 3),
    ("HelloWorld", 4),
    ("HelloWorld", 5);

INSERT INTO
    Follow
VALUES
    (1, 3),
    (1, 2),
    (1, 4),
    (2, 4);