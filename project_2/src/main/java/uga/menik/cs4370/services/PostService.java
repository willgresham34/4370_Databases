package uga.menik.cs4370.services;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.logging.log4j.util.StringBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uga.menik.cs4370.models.Comment;
import uga.menik.cs4370.models.ExpandedPost;
import uga.menik.cs4370.models.Post;
import uga.menik.cs4370.models.User;

/*
 * This service contains post related functions 
 */
@Service
public class PostService {

    private final DataSource dataSource;
    private final UserService userService;

    @Autowired
    public PostService(DataSource dataSource, UserService userService) {
        this.dataSource = dataSource;
        this.userService = userService;
    }

    public List<Post> getFollowedUsersPosts(String loggedInUser) {
        String sql = """
                    SELECT p.postId, p.postText, p.postDate,
                           u.userId, u.firstName, u.lastName,
                           (SELECT COUNT(*) FROM Heart h WHERE h.postId = p.postId) AS heartsCount,
                           (SELECT COUNT(*) FROM Comment c WHERE c.postId = p.postId) AS commentsCount,
                           (SELECT COUNT(*) FROM Heart h WHERE h.postId = p.postId AND h.userId = ?) AS isHearted,
                           (SELECT COUNT(*) FROM Bookmark bm WHERE bm.postId = p.postId AND bm.userId = ?) AS isBookmarked
                    FROM Post p, Follow f, User u
                    WHERE p.userId = f.followeeUserId
                      AND f.followerUserId = ?
                      AND p.userId = u.userId
                    ORDER BY p.postDate DESC
                """;

        List<Post> posts = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, loggedInUser);
            pstmt.setString(2, loggedInUser);
            pstmt.setString(3, loggedInUser);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User(rs.getString("userId"), rs.getString("firstName"), rs.getString("lastName"));
                    Post post = new Post(
                            rs.getString("postId"),
                            rs.getString("postText"),
                            rs.getString("postDate"),
                            user,
                            rs.getInt("heartsCount"),
                            rs.getInt("commentsCount"),
                            rs.getInt("isHearted") > 0,
                            rs.getInt("isBookmarked") > 0);
                    posts.add(post);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return posts;
    }

    public List<Post> getPostByUserId(String userId) {
        String sql = """
                    SELECT p.postId, p.postText, p.postDate,
                           u.userId, u.firstName, u.lastName,
                           (SELECT COUNT(*) FROM Heart h WHERE h.postId = p.postId) AS heartsCount,
                           (SELECT COUNT(*) FROM Comment c WHERE c.postId = p.postId) AS commentsCount,
                           (SELECT COUNT(*) FROM Heart h WHERE h.postId = p.postId AND h.userId = ?) AS isHearted,
                           (SELECT COUNT(*) FROM Bookmark bm WHERE bm.postId = p.postId AND bm.userId = ?) AS isBookmarked
                    FROM Post p, User u
                    WHERE p.userId = ?
                      AND p.userId = u.userId
                    ORDER BY p.postDate DESC
                """;

        List<Post> posts = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, userId);
            pstmt.setString(3, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User(rs.getString("userId"), rs.getString("firstName"), rs.getString("lastName"));
                    Post post = new Post(
                            rs.getString("postId"),
                            rs.getString("postText"),
                            rs.getString("postDate"),
                            user,
                            rs.getInt("heartsCount"),
                            rs.getInt("commentsCount"),
                            rs.getInt("isHearted") > 0,
                            rs.getInt("isBookmarked") > 0);
                    posts.add(post);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return posts;
    }

    public List<ExpandedPost> constructExpandedPost(String postId) {
        // Fetch current user, create sql statements
        String currentUserId = userService.getLoggedInUser().getUserId();
        final String sql = "select * from Post where postId = ?";
        final String sql2 = "select * from Comment where postId = ? order by commentDate DESC";
        final String sql3 = "select * from User where userId = ?";
        final String sql4 = "select * from Heart where postId = ?";
        final String sql5 = "select * from Bookmark where postId = ?";

        // Prepare statements
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                PreparedStatement pstmt2 = conn.prepareStatement(sql2);
                PreparedStatement pstmt3 = conn.prepareStatement(sql3);
                PreparedStatement pstmt4 = conn.prepareStatement(sql4);
                PreparedStatement pstmt5 = conn.prepareStatement(sql5);) {

            // Fetch user Id for post
            pstmt.setString(1, postId);
            ResultSet rs1 = pstmt.executeQuery();
            rs1.next();
            String postUserId = rs1.getString("userId");

            pstmt2.setString(1, postId);
            ResultSet rs2 = pstmt2.executeQuery();

            pstmt3.setString(1, postUserId);
            ResultSet rs3 = pstmt3.executeQuery();
            rs3.next();

            // fetch postDate and postText
            String postText = rs1.getString("postText");
            String postDate = rs1.getString("postDate");

            // define user for expanded post
            User postUser = new User(postUserId, rs3.getString("firstName"), rs3.getString("lastName"));

            // Get heart count and check if current user hads liked the post
            int heartsCount = 0;
            boolean isHearted = false;
            pstmt4.setString(1, postId);
            ResultSet rs4 = pstmt4.executeQuery();
            while (rs4.next()) {
                heartsCount++;
                if (currentUserId.equals(rs4.getString("userId"))) {
                    isHearted = true;
                }
            }
            // find comment size
            int commentsSize = 0;
            while (rs2.next()) {
                commentsSize++;
            }
            // check if bookmarked
            boolean isBookmarked = false;
            pstmt5.setString(1, postId);
            ResultSet rs5 = pstmt5.executeQuery();

            while (rs5.next()) {
                if (currentUserId.equals(rs5.getString("userId"))) {
                    isBookmarked = true;
                }
            }

            // create list of comments
            List<Comment> commentsForPost = new ArrayList<>();
            rs2 = pstmt2.executeQuery();

            while (rs2.next()) {
                // Fetch user info for current comment in result set
                PreparedStatement pstmt6 = conn.prepareStatement(sql3);
                pstmt6.setString(1, rs2.getString("userId"));
                ResultSet set = pstmt6.executeQuery();
                set.next();

                User commentUser = new User(set.getString("userId"), set.getString("firstName"),
                        set.getString("lastName"));

                Comment temp = new Comment(postId, rs2.getString("commentText"),
                        rs2.getString("commentDate"), commentUser);
                commentsForPost.add(temp);
            }

            // Contruct expanded post and return
            ExpandedPost postWithComments = new ExpandedPost(postId, postText, postDate,
                    postUser, heartsCount, commentsSize, isHearted, isBookmarked, commentsForPost);

            return List.of(postWithComments);

        } catch (SQLException e) {
            e.printStackTrace();
            String message = URLEncoder.encode("Post info not found",
                    StandardCharsets.UTF_8);
            return null;
        }
    }

    public Boolean commentToDatabase(String postId, String comment) {
        // Fetch current user, create sql statement
        String currentUserId = userService.getLoggedInUser().getUserId();
        final String sql = "insert into Comment (postId, userId, commentText) values (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, postId);
            pstmt.setString(2, currentUserId);
            pstmt.setString(3, comment);
            pstmt.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean bookmarkToDatabase(String postId, Boolean isAdd) {

        final String sql = "insert into Bookmark (postId, userId) values (?, ?)";
        final String sql2 = "delete from Bookmark where postId = ? and userId = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt2 = conn.prepareStatement(sql);
                PreparedStatement pstmt3 = conn.prepareStatement(sql2)) {
            String currentUserId = userService.getLoggedInUser().getUserId();
            if (isAdd) {
                pstmt2.setString(1, postId);
                pstmt2.setString(2, currentUserId);
                pstmt2.executeUpdate();
            } else {
                pstmt3.setString(1, postId);
                pstmt3.setString(2, currentUserId);
                pstmt3.executeUpdate();

            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        }
    }

    public Boolean heartToDatabase(String postId, Boolean isAdd) {
        final String sql2 = "insert into Heart (postId, userId) values (?, ?)";
        final String sql3 = "delete from Heart where postId = ? and userId = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt2 = conn.prepareStatement(sql2);
                PreparedStatement pstmt3 = conn.prepareStatement(sql3)) {
            String currentUserId = userService.getLoggedInUser().getUserId();
            if (isAdd) {
                pstmt2.setString(1, postId);
                pstmt2.setString(2, currentUserId);
                pstmt2.executeUpdate();
            } else {
                pstmt3.setString(1, postId);
                pstmt3.setString(2, currentUserId);
                pstmt3.executeUpdate();

            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean postToDatabase(String postText) {
        final String sql = "insert into Post (userId, postText) values (?, ?)";
        final String sql2 = "select * from Post where postText = ?";
        final String sql3 = "insert into Hashtag (hashTag, postId) values (?, ?)";
        String currentUserID = userService.getLoggedInUser().getUserId();

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {

            pstmt.setString(1, currentUserID);
            pstmt.setString(2, postText);
            pstmt.executeUpdate();

            pstmt2.setString(1, postText);
            ResultSet rs = pstmt2.executeQuery();

            List<String> hashtags = new ArrayList<>();

            Pattern pattern = Pattern.compile("#(\\w+)");
            Matcher matcher = pattern.matcher(postText);

            if (rs.next()) {
                String postID = rs.getString("postId");
                while (matcher.find()) {
                    String word = matcher.group(1);
                    PreparedStatement pstmt3 = conn.prepareStatement(sql3);
                    pstmt3.setString(1, word);
                    pstmt3.setString(2, postID);
                    pstmt3.executeUpdate();
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Post> searchPostByHashtags(String hashtags, String loggedInUser) {

        if (hashtags == null || hashtags.isEmpty()) {
            throw new IllegalArgumentException("Hashtag list cannot be empty");
        }

        String[] hashtagsArr = hashtags.split("\\s+");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hashtagsArr.length; i++) {
            sb.append("?");
            if (i < hashtagsArr.length - 1) {
                sb.append(", ");
            }
        }
        System.out.println(sb.toString());

        String sql = """
                SELECT DISTINCT p.postId, p.postText, p.postDate,
                    u.userId, u.firstName, u.lastName,
                    (SELECT COUNT(*) FROM Heart h WHERE h.postId = p.postId) AS heartsCount,
                    (SELECT COUNT(*) FROM Comment c WHERE c.postId = p.postId) AS commentsCount,
                    (SELECT COUNT(*) FROM Heart h WHERE h.postId = p.postId AND h.userId = ?) AS isHearted,
                    (SELECT COUNT(*) FROM Bookmark b WHERE b.postId = p.postId AND b.userId = ?) AS isBookmarked
                FROM Post p, User u, Hashtag h
                WHERE p.userId = u.userId
                AND h.postId = p.postId
                AND h.hashTag IN (%s)
                ORDER BY p.postDate DESC;
                """.formatted(sb.toString());

        List<Post> posts = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, loggedInUser);
            pstmt.setString(2, loggedInUser);
            pstmt.setString(3, sb.toString());

            for (int i = 0; i < hashtagsArr.length; i++) {
                pstmt.setString(i + 3, hashtagsArr[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User(rs.getString("userId"), rs.getString("firstName"), rs.getString("lastName"));
                    Post post = new Post(
                            rs.getString("postId"),
                            rs.getString("postText"),
                            rs.getString("postDate"),
                            user,
                            rs.getInt("heartsCount"),
                            rs.getInt("commentsCount"),
                            rs.getInt("isHearted") > 0,
                            rs.getInt("isBookmarked") > 0);
                    posts.add(post);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return posts;
    }

};
