/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 * Created on 15/9/2018
 */

package com.smattnews.controllers.auth;

import com.smattnews.config.Routes;
import com.smattnews.models.stereotypes.UserRegistration;
import com.smattnews.service.admin.auth.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
public class AuthController {


    private Logger logger  = LoggerFactory.getLogger(AuthController.class);
    private AuthenticationService authenticationService;


    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping(value = {Routes.Auth.LOGIN}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
    consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity login(@RequestBody Map<String, Object> data) {

        Map<String, Object> response = new HashMap<>();

        //perform validation
        if(!data.containsKey("email") || !data.containsKey("password")) {
           response.put("status", false);
           response.put("error", "The email and password key is required");
           return ResponseEntity.badRequest().body(response);
        }

        response = authenticationService.doLogin(data.get("email").toString(), data.get("password").toString());

        logger.debug("response for login: " + response);

        if(Boolean.parseBoolean(response.get("status").toString())) {
           return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(response);

     }


    @PostMapping(value = Routes.Auth.REGISTER, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity register(@RequestBody UserRegistration userRegistration) {

        Map<String, Object> response = authenticationService.doRegister(userRegistration);
        logger.debug("response == " + response.toString());

        if(Boolean.parseBoolean(response.get("status").toString())) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(response);

    }
}
