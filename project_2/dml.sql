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

-- People page query
select
    User.userId as userId, 
    firstName, 
    lastName,
    (
        User.userId in
        (
            select 
                followeeUserId
            from 
                Follow 
            where 
                followerUserId = ?)
    ) as isFollowed,
    lastActive
from
    User
left join
    (
        select 
            userId, 
            max(postDate) as lastActive 
        from 
            Post 
        group by 
            userId
    ) as userLastActive 
on 
    userLastActive.userId = User.userId
where
    User.userId != ?;

-- Bookmarked posts query
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
        Post.postId in 
        (
            select 
                Heart.postId 
            from 
                Heart 
            where 
                userId = ?
        )
    ) as isHearted,
    (
        Post.postId in 
            (
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
        User.userId = Post.userId and
        Post.postId = Bookmark.postId and
        Bookmark.userId = ?
    order by 
        Post.postDate desc;