/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 * Created on 15/9/2018
 */

package com.smattnews.config;


public class Constants {

    public static final String INTENDED_URI = "intentPath"; //this is the path being visited before login was triggered
    public static final int MAX_RECORD_PER_PAGE = 100;
    public static final String AUTH_TOKEN_SEPARATOR = "___";
    public static final int TOKEN_LENGTH = 10;

    public class AccountStatus {
        public static final int ACCOUNT_STATUS_ACTIVE = 1;
        public static final int ACCOUNT_STATUS_BANNED = 2;
        public static final int ACCOUNT_STATUS_DEACTIVATED = 3;
    }

}
