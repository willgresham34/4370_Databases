/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.cs4370.controllers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uga.menik.cs4370.models.Comment;
import uga.menik.cs4370.models.ExpandedPost;
import uga.menik.cs4370.models.User;
import uga.menik.cs4370.services.UserService;
import uga.menik.cs4370.utility.Utility;

/**
 * Handles /post URL and its sub urls.
 */
@Controller
@RequestMapping("/post")
public class PostController {


    private final UserService userService;
    private final DataSource dataSource;

    public PostController(UserService u, DataSource d) {
        this.userService = u;
        this.dataSource = d;
    }

    /**
     * This function handles the /post/{postId} URL.
     * This handlers serves the web page for a specific post.
     * Note there is a path variable {postId}.
     * An example URL handled by this function looks like below:
     * http://localhost:8081/post/1
     * The above URL assigns 1 to postId.
     * 
     * See notes from HomeController.java regardig error URL parameter.
     */
    @GetMapping("/{postId}")
    public ModelAndView webpage(@PathVariable("postId") String postId,
            @RequestParam(name = "error", required = false) String error) {
        System.out.println("The user is attempting to view post with id: " + postId);
        // See notes on ModelAndView in BookmarksController.java.
        ModelAndView mv = new ModelAndView("posts_page");

        // Following line populates sample data.
        // You should replace it with actual data from the database.
        List<ExpandedPost> posts = Utility.createSampleExpandedPostWithComments();
        mv.addObject("posts", posts);

        // If an error occured, you can set the following property with the
        // error message to show the error message to the user.
        // An error message can be optionally specified with a url query parameter too.
        String errorMessage = error;
        mv.addObject("errorMessage", errorMessage);

        // Enable the following line if you want to show no content message.
        // Do that if your content list is empty.
        // mv.addObject("isNoContent", true);

        return mv;
    }

    /**
     * Handles comments added on posts.
     * See comments on webpage function to see how path variables work here.
     * This function handles form posts.
     * See comments in HomeController.java regarding form submissions.
     */
    @PostMapping("/{postId}/comment")
    public String postComment(@PathVariable("postId") String postId,
            @RequestParam(name = "comment") String comment) {
                //NEEDS TESTING. IMPLEMENTATION ONLY
        System.out.println("The user is attempting add a comment:");
        System.out.println("\tpostId: " + postId);
        System.out.println("\tcomment: " + comment);

        String currentUserId = userService.getLoggedInUser().getUserId();
        final String sql = "insert into Comment (postId, userId, commentText) values (?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql))
            {
                pstmt.setString(1, postId);
                pstmt.setString(2, currentUserId);
                pstmt.setString(3, comment);
                pstmt.executeUpdate();

                return "redirect:/post/" + postId;

            } catch (SQLException e) {
                e.printStackTrace();
                String message = URLEncoder.encode("Failed to post the comment. Please try again.",
                StandardCharsets.UTF_8);
                return "redirect:/post/" + postId + "?error=" + message;

            }

        // Redirect the user if the comment adding is a success.
        

        // Redirect the user with an error message if there was an error.
        
    }

