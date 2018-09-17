/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 * Created on 15/9/2018
 */

package com.smattnews.controllers.admin;

import com.smattnews.beans.SessionConfig;
import com.smattnews.config.Routes;
import com.smattnews.models.Post;
import com.smattnews.models.User;
import com.smattnews.models.stereotypes.PostBean;
import com.smattnews.service.admin.posts.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * Created by Seun Matt on 15-Sep-18
 */
@RestController
public class AdminPostsController {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private PostService postService;
    private SessionConfig sessionConfig;

    @Autowired
    public AdminPostsController(PostService postService, SessionConfig sessionConfig) {
        this.postService = postService;
        this.sessionConfig = sessionConfig;
    }

    @PostMapping(value = {Routes.App.ADMIN_ADD_NEW_POST}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity addNewPost(@RequestBody PostBean postBean) {

        logger.debug("mapped postBean: \n" + postBean);

        User loggedInUser = sessionConfig.getLoggedInUser();
        Map<String, Object> response = postService.addPost(loggedInUser, postBean);
        logger.debug("response from creating new post: " + response);

        if(Boolean.parseBoolean(response.get("status").toString())) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @PatchMapping(value = {Routes.App.ADMIN_UPDATE_POST}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity updatePost(@PathVariable("id") String id, @RequestBody Post post) {
        logger.debug("post in update post: \n" + post);
        User loggedInUser = sessionConfig.getLoggedInUser();
        Map<String, Object> response = postService.updatePost(id, post, loggedInUser);
        logger.debug("response from updating post: " + response);

        if(Boolean.parseBoolean(response.get("status").toString())) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(response);
    }


    @DeleteMapping(value = {Routes.App.ADMIN_DELETE_POST}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity deletePost(@PathVariable("id") String id) {
        User loggedInUser = sessionConfig.getLoggedInUser();
        Map<String, Object> response = postService.deletePost(id, loggedInUser);
        logger.debug("response from deleting post: " + response);

        if(Boolean.parseBoolean(response.get("status").toString())) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(response);
    }


}
