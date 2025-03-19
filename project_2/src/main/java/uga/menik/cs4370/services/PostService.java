package uga.menik.cs4370.services;

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
    private final User loggedInUser = null;

    @Autowired
    public PostService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Post> getFollowedUserPosts() {
        // get followed users

        // get all posts of followed users

        //
        return null;
    }

    public List<Post> getPostByUserId() {
        return null;
    };

}
