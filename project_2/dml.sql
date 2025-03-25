-- homepage get followUsers
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

-- Profile page
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

-- Hashtag search 
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