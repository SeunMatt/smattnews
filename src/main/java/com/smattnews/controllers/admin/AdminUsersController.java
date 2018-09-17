/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 * Created on 15/9/2018
 */

package com.smattnews.controllers.admin;

import com.smattnews.config.Routes;
import com.smattnews.service.admin.users.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Seun Matt on 15-Sep-18
 */
@RestController
public class AdminUsersController {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private UsersService usersService;

    public AdminUsersController(UsersService usersService) {
        this.usersService = usersService;
    }


    @GetMapping(value = Routes.App.ADMIN_LIST_USERS, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity allUsers(HttpServletRequest request) {

        Optional<String> limit = Optional.ofNullable(request.getParameter("limit"));
        Optional<String> offset = Optional.ofNullable(request.getParameter("offset"));
        Map<String, Object> response = usersService.allUsers(limit, offset);

        if(Boolean.parseBoolean(response.get("status").toString())) {
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(response);
    }


}
