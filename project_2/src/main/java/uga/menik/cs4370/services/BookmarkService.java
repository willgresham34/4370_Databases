package uga.menik.cs4370.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uga.menik.cs4370.models.Post;
import uga.menik.cs4370.models.User;

/**
 * This service contains people related functions.
 */
@Service
public class BookmarkService {

    // dataSource enables talking to the database.
    private final DataSource dataSource;

    @Autowired
    public BookmarkService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * This function should query and return all bookmarked posts from the user
     * (specified by userId).
     * The list is ordered by most recent first.
     */
    public List<Post> getBookmarkedPosts(String userId) throws SQLException {

        final String sql = """
                    select
                        Post.postId as postId, postText, postDate,
                        User.userId as userId, firstName, lastName,
                        (select count(*) from Heart h where h.postId = Post.postId) as heartsCount,
                        (select count(*) from Comment c where c.postId = Post.postId) as commentsCount,
                        (Post.postId in (select Heart.postId from Heart where userId = ?)) as isHearted,
                        (Post.postId in (select Bookmark.postId from Bookmark where userId = ?)) as isBookmarked
                    from
                        Post, Bookmark, User
                    where
                        User.userId = Post.userId and
                        Post.postId = Bookmark.postId and
                        User.userId = ?
                    order by post.postDate desc;
                """;

        // Run the query with a datasource.
        // See UserService.java to see how to inject DataSource instance and
        // use it to run a query.
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Following line replaces the first place holder with userIdToExclude.
            pstmt.setString(1, userId);
            pstmt.setString(2, userId);
            pstmt.setString(3, userId);

            try (ResultSet rs = pstmt.executeQuery()) {

                List<Post> bookmarkedPosts = new ArrayList<>();

                while (rs.next()) {

                    // Create user for post
                    User user = new User(
                            rs.getString("userId"),
                            rs.getString("firstName"),
                            rs.getString("lastName"));

                    // Create post
                    Post post = new Post(
                            rs.getString("postId"),
                            rs.getString("postText"),
                            rs.getString("postDate"),
                            user,
                            rs.getInt("heartsCount"),
                            rs.getInt("commentsCount"),
                            rs.getInt("isHearted") == 1,
                            rs.getInt("isBookmarked") == 1);
                    bookmarkedPosts.add(post);
                }
                return bookmarkedPosts;
            }
        }
    }
}
