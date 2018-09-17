/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 * Created on 15/9/2018
 */

package com.smattnews.controllers.front;

import com.smattnews.config.Routes;
import com.smattnews.service.admin.posts.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Seun Matt on 06-Apr-18
 */
@RestController
public class IndexController {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private PostService postService;

    @Autowired
    public IndexController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = {Routes.HOME}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity index() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", "Welcome to SmattNews! An API service for reading the latest news by our amazing authors");
        return  ResponseEntity.ok(response);
    }

    @GetMapping(value = {Routes.Front.FRONT_NEWS}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity news(HttpServletRequest request) {

        Optional<String> limit = Optional.ofNullable(request.getParameter("limit"));
        Optional<String> offset = Optional.ofNullable(request.getParameter("offset"));
        Map<String, Object> response = postService.allPosts(limit, offset);
        if(Boolean.parseBoolean(response.get("status").toString())) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping(value = {Routes.Front.FRONT_NEWS_DETAILS}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity postDetails(@PathVariable("id") String postId) {
        Map<String, Object> response = postService.postDetails(postId);
        if(Boolean.parseBoolean(response.get("status").toString())) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(response);
    }



}
