/*
 * Copyright (c) Seun Matt (smatt382@gmail.com) 2018
 * Created on 15/9/2018
 */

package com.smattnews.models;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by Seun Matt on 15-Sep-18
 */

@Entity
@Table(name = "auth_tokens")
public class AuthToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_string", unique = true, nullable = false)
    private String idString;

    private String token;

    @OneToOne
    @JoinColumn(name = "id_user")
    private User user;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "abolished_at")
    private LocalDateTime abolishedAt;

    private boolean abolished;

    public AuthToken() { }

    public AuthToken(User user, String token) {
        this.user = user;
        this.token = token;
        expiryDate = LocalDateTime.now().plusHours(1);
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
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

        if(Objects.isNull(expiryDate)) {
            expiryDate = LocalDateTime.now().plusHours(1);
        }

        //always a new one
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
