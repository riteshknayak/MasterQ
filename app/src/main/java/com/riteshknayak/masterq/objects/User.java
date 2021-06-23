package com.riteshknayak.masterq.objects;

import com.google.firebase.firestore.FirebaseFirestore;

public class User {
    FirebaseFirestore db;

    private String name, email, pass, uid, publicNAme,imageUrl;
    private Integer score;

    public User() {
    }

    public User(String email, String pass, String uid, String imageUrl, Integer score) {
        this.email = email;
        this.pass = pass;
        this.uid = uid;
        this.imageUrl = imageUrl;
        this.score = score;
    }

    public User(String name, String email, String pass) {
        this.name = name;
        this.email = email;
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmailObject(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}