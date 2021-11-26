package com.staugustine.dimitsattendance.model;

public class Class_Names {
    String id;

    String name_class;
    String name_subject;
    String position_bg;
    String specificId;

    public Class_Names() {
    }

    public Class_Names(String id, String name_class, String name_subject, String position_bg, String specificId) {
        this.id = id;
        this.name_class = name_class;
        this.name_subject = name_subject;
        this.position_bg = position_bg;
        this.specificId = specificId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_class() {
        return name_class;
    }

    public void setName_class(String name_class) {
        this.name_class = name_class;
    }

    public String getName_subject() {
        return name_subject;
    }

    public void setName_subject(String name_subject) {
        this.name_subject = name_subject;
    }

    public String getPosition_bg() {
        return position_bg;
    }

    public void setPosition_bg(String position_bg) {
        this.position_bg = position_bg;
    }

    public String getSpecificId() {
        return specificId;
    }

    public void setSpecificId(String specificId) {
        this.specificId = specificId;
    }
}
