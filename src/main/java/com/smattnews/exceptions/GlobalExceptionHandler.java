

/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 * Created on 15/9/2018
 */

package com.smattnews.exceptions;


import freemarker.template.utility.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@ControllerAdvice
@Controller
public class GlobalExceptionHandler implements ErrorController {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String missingRequiredParamExceptionHandler(MissingServletRequestParameterException e) {
        logger.error("Missing Required Param Exception: " + e.getMessage());
        e.printStackTrace();
        return "redirect:/error?code=400&message=" + e.getMessage();
    }

    @ExceptionHandler(Exception.class)
    public String generalExceptionHandler(Exception e) {
        logger.error("An unexpected exception occurred: " + e.getMessage() + "\nLocalized Message: " + e.getLocalizedMessage());
        e.printStackTrace();
        return "redirect:/error";
    }

    @RequestMapping("error")
    @ResponseBody
    public ResponseEntity handleAjaxError(HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("status", false);

        int statusCode = getStatusCode(request);

        logger.debug("Status Code in XMLHttpRequest: " + statusCode);

        if(statusCode == HttpStatus.NOT_FOUND.value()) {
            response.put("error", "The resource you're looking for does not exist");
            response.put("code", HttpStatus.NOT_FOUND.value());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            response.put("error", "Oops! Something went wrong internally");
            response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        else if(statusCode == HttpStatus.FORBIDDEN.value()){
            response.put("error", "Access Denied! Your login may have expired");
            response.put("code", HttpStatus.FORBIDDEN.value());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
        else if(statusCode == HttpStatus.UNAUTHORIZED.value()) {
            String message = StringUtils.isEmpty(request.getParameter("message")) ? "Unauthorized Access! Please login again or supply authentication header" :
                    request.getParameter("message");
            response.put("error", message);
            response.put("code", HttpStatus.UNAUTHORIZED.value());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        else if(statusCode == HttpStatus.BAD_REQUEST.value()) {
            String message = StringUtils.isEmpty(request.getParameter("message")) ? "Invalid Request. Please supply the " +
                    "appropriate parameter" : request.getParameter("message");
            response.put("error", message);
            response.put("code", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        else {
            response.put("error", "Oops! Something went wrong internally");
            response.put("code", statusCode);
            return ResponseEntity.status(statusCode).body(response);
        }
    }


    @Override
    public String getErrorPath() {
        return "/error";
    }


    private int getStatusCode(HttpServletRequest request) {

        String code = request.getParameter("code");

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        logger.error("Exception Occurred [" + status + "]: " + request.getRequestURL());

        int statusCode = 500;

        if (status != null) {
            statusCode = Integer.valueOf(status.toString());
        }
        else if(!StringUtils.isEmpty(code)) {
            statusCode = Integer.valueOf(code);
        }

        return statusCode;
    }

}
