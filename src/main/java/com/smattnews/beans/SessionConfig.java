/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 * Created on 15/9/2018
 */

package com.smattnews.beans;

import com.smattnews.models.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

/**
 * Created by Seun Matt on 22-Aug-18
 */

@SessionScope
@Component
public class SessionConfig {

    private User loggedInUser;

    public SessionConfig() { }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
