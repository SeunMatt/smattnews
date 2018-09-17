/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 * Created on 15/9/2018
 */

package com.smattnews.daos;

import com.smattnews.models.AuthToken;
import com.smattnews.models.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Seun Matt on 15-Sep-18
 */
public interface AuthTokenRepository extends CrudRepository<AuthToken, Integer> {

    AuthToken findByUser(User user);
    AuthToken findByToken(String token);
}
