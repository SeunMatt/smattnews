/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 * Created on 15/9/2018
 */

package com.smattnews.models;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Seun Matt on 15-Sep-18
 */
@Entity
@Table(name = "posts")
@TypeDefs(
        @TypeDef(name = "TEXT", typeClass = String.class)
)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_string", unique = true, nullable = false)
    private String idString;


    @Type(type = "TEXT")
    @Column(name = "post", columnDefinition = "TEXT")
    private String post;

    private String title;

    private String url;

    @Type(type = "TEXT")
    @Column(name = "tags", columnDefinition = "TEXT")
    private String tags;

    @OneToOne
    @JoinColumn(name = "id_user")
    private User author;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "abolished_at")
    private LocalDateTime abolishedAt;

    private boolean abolished;

    public Post() { }

    public Post(String title, String post, String url, User author) {
        this.title = title;
        this.post = post;
        this.url = url;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getIdString() {
        return idString;
    }

    public void setIdString(String idString) {
        this.idString = idString;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getAbolishedAt() {
        return abolishedAt;
    }

    public void setAbolishedAt(LocalDateTime abolishedAt) {
        this.abolishedAt = abolishedAt;
    }

    public boolean isAbolished() {
        return abolished;
    }

    public void setAbolished(boolean abolished) {
        this.abolished = abolished;
    }

    @PrePersist
    public void preSave() {

        if(createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if(StringUtils.isEmpty(idString)) {
            idString = RandomStringUtils.randomAlphanumeric(10).toUpperCase();
        }

        //always a new one
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
