package com.avaantony.dimitsattendance.model;

public class Grade_Names {

    String id;
    String name_grade;
    String position_bg;

    public Grade_Names() {
    }

    public Grade_Names(String id, String name_class, String position_bg) {
        this.id = id;
        this.name_grade = name_class;
        this.position_bg = position_bg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName_grade() {
        return name_grade;
    }

    public void setName_grade(String name_grade) {
        this.name_grade = name_grade;
    }

    public String getPosition_bg() {
        return position_bg;
    }

    public void setPosition_bg(String position_bg) {
        this.position_bg = position_bg;
    }
}
