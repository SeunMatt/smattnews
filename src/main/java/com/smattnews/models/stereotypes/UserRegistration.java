/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 * Created on 15/9/2018
 */

package com.smattnews.models.stereotypes;

import com.smattnews.config.Constants;
import com.smattnews.models.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;



/**
 * This POJO will be used to model user input for registration
 */
public class UserRegistration {

    @NotEmpty(message = "The first name field is required")
    private String first_name;

    @NotEmpty(message = "The last name field is required")
    private String last_name;

    @NotEmpty(message = "The email field is required")
    private String email;

    @Size(max = 250, message = "Bio should be at most 250 chars long")
    private String bio;

    @NotEmpty(message = "Password field is required")
    @Size(min = 6, message = "Password must be a minimum of 6 chars in length")
    private String password;

    @NotEmpty(message = "Password Confirmation is required")
    private String password_confirmation;

    public UserRegistration() {}


    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_confirmation() {
        return password_confirmation;
    }

    public void setPassword_confirmation(String password_confirmation) {
        this.password_confirmation = password_confirmation;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean passwordMatch() {
        return StringUtils.equals(password, password_confirmation);
    }

    @Override
    public String toString() {
        return "UserRegistration{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    //this method will create the user object that will be saved in the database
    public User createUserObject() {
        User user = new User();
        user.setUsername(first_name.charAt(0) + last_name.charAt(0) + RandomStringUtils.randomNumeric(3));
        user.setEmail(getEmail());
        user.setFirstName(first_name);
        user.setLastName(last_name);
        user.setBio(bio);
        user.setStatus(Constants.AccountStatus.ACCOUNT_STATUS_ACTIVE);
        user.setPassword(new BCryptPasswordEncoder().encode(getPassword()));
        return user;
    }
}
