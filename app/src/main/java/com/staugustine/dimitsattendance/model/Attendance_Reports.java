package com.staugustine.dimitsattendance.model;

import java.util.List;

public class Attendance_Reports {

    String date;
    String monthOnly;
    String dateOnly;
    String classId;
    String date_and_classID;
    String classname;
    String subjName;
    List<com.staugustine.dimitsattendance.model.Attendance_Students_List> attendance_students_lists;

    public Attendance_Reports() {
    }

    public Attendance_Reports(String date, String monthOnly, String dateOnly, String classId, String date_and_classID, String classname, String subjName, List<Attendance_Students_List> attendance_students_lists) {
        this.date = date;
        this.monthOnly = monthOnly;
        this.dateOnly = dateOnly;
        this.classId = classId;
        this.date_and_classID = date_and_classID;
        this.classname = classname;
        this.subjName = subjName;
        this.attendance_students_lists = attendance_students_lists;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonthOnly() {
        return monthOnly;
    }

    public void setMonthOnly(String monthOnly) {
        this.monthOnly = monthOnly;
    }

    public String getDateOnly() {
        return dateOnly;
    }

    public void setDateOnly(String dateOnly) {
        this.dateOnly = dateOnly;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getDate_and_classID() {
        return date_and_classID;
    }

    public void setDate_and_classID(String date_and_classID) {
        this.date_and_classID = date_and_classID;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getSubjName() {
        return subjName;
    }

    public void setSubjName(String subjName) {
        this.subjName = subjName;
    }

    public List<Attendance_Students_List> getAttendance_students_lists() {
        return attendance_students_lists;
    }

    public void setAttendance_students_lists(List<Attendance_Students_List> attendance_students_lists) {
        this.attendance_students_lists = attendance_students_lists;
    }
}