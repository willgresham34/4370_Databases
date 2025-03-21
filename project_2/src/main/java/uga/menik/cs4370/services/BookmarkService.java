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
     * This function should query and return all bookmarked posts from the user (specified by userId).
     * The list is ordered by most recent first.
     */
    public List<Post> getBookmarkedPosts(String userId) throws SQLException {

        final String sql = """
            select 
                post.postId as postId, postText, postDate,
                user.userId as userId, firstName, lastName,
                (select count(*) from Heart h where h.postId = post.postId) as heartsCount,
                (select count(*) from Comment c where c.postId = post.postId) as commentsCount,
                (post.postId in (select heart.postId from heart where userId = ?)) as isHearted,
                (post.postId in (select bookmark.postId from bookmark where userId = ?)) as isBookmarked
            from 
                post, bookmark, user
            where 
                user.userId = post.userId and
                post.postId = bookmark.postId and
                user.userId = ?
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
                        rs.getString("lastName")
                    );
                    
                    // Create post
                    Post post = new Post(
                        rs.getString("postId"),
                        rs.getString("postText"),
                        rs.getString("postDate"),
                        user,
                        rs.getInt("heartsCount"),
                        rs.getInt("commentsCount"),
                        rs.getInt("isHearted") == 1,
                        rs.getInt("isBookmarked") == 1
                    );
                    bookmarkedPosts.add(post);
                }
                return bookmarkedPosts;
            }
        }
    }
}
