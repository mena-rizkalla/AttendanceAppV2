package com.avaantony.dimitsattendance.model;

public class User {
    String id,fullname,username,imageurl,active,grade;

    public User(String id, String fullname, String username, String imageurl, String active) {
        this.id = id;
        this.fullname = fullname;
        this.username = username;
        this.imageurl = imageurl;
        this.active = active;
    }

    public User(String id, String fullname, String username, String imageurl, String active, String grade) {
        this.id = id;
        this.fullname = fullname;
        this.username = username;
        this.imageurl = imageurl;
        this.active = active;
        this.grade = grade;
    }

    public User() {
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
