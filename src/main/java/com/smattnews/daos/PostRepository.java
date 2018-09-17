/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 * Created on 15/9/2018
 */

package com.smattnews.daos;

import com.smattnews.models.Post;
import com.smattnews.models.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Seun Matt on 15-Sep-18
 */
public interface PostRepository extends CrudRepository<Post, Integer> {
    Post findByIdStringAndAuthor(String idString, User author);
    Post findByIdString(String idString);
    Post findByTitle(String title);
    Post findByTitleAndIdStringNot(String title, String idString);
}
