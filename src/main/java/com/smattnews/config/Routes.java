/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 * Created on 15/9/2018
 */

package com.smattnews.config;

import org.springframework.context.annotation.Configuration;


@Configuration
public class Routes {

    public static final String HOME = "/";

    /**
     * This is the prefix for all secured apis related to the
     * admin part of the system
     */
    public static final String DASHBOARD_MOUTH_PART = "/app";


    public class Front {
        public static final String FRONT_NEWS = HOME + "news";
        public static final String FRONT_NEWS_DETAILS = HOME + "/news/{id}";

    }

    public class App {
        public static final String ADMIN_ADD_NEW_POST = DASHBOARD_MOUTH_PART + "/post";
        public static final String ADMIN_UPDATE_POST = DASHBOARD_MOUTH_PART + "/post/{id}";
        public static final String ADMIN_DELETE_POST = DASHBOARD_MOUTH_PART + "/post/{id}";
        public static final String ADMIN_LIST_USERS = DASHBOARD_MOUTH_PART + "/users";
    }

    public class Auth {
        public static final String LOGIN = "/login";
        public static final String REGISTER = "/register";
    }

    @Override
    public String toString() {
        return "This class holds all the routes for the application";
    }
}
