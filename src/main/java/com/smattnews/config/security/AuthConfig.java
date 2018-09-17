/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 * Created on 15/9/2018
 */

package com.smattnews.config.security;


import com.smattnews.config.ApplicationProperties;
import com.smattnews.config.Constants;
import com.smattnews.daos.UserRepository;
import com.smattnews.exceptions.MyAccessDeniedHandler;
import com.smattnews.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;


@SuppressWarnings("JavaDoc")
@EnableWebSecurity
@Configuration
public class AuthConfig extends WebSecurityConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(AuthConfig.class);
    private ApplicationProperties properties;
    private TokenAuthenticationProvider tokenAuthenticationProvider;
    private RequestMatcher protectedURLS;
    private RequestMatcher publicURLS;


    @Autowired
    public AuthConfig(ApplicationProperties properties, TokenAuthenticationProvider tokenAuthenticationProvider) {
        super();
        this.properties = properties;
        this.tokenAuthenticationProvider = tokenAuthenticationProvider;
        protectedURLS = new AntPathRequestMatcher("/app/**");
        publicURLS = new NegatedRequestMatcher(protectedURLS);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new MyAccessDeniedHandler();
    }

    /**
     * This has to be configured so that it won't
     * send a redirect - which is the default behaviour
     * @return SimpleUrlAuthenticationSuccessHandler
     */
    @Bean
    SimpleUrlAuthenticationSuccessHandler successHandler() {
        final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setRedirectStrategy((request, response, url) -> {
            //do nothing
        });
        return successHandler;
    }

    /**
     * We'll plugin our custom failure handler
     * so we can send actual messages other than generic ones
     * @return
     */
    @Bean
    SimpleUrlAuthenticationFailureHandler failureHandler() {
       return new MySimpleUrlAuthenticationFailureHandler();
    }


    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() throws Exception {
        TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter(protectedURLS);
        tokenAuthenticationFilter.setAuthenticationManager(authenticationManager());
        tokenAuthenticationFilter.setAuthenticationSuccessHandler(successHandler());
        tokenAuthenticationFilter.setAuthenticationFailureHandler(failureHandler());
        return tokenAuthenticationFilter;
    }

    @Bean
    @Primary
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /**
     * This particular authentication manager
     * will be used for username/password authentication
     * that'll result in the return of token
     * @param managerBuilder
     * @param userRepository
     * @return
     * @throws Exception
     */
    @Bean
    @Qualifier("usernamePasswordAuthManager")
    public AuthenticationManager usernamePasswordAuthenticationManager(AuthenticationManagerBuilder managerBuilder,  UserRepository userRepository) throws Exception {
        //set the user details service for this guy
        managerBuilder.userDetailsService((String email) -> {
            User user  = userRepository.findByEmail(email);
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                    user.getStatus() == Constants.AccountStatus.ACCOUNT_STATUS_ACTIVE,
                    user.getStatus() != Constants.AccountStatus.ACCOUNT_STATUS_DEACTIVATED,
                    true,
                    user.getStatus() != Constants.AccountStatus.ACCOUNT_STATUS_BANNED,
                    AuthorityUtils.createAuthorityList());
        }).passwordEncoder(new BCryptPasswordEncoder());
        return managerBuilder.build();
    }


    /**
     * This will configure the default authentication
     * provider to be the tokenAuthenticationProvider
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(tokenAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authenticationProvider(tokenAuthenticationProvider)
                    .addFilterBefore(tokenAuthenticationFilter(), AnonymousAuthenticationFilter.class)
                    .authorizeRequests()
                    .antMatchers("/app/**")
                    .authenticated()
//                .requestMatchers(protectedURLS)
                .and()
                    .exceptionHandling()
                    .authenticationEntryPoint((request, response, authException) -> {
                        request.getSession().setAttribute(Constants.INTENDED_URI, request.getRequestURI());
                        response.sendRedirect("/error?code=403");
                    })
                .and()
                    .csrf().disable()
                    .formLogin().disable()
                    .httpBasic().disable()
                    .logout().disable();

//            http
//                .authorizeRequests()
//                    .antMatchers("/app/**")
//                    .authenticated()
//                .and()
//                    .exceptionHandling()
//                        .authenticationEntryPoint((request, response, authException) -> {
//                            request.getSession().setAttribute(Constants.INTENDED_URI, request.getRequestURI());
//                            response.sendRedirect("/error?code=403");
//                        })
//                .and()
//                    .csrf()
//                        .disable();

    }

}
