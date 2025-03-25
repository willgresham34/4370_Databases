/*This query combines data from the User, Post and Follow table to return a list
of posts from the users the logged in user follows. It also uses data from Bookmark
and Hear table to tell if the post is liked or bookmarked by the user
It is used at http://localhost:8081/ aka the home page */
SELECT
    p.postId,
    p.postText,
    p.postDate,
    u.userId,
    u.firstName,
    u.lastName,
    (
        SELECT
            COUNT(*)
        FROM
            Heart h
        WHERE
            h.postId = p.postId
    ) AS heartsCount,
    (
        SELECT
            COUNT(*)
        FROM
            Comment c
        WHERE
            c.postId = p.postId
    ) AS commentsCount,
    (
        SELECT
            COUNT(*)
        FROM
            Heart h
        WHERE
            h.postId = p.postId
            AND h.userId = ?
    ) AS isHearted,
    (
        SELECT
            COUNT(*)
        FROM
            Bookmark bm
        WHERE
            bm.postId = p.postId
            AND bm.userId = ?
    ) AS isBookmarked
FROM
    Post p,
    Follow f,
    User u
WHERE
    p.userId = f.followeeUserId
    AND f.followerUserId = ?
    AND p.userId = u.userId
ORDER BY
    p.postDate DESC;

/* This query combines data from the User and Post table to return the list
of posts from the logged in user . It also uses data from Bookmark
and Hear table to tell if the post is liked or bookmarked by the user
It is used at http://localhost:8081/profile aka the profile page */
SELECT
    p.postId,
    p.postText,
    p.postDate,
    u.userId,
    u.firstName,
    u.lastName,
    (
        SELECT
            COUNT(*)
        FROM
            Heart h
        WHERE
            h.postId = p.postId
    ) AS heartsCount,
    (
        SELECT
            COUNT(*)
        FROM
            Comment c
        WHERE
            c.postId = p.postId
    ) AS commentsCount,
    (
        SELECT
            COUNT(*)
        FROM
            Heart h
        WHERE
            h.postId = p.postId
            AND h.userId = ?
    ) AS isHearted,
    (
        SELECT
            COUNT(*)
        FROM
            Bookmark bm
        WHERE
            bm.postId = p.postId
            AND bm.userId = ?
    ) AS isBookmarked
FROM
    Post p,
    User u
WHERE
    p.userId = ?
    AND p.userId = u.userId
ORDER BY
    p.postDate DESC;

/*This query combines data from the User, Post and Hashtag table to return a list
of posts that contain one of the searched hashtags. It also uses data from Bookmark
and Hear table to tell if the post is liked or bookmarked by the user
It is used at http://localhost:8081/ aka the home page */
SELECT DISTINCT
    p.postId,
    p.postText,
    p.postDate,
    u.userId,
    u.firstName,
    u.lastName,
    (
        SELECT
            COUNT(*)
        FROM
            Heart h
        WHERE
            h.postId = p.postId
    ) AS heartsCount,
    (
        SELECT
            COUNT(*)
        FROM
            Comment c
        WHERE
            c.postId = p.postId
    ) AS commentsCount,
    (
        SELECT
            COUNT(*)
        FROM
            Heart h
        WHERE
            h.postId = p.postId
            AND h.userId = ?
    ) AS isHearted,
    (
        SELECT
            COUNT(*)
        FROM
            Bookmark b
        WHERE
            b.postId = p.postId
            AND b.userId = ?
    ) AS isBookmarked
FROM
    Post p,
    User u,
    Hashtag h
WHERE
    p.userId = u.userId
    AND h.postId = p.postId
    AND h.hashTag IN (?)
ORDER BY
    p.postDate DESC;

/*
This query uses data from the User table to return a list containing all
users on the site. It also uses data from Follow and Post table to tell 
if the user is followed by the current logged in user
It is used at http://localhost:8081/people aka the people page 
 */
select
    User.userId as userId,
    firstName,
    lastName,
    (
        User.userId in (
            select
                followeeUserId
            from
                Follow
            where
                followerUserId = ?
        )
    ) as isFollowed,
    lastActive
from
    User
    left join (
        select
            userId,
            max(postDate) as lastActive
        from
            Post
        group by
            userId
    ) as userLastActive on userLastActive.userId = User.userId
where
    User.userId != ?;

/*
This query combines data from the User, Post, and Bookmark table to 
return a list containing all posts bookmarked by the current user. 
It also uses data from Comment, Heart, and Bookmark table to tell 
the hearts and comments count, as well as if the post is bookmarked
and hearted by the current logged in user.
It is used at http://localhost:8081/bookmarks aka the bookmarks page
 */
select
    Post.postId as postId,
    postText,
    postDate,
    User.userId as userId,
    firstName,
    lastName,
    -- find the heart count for each post
    (
        select
            count(*)
        from
            Heart h
        where
            h.postId = Post.postId
    ) as heartsCount,
    (
        select
            count(*)
        from
            Comment c
        where
            c.postId = Post.postId
    ) as commentsCount,
    (
        Post.postId in (
            select
                Heart.postId
            from
                Heart
            where
                userId = ?
        )
    ) as isHearted,
    (
        Post.postId in (
            select
                Bookmark.postId
            from
                Bookmark
            where
                userId = ?
        )
    ) as isBookmarked
from
    Post,
    Bookmark,
    User
where
    User.userId = Post.userId
    and Post.postId = Bookmark.postId
    and Bookmark.userId = ?
order by
    Post.postDate desc;

-- Construct Expanded Post
-- Get post info
SELECT
    *
FROM
    Post
WHERE
    postId = ?;

-- Get comments related to post
SELECT
    *
FROM
    Comment
where
    postId = ?
order by
    commentDate DESC;

-- Find user related to post
SELECT
    *
FROM
    User
where
    userId = ?;

--Fetch hearts related to post
SELECT
    *
FROM
    Heart
WHERE
    postId = ?;

--Find all bookmarks related to post
SELECT
    *
FROM
    Heart
WHERE
    postId = ?;

-- URL: http://localhost:8081/post/<postId>
-- Send comment to database
INSERT INTO
    Comment (postId, userId, commentText)
values
    (?, ?, ?);

--Add or Remove heart from database
INSERT INTO
    Heart (postId, userId)
values
    (?, ?);

DELETE FROM Heart
WHERE
    postId = ?
    and userId = ?;

--Add or Remove bookmark from database
INSERT INTO
    Bookmark (postId, userId)
values
    (?, ?);

DELETE FROM Bookmark
where
    postId = ?
    and userId = ?;

--Add post to database and parse hashtags
--Send post to database
INSERT INTO
    Post (userId, postText)
values
    (?, ?);

--Fetch post ID
SELECT
    *
from
    Post
where
    postText = ?;

--Insert parsed hashtag
INSERT into
    Hashtag (hashtag, postId)
values
    (?, ?);