    /**
     * Handles likes added on posts.
     * See comments on webpage function to see how path variables work here.
     * See comments in PeopleController.java in followUnfollowUser function regarding 
     * get type form submissions and how path variables work.
     */
    @GetMapping("/{postId}/heart/{isAdd}")
    public String addOrRemoveHeart(@PathVariable("postId") String postId,
            @PathVariable("isAdd") Boolean isAdd) {
        System.out.println("The user is attempting add or remove a heart:");
        System.out.println("\tpostId: " + postId);
        System.out.println("\tisAdd: " + isAdd);

        final String sql2 = "insert into Heart (postId, userId) values (?, ?)";
        final String sql3 = "delete from Heart where postId = ? and userId = ?";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            PreparedStatement pstmt3 = conn.prepareStatement(sql3)) 
            {
                String currentUserId = userService.getLoggedInUser().getUserId();
                if(isAdd) {
                    pstmt2.setString(1, postId);
                    pstmt2.setString(2, currentUserId);
                    pstmt2.executeUpdate();
                } else {
                    pstmt3.setString(1, postId);
                    pstmt3.setString(2, currentUserId);
                    pstmt3.executeUpdate();

                }
                return "redirect:/post/" + postId;

            } catch (SQLException e) {
                e.printStackTrace();
                String message = URLEncoder.encode("Failed to (un)like the post. Please try again.",
                StandardCharsets.UTF_8);
                return "redirect:/post/" + postId + "?error=" + message;
            }
    }

    /**
     * Handles bookmarking posts.
     * See comments on webpage function to see how path variables work here.
     * See comments in PeopleController.java in followUnfollowUser function regarding 
     * get type form submissions.
     */
    @GetMapping("/{postId}/bookmark/{isAdd}")
    public String addOrRemoveBookmark(@PathVariable("postId") String postId,
            @PathVariable("isAdd") Boolean isAdd) {
        System.out.println("The user is attempting add or remove a bookmark:");
        System.out.println("\tpostId: " + postId);
        System.out.println("\tisAdd: " + isAdd);

        final String sql = "insert into Bookmark (postId, userId) values (?, ?)";
        final String sql2 = "delete from Bookmark where postId = ? and userId = ?";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt2 = conn.prepareStatement(sql);
            PreparedStatement pstmt3 = conn.prepareStatement(sql2)) 
            {
                String currentUserId = userService.getLoggedInUser().getUserId();
                if(isAdd) {
                    pstmt2.setString(1, postId);
                    pstmt2.setString(2, currentUserId);
                    pstmt2.executeUpdate();
                } else {
                    pstmt3.setString(1, postId);
                    pstmt3.setString(2, currentUserId);
                    pstmt3.executeUpdate();

                }
                return "redirect:/post/" + postId;

            } catch (SQLException e) {
                e.printStackTrace();
                String message = URLEncoder.encode("Failed to (un)bookmark the post. Please try again.",
                StandardCharsets.UTF_8);
                return "redirect:/post/" + postId + "?error=" + message;
            }
    }

    public List <ExpandedPost> constructExpandedPost(String postId) {
        //IMPLEMENTATION ONLY. NEEDS TESTING
        String currentUserId = userService.getLoggedInUser().getUserId();
        final String sql = "select * from Post where postId = ?";
        final String sql2 = "select * from Comment where postId = ? order by commentDate DESC";
        final String sql3 = "select * from User where userId = ?";
        final String sql4 = "select * from Heart where postId = ?";
        final String sql5 = "select * from Bookmark where postId = ?";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            PreparedStatement pstmt3 = conn.prepareStatement(sql3);
            PreparedStatement pstmt4 = conn.prepareStatement(sql4);
            PreparedStatement pstmt5 = conn.prepareStatement(sql5);
            )
            {
                pstmt.setString(1, postId);
                ResultSet rs1 = pstmt.executeQuery();
                rs1.next();
                String postUserId = rs1.getString("userId");
                

                pstmt2.setString(1, postId);
                ResultSet rs2 = pstmt2.executeQuery();

                pstmt3.setString(1, postUserId);
                ResultSet rs3 = pstmt3.executeQuery();
                rs3.next();     

                //postId found
                String postText = rs1.getString("postText");
                String postDate = rs1.getString("postDate");

                //define user for expanded post
                User postUser = new User(postUserId, rs3.getString("firstName"), rs3.getString("lastName"));

                int heartsCount = 0;
                boolean isHearted = false;
                pstmt4.setString(1, postId);
                ResultSet rs4 = pstmt.executeQuery(); 
                while(rs4.next()) {
                    heartsCount++;
                    if(currentUserId.equals(rs4.getString("userId"))) {
                        isHearted = true;
                    }
                }
                //find size, check if hearted
                int commentsSize = 0;
                if (rs2.last()) { 
                    commentsSize = rs2.getRow(); 
                    rs2.beforeFirst(); 
                }
                //check if bookmarked
                boolean isBookmarked = false;
                pstmt5.setString(1, postId);
                ResultSet rs5 = pstmt5.executeQuery();

                while(rs5.next()) {
                    if(currentUserId.equals(rs5.getString("userId"))) {
                        isBookmarked = true;
                    }
                }

                //create list of comments
                List<Comment> commentsForPost = new ArrayList<>();

                while(rs2.next()) {
                    //Fetch user info for current comment
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

                ExpandedPost postWithComments = new ExpandedPost(postId, postText, postDate, 
                    postUser, heartsCount, commentsSize, isHearted, isBookmarked, commentsForPost);
                
                return List.of(postWithComments);


            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
    }

}
