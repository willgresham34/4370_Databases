package uga.menik.cs4370.services;

import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uga.menik.cs4370.models.FollowableUser;

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
    public List<FollowableUser> getBookmarkedPosts(String userIdToExclude) throws SQLException {
        return null;
    }
}
