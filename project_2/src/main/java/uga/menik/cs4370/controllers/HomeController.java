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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import uga.menik.cs4370.models.Post;
import uga.menik.cs4370.services.UserService;
import uga.menik.cs4370.utility.Utility;

/**
 * This controller handles the home page and some of it's sub URLs.
 */
@Controller
@RequestMapping
public class HomeController {
    private final UserService userService;
    private final DataSource dataSource;

    public HomeController(UserService u, DataSource d) {
        this.userService = u;
        this.dataSource = d;
    }

    /**
     * This is the specific function that handles the root URL itself.
     * 
     * Note that this accepts a URL parameter called error.
     * The value to this parameter can be shown to the user as an error message.
     * See notes in HashtagSearchController.java regarding URL parameters.
     */
    @GetMapping
    public ModelAndView webpage(@RequestParam(name = "error", required = false) String error) {
        // See notes on ModelAndView in BookmarksController.java.
        ModelAndView mv = new ModelAndView("home_page");
        List<Post> posts = Utility.createSamplePostsListWithoutComments();
        // Following line populates sample data.
        // You should replace it with actual data from the database.
        /*
         * 
         * Calling get post service
         * 
         */

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
     * This function handles the /createpost URL.
     * This handles a post request that is going to be a form submission.
     * The form for this can be found in the home page. The form has a
     * input field with name = posttext. Note that the @RequestParam
     * annotation has the same name. This makes it possible to access the value
     * from the input from the form after it is submitted.
     */
    @PostMapping("/createpost")
    public String createPost(@RequestParam(name = "posttext") String postText) {
        System.out.println("User is creating post: " + postText);
        String currentUserID = userService.getLoggedInUser().getUserId();
        
        final String sql = "insert into Post (userId, postText) values (?, ?)";
        final String sql2 = "select * from Post where postText = ?";
        final String sql3 = "insert into Hashtag (hashTag, postId) values (?, ?)";

        try (Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            PreparedStatement pstmt2 = conn.prepareStatement(sql2)) 
            {
                
                pstmt.setString(1, currentUserID);
                pstmt.setString(2, postText);
                pstmt.executeUpdate();

                pstmt2.setString(1, postText);
                ResultSet rs = pstmt2.executeQuery();

                List<String> hashtags = new ArrayList<>();
        
                Pattern pattern = Pattern.compile("#(\\w+)");
                Matcher matcher = pattern.matcher(postText);

                if(rs.next()) {
                    String postID = rs.getString("postId");
                    while (matcher.find()) {
                        String word = matcher.group(1);
                        PreparedStatement pstmt3 = conn.prepareStatement(sql3);
                        pstmt3.setString(1, word);
                        pstmt3.setString(2, postID);
                        pstmt3.executeUpdate();
                    }
                }


                return "redirect:/";

            } catch (SQLException e) {
                e.printStackTrace();
                String message = URLEncoder.encode("Failed to create the post. Please try again.",
                StandardCharsets.UTF_8);
                return "redirect:/?error=" + message;
            }
    }

}
