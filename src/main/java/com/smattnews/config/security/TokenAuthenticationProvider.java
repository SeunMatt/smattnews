/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 * Created on 15/9/2018
 */

package com.smattnews.config.security;

import com.smattnews.beans.SessionConfig;
import com.smattnews.config.Constants;
import com.smattnews.daos.AuthTokenRepository;
import com.smattnews.models.AuthToken;
import com.smattnews.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by Seun Matt on 15-Sep-18
 */
@SuppressWarnings("JavaDoc")
@Component
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {


    private Logger logger = LoggerFactory.getLogger(getClass());
    private AuthTokenRepository authTokenRepository;
    private SessionConfig sessionConfig;

    @Autowired
    public TokenAuthenticationProvider(AuthTokenRepository authTokenRepository, SessionConfig sessionConfig) {
        super();
        this.authTokenRepository = authTokenRepository;
        this.sessionConfig = sessionConfig;
    }



    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
        //no additional check
    }

    /**
     * This will be called by the AuthenticationManager
     * to get the user details which if no exception is thrown simply
     * mean all is well
     * @param username
     * @param usernamePasswordAuthenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

        String token = (String) usernamePasswordAuthenticationToken.getCredentials();
        AuthToken authToken = authTokenRepository.findByToken(token);

        if(Objects.isNull(authToken)) {
            logger.debug("Invalid authentication token supplied: " + token);
            throw new BadCredentialsException("Invalid authentication token supplied!");
        }

        if(LocalDateTime.now().isAfter(authToken.getExpiryDate())) {
            throw new BadCredentialsException("Authentication token has expired! Login to get a fresh one");
        }

        User user = authToken.getUser();

        //let's keep this for use in other places before the end of this request
        sessionConfig.setLoggedInUser(user);

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.getStatus() == Constants.AccountStatus.ACCOUNT_STATUS_ACTIVE,
                user.getStatus() != Constants.AccountStatus.ACCOUNT_STATUS_DEACTIVATED,
                true,
                user.getStatus() != Constants.AccountStatus.ACCOUNT_STATUS_BANNED,
                AuthorityUtils.createAuthorityList());
    }

}
