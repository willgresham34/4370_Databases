/**
 * Copyright (c) 2024 Sami Menik, PhD. All rights reserved.
 * 
 * This is a project developed by Dr. Menik to give the students an opportunity to apply database
 * concepts learned in the class in a real world project. Permission is granted to host a running
 * version of this software and to use images or videos of this work solely for the purpose of
 * demonstrating the work to potential employers. Any form of reproduction, distribution, or
 * transmission of the software's source code, in part or whole, without the prior written consent
 * of the copyright owner, is strictly prohibited.
 */
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

import uga.menik.cs4370.models.FollowableUser;
import uga.menik.cs4370.utility.Utility;

/**
 * This service contains people related functions.
 */
@Service
public class PeopleService {

    // dataSource enables talking to the database.
    private final DataSource dataSource;

    @Autowired
    public PeopleService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * This function should query and return all users that are followable. The list should not
     * contain the user with id userIdToExclude.
     */
    public List<FollowableUser> getFollowableUsers(String userIdToExclude) throws SQLException {
        // Write an SQL query to find the users that are not the current user.
        final String sql = "select user.userId as userId, firstName, lastName, " +
        "(user.userId in (select followeeUserId " +
        "from follow where followerUserId = ?)) "  +
        "as isFollowed, lastActive from user " +
        "left join (select userId, max(postDate) as " +
        "lastActive from post group by userId) " + 
        "as userLastActive on userLastActive.userId = " + 
        "user.userId where user.userId != ?";
        
        // Run the query with a datasource.
        // See UserService.java to see how to inject DataSource instance and
        // use it to run a query.
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Following line replaces the first place holder with userIdToExclude.
            pstmt.setString(1, userIdToExclude);
            pstmt.setString(2, userIdToExclude);

            System.out.println("Executing query:");

            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println(rs);
                // Use the query result to create a list of followable users.
                // See UserService.java to see how to access rows and their attributes
                // from the query result.
                // Check the following createSampleFollowableUserList function to see
                // how to create a list of FollowableUsers.
                List<FollowableUser> followableUsers = new ArrayList<>();
                while (rs.next()) {

                    // Get attributes of next user
                    String userId = rs.getString("userId");
                    String firstName = rs.getString("firstName");
                    String lastName = rs.getString("lastName");
                    Boolean isFollowed = rs.getString("isFollowed").equals("1");
                    String lastActiveDate = rs.getString("lastActive");
                    if (lastActiveDate == null) {lastActiveDate = "No posts yet";}

                    // Create followable user, isFollowed and lastActiveDate being placeholders
                    FollowableUser followableUser = new FollowableUser(userId, firstName, lastName,
                            isFollowed, lastActiveDate);

                    // Add to list
                    followableUsers.add(followableUser);
                }
                return followableUsers;
            }
        }
    }

    /**
     * This function allows users to (un)follow a user.
     */
    public Boolean followUnfollowUser(String followerUserId, String followeeUserId,
            Boolean isFollow) throws SQLException {

        final String sqlFollow =
                "insert into Follow (followerUserId, followeeUserId) values (?, ?)";
        final String sqlUnfollow =
                "delete from Follow where followerUserId = ? and followeeUserId = ?";

        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt =
                        conn.prepareStatement(isFollow ? sqlFollow : sqlUnfollow)) {

            pstmt.setString(1, followerUserId);
            pstmt.setString(2, followeeUserId);
            pstmt.executeUpdate();
            return true;
        }
    }

}
