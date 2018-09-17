/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 * Created on 15/9/2018 
 */

package com.smattnews.service.admin.auth;

import com.smattnews.beans.SessionConfig;
import com.smattnews.config.Constants;
import com.smattnews.daos.AuthTokenRepository;
import com.smattnews.daos.UserRepository;
import com.smattnews.models.*;
import com.smattnews.models.stereotypes.UserRegistration;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.*;


@SuppressWarnings("JavaDoc")
@Service
public class AuthenticationService {

    private UserRepository userRepository;
    private Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private AuthenticationManager authManager;
    private Validator validator;
    private TransactionTemplate transactionTemplate;
    private SessionConfig sessionConfig;
    private AuthTokenRepository authTokenRepository;


    @Autowired
    public AuthenticationService(UserRepository userRepository, Validator validator,
                                 @Qualifier("usernamePasswordAuthManager") AuthenticationManager authenticationManager,
                                 TransactionTemplate transactionTemplate,
                                 SessionConfig sessionConfig, AuthTokenRepository authTokenRepository
    ) {
        this.userRepository = userRepository;
        this.authManager = authenticationManager;
        this.validator = validator;
        this.transactionTemplate = transactionTemplate;
        this.sessionConfig = sessionConfig;
        this.authTokenRepository = authTokenRepository;
    }


    /**
     * Performs the logic for new user registration
     * @param userRegistration
     * @return Map
     */
    @Transactional
    public Map<String, Object> doRegister(UserRegistration userRegistration) {

        Map<String, Object> response = new HashMap<>();

        logger.debug("reg info supplied: " + userRegistration.toString());

        //validation
        Set<ConstraintViolation<UserRegistration>> violations = validator.validate(userRegistration);
        if(!violations.isEmpty()) {
            Object [] violationArray = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .toArray();
            response.put("status", false);
            response.put("errors", violationArray);
           return response;
        }

        if(!userRegistration.passwordMatch()) {
            logger.debug("passwords do not match");
            response.put("status", false);
            response.put("error", "Passwords Do Not Match");
            return  response;
        }

        if(userRepository.findByEmail(userRegistration.getEmail()) != null) {
            logger.debug("email exists already");
            response.put("status", false);
            response.put("error", "This email [" + userRegistration.getEmail() + "] is not available");
            return  response;
        }

        //create the user object
        User user = userRegistration.createUserObject();
        logger.debug("User Object created : " + user);

        userRepository.save(user);
        logger.debug("Registration successful");

        response.put("status", true);
        response.put("message", "Registration Successful! Login with your credentials");
        return response;
    }


    /**
     * Performs the login logic using the specialized
     * authentication Manager
     * @param email
     * @param password
     * @return
     */
    public Map<String, Object> doLogin(String email, String password) {

        Map<String, Object> response = new HashMap<>();

        User user = userRepository.findByEmail(email);

        if(Objects.isNull(user)) {
            logger.debug("user not found in login: " + email);
            response.put("status", false);
            response.put("error", "Username/Password Does not Match");
            return response;
        }

        if(user.getStatus() == Constants.AccountStatus.ACCOUNT_STATUS_DEACTIVATED) {
            logger.debug("Account is deactivated for user: " + email);
            response.put("status", false);
            response.put("error", "This account is not active. Have you confirm your email address yet? " +
                    "<a href='/resend/email'>Resend Confirmation Link</a>");
            return response;
        }

        if(user.getStatus() == Constants.AccountStatus.ACCOUNT_STATUS_BANNED) {
            logger.debug("account banned: " + email);
            response.put("status", false);
            response.put("error", "You're not Permitted on this Platform. Contact Support for help");
            return response;
        }

        if(doAuth(email, password)) {

            AuthToken token = authTokenRepository.findByUser(user);

            if(Objects.isNull(token)) {
                //create a new token for the user and return it
                token = new AuthToken(user, RandomStringUtils.randomAlphanumeric(15));
            }
            else {
                //update the existing token expiry date
                token.setExpiryDate(LocalDateTime.now().plusHours(1));
                token.setToken(RandomStringUtils.randomAlphanumeric(15));
            }

            authTokenRepository.save(token);
            sessionConfig.setLoggedInUser(user);

            logger.debug("login successful");
            response.put("status", true);
            response.put("token", token.getToken());
            response.put("message", "Login Successful");
            return response;
        }

        logger.debug("login not successful");
        response.put("status", false);
        response.put("error", "Username/Password Do Not Match");
        return response;

    }


    /**
     * This function will do the actual authentication
     * and execute the logic
     * @param email
     * @param password
     * @return
     */
    private boolean doAuth(String email, String password) {

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, password);

        if(authManager == null) {
            logger.debug("authManager is null in doAuth");
            return false;
        }

        try {

            authToken = (UsernamePasswordAuthenticationToken) authManager.authenticate(authToken);
            logger.debug("auth Token : " + authToken.toString());
            logger.debug("auth Token isAuthenticated : " + authToken.isAuthenticated());

            if(authToken.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authToken);
                return true;
            }

            return false;

        }
        catch(BadCredentialsException e) {
            logger.debug("bad credentials exception " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        catch(Exception ex) {
            logger.error("Unexpected Exception occurred in doAuth while logging in " + ex.getLocalizedMessage());
            ex.printStackTrace();
            return false;
        }
    }

}
