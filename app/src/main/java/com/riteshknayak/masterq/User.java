package com.riteshknayak.masterq;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class User {
    FirebaseFirestore db;

    private String name, email, pass, uid;
    private ArrayList<String> unlockedCategories = new ArrayList<>();
    //completedTopics, startedCategories
    public  void  seDefault(){
        //TODO add function for default categories
        unlockedCategories.add("CmYfZdAGsDpA2Vupktb4");
        unlockedCategories.add("gzkAZfEGotIKDQxcXSFV");
        unlockedCategories.add("gzkAZfEGotIKDQxcXSFV");
    }

    public User() {
    }


    public User(String name, String email, String pass) {
        this.name = name;
        this.email = email;
        this.pass = pass;
    }

    public String getNameObject() {
        return name;
    }

    public void setNameObject(String name) {
        this.name = name;
    }

    public String getEmailObject() {
        return email;
    }

    public void setEmailObject(String email) {
        this.email = email;
    }

    public String getPassObject() {
        return pass;
    }

    public void setPassObject(String pass) {
        this.pass = pass;
    }

    public String getUidObject() {
        return uid;
    }

    public void setUidObject(String uid) {
        this.uid = uid;
    }

}