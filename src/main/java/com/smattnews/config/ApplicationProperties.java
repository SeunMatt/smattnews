/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 * Created on 15/9/2018
 */

package com.smattnews.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {

    private String appUrl;
    private String env;
    private String supportEmail;
    private String mailChimpListId;
    private String mailChimpApiKey;
    private String mailChimpApiRoot;
    private String appName;


    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String url) {
        this.appUrl = url;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getSupportEmail() {
        return supportEmail;
    }

    public void setSupportEmail(String supportEmail) {
        this.supportEmail = supportEmail;
    }

    public String getMailChimpListId() {
        return mailChimpListId;
    }

    public void setMailChimpListId(String mailChimpListId) {
        this.mailChimpListId = mailChimpListId;
    }

    public String getMailChimpApiKey() {
        return mailChimpApiKey;
    }

    public void setMailChimpApiKey(String mailChimpApiKey) {
        this.mailChimpApiKey = mailChimpApiKey;
    }

    public String getMailChimpApiRoot() {
        return mailChimpApiRoot;
    }

    public void setMailChimpApiRoot(String mailChimpApiRoot) {
        this.mailChimpApiRoot = mailChimpApiRoot;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        return "ApplicationProperties{" +
                "appUrl='" + appUrl + '\'' +
                ", env='" + env + '\'' +
                ", supportEmail='" + supportEmail + '\'' +
                ", mailChimpListId='" + mailChimpListId + '\'' +
                ", mailChimpApiKey='" + mailChimpApiKey + '\'' +
                ", mailChimpApiRoot='" + mailChimpApiRoot + '\'' +
                ", appName='" + appName + '\'' +
                '}';
    }
}
