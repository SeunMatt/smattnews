package com.smattnews.exceptions;


import com.smattnews.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by smatt on 10/04/2017
 */
@ControllerAdvice
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    private Logger logger = LoggerFactory.getLogger(MyAccessDeniedHandler.class);

    public MyAccessDeniedHandler() {}

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, org.springframework.security.access.AccessDeniedException e) throws IOException, ServletException {
        logger.debug("Access Denied Handler Called");
        e.printStackTrace();
        httpServletRequest.getSession().setAttribute(Constants.INTENDED_URI, httpServletRequest.getRequestURI());
        httpServletResponse.sendRedirect("error?code=403");
    }
}
