package com.example.gromate.gromate.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;

    @SuppressWarnings("unused")
    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

}

