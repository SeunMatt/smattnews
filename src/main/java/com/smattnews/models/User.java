/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 * Created on 15/9/2018
 */

package com.smattnews.models;

import com.smattnews.config.Constants;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_string", unique = true, nullable = false)
    private String idString;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable =  false)
    private String password;

    private int status;

    private String bio;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "abolished_at")
    private LocalDateTime abolishedAt;

    private boolean abolished;


    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password =  password;
        this.status = Constants.AccountStatus.ACCOUNT_STATUS_ACTIVE;
    }

    public int getId() {
        return id;
    }

    public String getIdString() {
        return idString;
    }

    public void setIdString(String idString) {
        this.idString = idString;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getAbolishedAt() {
        return abolishedAt;
    }

    public void setAbolishedAt(LocalDateTime abolishedAt) {
        this.abolishedAt = abolishedAt;
    }

    public boolean isAbolished() {
        return abolished;
    }

    public void setAbolished(boolean abolished) {
        this.abolished = abolished;
    }

    @PrePersist
    public void preSave() {

        if(createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if(StringUtils.isEmpty(idString)) {
            idString = RandomStringUtils.randomAlphanumeric(10).toUpperCase();
        }

        //always a new one
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User && ((User) obj).getEmail().equals(getEmail());
    }

    @Override
    public String toString() {
        return "User{" +
                "idString='" + idString + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", bio='" + bio + '\'' +
                ", abolished=" + abolished +
                '}';
    }
}
