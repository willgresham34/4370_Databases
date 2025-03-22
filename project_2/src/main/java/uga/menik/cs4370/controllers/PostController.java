/**
Copyright (c) 2024 Sami Menik, PhD. All rights reserved.

This is a project developed by Dr. Menik to give the students an opportunity to apply database concepts learned in the class in a real world project. Permission is granted to host a running version of this software and to use images or videos of this work solely for the purpose of demonstrating the work to potential employers. Any form of reproduction, distribution, or transmission of the software's source code, in part or whole, without the prior written consent of the copyright owner, is strictly prohibited.
*/
package uga.menik.cs4370.controllers;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uga.menik.cs4370.models.ExpandedPost;
import uga.menik.cs4370.services.PostService;
import uga.menik.cs4370.services.UserService;

/**
 * Handles /post URL and its sub urls.
 */
@Controller
@RequestMapping("/post")
public class PostController {


    private final UserService userService;
    private final DataSource dataSource;
    private final PostService postService;

    public PostController(UserService u, DataSource d, PostService p) {
        this.userService = u;
        this.dataSource = d;
        this.postService = p;
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
        List<ExpandedPost> posts = postService.constructExpandedPost(postId);
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
        System.out.println("The user is attempting add a comment:");
        System.out.println("\tpostId: " + postId);
        System.out.println("\tcomment: " + comment);

        //Fetch current user, create sql statement
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

}
