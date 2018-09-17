/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 * Created on 15/9/2018
 */

package com.smattnews.config.security;

import freemarker.template.utility.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Seun Matt on 15-Sep-18
 */
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public TokenAuthenticationFilter(RequestMatcher requestMatcher) {
        super(requestMatcher);
    }

    /**
     * This function will extract the token from the
     * header, it will then build a UsernamePasswordAuthenticationToken
     * that'll be passed to the AuthenticationProvider for getting the UserDetails object
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     * @throws AuthenticationException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {

        String token = httpServletRequest.getHeader("Authentication");
        logger.debug("token obtained from the header: " + token);

        if(StringUtils.isEmpty(token)) {
            throw new BadCredentialsException("Access Denied! Missing authentication header");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(token, token);
        return getAuthenticationManager().authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        //call the super function that'll update the SecurityContextHolder
        //with the authentication result
        super.successfulAuthentication(request, response, chain, authResult);

        //then do filter and continue normal process
        chain.doFilter(request, response);

    }

}
