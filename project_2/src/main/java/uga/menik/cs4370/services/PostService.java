package uga.menik.cs4370.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import uga.menik.cs4370.models.Post;
import uga.menik.cs4370.models.User;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * This service contains post related functions 
 */
@Service
public class PostService {

    private final DataSource dataSource;

    @Autowired
    public PostService(DataSource dataSource) {
        this.dataSource = dataSource;
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
};
