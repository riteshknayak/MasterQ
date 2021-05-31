package com.riteshknayak.masterq.objects;

import com.google.firebase.firestore.FirebaseFirestore;

public class User {
    FirebaseFirestore db;

    private String name, email, pass, uid;
//    private ArrayList<String> unlockedCategories = new ArrayList<>();
    //completedTopics, startedCategories
//    public  void  seDefault(){
//        //TODO add function for default categories
//        unlockedCategories.add("CmYfZdAGsDpA2Vupktb4");
//        unlockedCategories.add("gzkAZfEGotIKDQxcXSFV");
//        unlockedCategories.add("gzkAZfEGotIKDQxcXSFV");
//    }

    public User() {
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

}