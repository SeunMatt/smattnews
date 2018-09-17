/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 * Created on 15/9/2018
 */

package com.smattnews.service.admin.posts;

import com.smattnews.config.Constants;
import com.smattnews.daos.PostRepository;
import com.smattnews.models.Post;
import com.smattnews.models.User;
import com.smattnews.models.stereotypes.PostBean;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;


/**
 * Created by Seun Matt on 15-Sep-18
 */
@SuppressWarnings("JavaDoc")
@Service
public class PostService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private PostRepository postRepository;
    private Validator validator;
    private EntityManager entityManager;

    public PostService(PostRepository postRepository, Validator validator, EntityManager entityManager) {
        this.postRepository = postRepository;
        this.validator = validator;
        this.entityManager = entityManager;
    }

    /**
     * add a new post
     * @param user
     * @param postBean
     * @return Map
     */
    @SuppressWarnings("Duplicates")
    @Transactional
    public Map<String, Object> addPost(User user, PostBean postBean) {

        logger.debug("PostBean supplied: " + postBean);

        Map<String, Object> response = new HashMap<>();

        //validate post
        Set<ConstraintViolation<PostBean>> postViolations = validator.validate(postBean);
        if(!postViolations.isEmpty()) {
            Object [] violationArray = postViolations.stream()
                    .map(ConstraintViolation::getMessage)
                    .toArray();
            response.put("status", false);
            response.put("errors", violationArray);
            return response;
        }

        //check for existing post with same title
        Post existingPost = postRepository.findByTitle(postBean.getTitle());
        if(!Objects.isNull(existingPost)) {
            response.put("status", false);
            response.put("message", "There's an existing article with the same title. Kindly update and try again");
            return response;
        }

        //create a new post
        //compose a new URL that's SEO friendly from the URL
        if(StringUtils.isEmpty(postBean.getUrl())) {
            postBean.setUrl(postBean.getTitle().replace(" ", "-").toLowerCase());
        }

        Post post = new Post();
        post.setPost(postBean.getPost());
        post.setUrl(postBean.getUrl());
        post.setTitle(postBean.getTitle());
        post.setTags(postBean.getTags());
        post.setAuthor(user);
        postRepository.save(post);

        response.put("status", true);
        response.put("message", "New Post Created Successfully");
        return response;
    }


    /**
     * This will update the post object
     * @param postId
     * @param updatedPost
     * @param user
     * @return
     */
    @Transactional
    public Map<String, Object> updatePost(String postId, Post updatedPost, User user) {

        Map<String, Object> response = new HashMap<>();

        //try getting the post for the user
        Post post = postRepository.findByIdStringAndAuthor(postId, user);
        if(Objects.isNull(post)) {
            response.put("status", false);
            response.put("message", "Sorry we can't find that article");
            return response;
        }

        if(!StringUtils.isEmpty(updatedPost.getTitle())) {
            //check for existing post with same title
            Post checkPost = postRepository.findByTitleAndIdStringNot(updatedPost.getTitle(), postId);
            if(!Objects.isNull(checkPost)) {
                response.put("status", false);
                response.put("message", "There's an existing article with the same title. Kindly update and try again");
                return response;
            }
            post.setTitle(updatedPost.getTitle());
        }

        if(!StringUtils.isEmpty(updatedPost.getPost()))
            post.setPost(updatedPost.getPost());

        if(!StringUtils.isEmpty(updatedPost.getTags()))
            post.setTags(updatedPost.getTags());

        if(!StringUtils.isEmpty(updatedPost.getUrl()))
            post.setUrl(updatedPost.getUrl());

        postRepository.save(post);

        response.put("status", true);
        response.put("message", "Post Updated Successfully");
        return response;

    }


    /**
     * This will get all the posts available
     * @param limit
     * @param offset
     * @return Map
     */
    public Map<String, Object> allPosts(Optional<String> limit,  Optional<String > offset) {

        Map<String, Object> response = new HashMap<>();

        Query postsQuery = entityManager.createNativeQuery("SELECT p.id_string as id, p.title, p.post, p.url, p.tags, " +
                "p.created_at, CONCAT(u.first_name, ' ',u.last_name) as author FROM posts p " +
                "JOIN users u ON p.id_user = u.id")
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        int realLimit = Integer.parseInt(limit.orElse(Constants.MAX_RECORD_PER_PAGE + ""));
        int realOffset = 0;

        //the offset behaves as a page
        //i.e. offset of 1 means send me page 1, 2 means send me page 2 and so on...
        if(Integer.parseInt(offset.orElse("0")) > 0) {
            realOffset = Integer.parseInt(offset.orElse("0")) * realLimit;
        }

        postsQuery.setFirstResult(realOffset);
        postsQuery.setMaxResults(realLimit);
        List posts = postsQuery.getResultList();

        //run query to get total result
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(id) FROM posts");
        Long total = Long.parseLong(countQuery.getSingleResult().toString());

        response.put("status", true);
        response.put("posts", posts);
        Map<String, Object> meta = new HashMap<>();
        meta.put("total", total);
        meta.put("limit", realLimit);
        meta.put("offset", Integer.parseInt(offset.orElse("0")));
        response.put("meta", meta);
        return  response;
    }


    /**
     * This will get just the details of a post
     * using the ID or URL
     * @return Map
     */
    public Map<String, Object> postDetails(String postId) {

        Map<String, Object> response = new HashMap<>();

        Query postsQuery = entityManager.createNativeQuery("SELECT p.id_string as id, p.title, p.post, p.url, p.tags, " +
                "p.created_at, p.updated_at, CONCAT(u.first_name, ' ',u.last_name) as author FROM posts p " +
                "JOIN users u ON p.id_user = u.id" +
                " WHERE p.id_string = '" + postId + "' OR p.url = '" + postId + "'")
                .unwrap(org.hibernate.query.Query.class)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        List posts = postsQuery.getResultList();

        if(posts.isEmpty()) {
            response.put("status", false);
            response.put("error", "Post not found!");
            return response;
        }

        response.put("status", true);
        response.put("post", posts.get(0));
        return  response;
    }


    /**
     * This function will delete the post
     * that's created by the logged in user
     * @param postId
     * @param loggedInUser
     * @return Map
     */
    public Map<String, Object> deletePost(String postId, User loggedInUser) {

        Map<String, Object> response = new HashMap<>();
        Post post = postRepository.findByIdStringAndAuthor(postId, loggedInUser);
        if(Objects.isNull(post)) {
            response.put("status", false);
            response.put("message", "Post [" + postId + "] Not Found");
            return response;
        }

        postRepository.delete(post);
        response.put("status", true);
        response.put("message", "Post deleted successfully");
        return  response;
    }







}
