/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 * Created on 15/9/2018
 */

package com.smattnews.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Seun Matt on 15-Sep-18
 */
public class MySimpleUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        logger.debug("onAuthenticationFailure: " + exception.getMessage());
        exception.printStackTrace();

        if(exception instanceof BadCredentialsException) {
            //we're handling this our self
            logger.debug("Bad Credentials Exception Caught: " + exception.getMessage());
            response.sendRedirect("/error?code=401&message=" + exception.getMessage());
        }
        else {
            //execute the default behaviour
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
