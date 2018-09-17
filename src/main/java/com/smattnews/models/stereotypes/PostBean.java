/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 * Created on 15/9/2018
 */

package com.smattnews.models.stereotypes;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * Created by Seun Matt on 15-Sep-18
 */
public class PostBean {

    private String id; //used to map idString during update action

    @NotEmpty(message = "Article's title is required")
    @Size(max = 250, message = "Article's title should'nt be longer than 250 chars")
    private String title;

    @NotEmpty(message = "Article body is required")
    private String post;

    @Size(max = 250, message = "Tags should be comma separated and must not be more than 250 chars")
    private String tags;

    @Size(max = 250, message = "URL should not be longer than 250 chars")
    private String url;

    public PostBean() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PostBean{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", post='" + post + '\'' +
                ", tags='" + tags + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
