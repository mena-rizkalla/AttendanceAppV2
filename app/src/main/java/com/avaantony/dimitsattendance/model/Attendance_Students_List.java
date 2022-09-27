package com.avaantony.dimitsattendance.model;

public class Attendance_Students_List {
    String studentName;
    String studentRegNo;
    String attendance;
    String mobNo;
    String classID;
    String date;
    String unique_ID;

    public Attendance_Students_List() {
    }

    public Attendance_Students_List(String studentName, String studentRegNo, String attendance, String mobNo, String classID, String date, String unique_ID) {
        this.studentName = studentName;
        this.studentRegNo = studentRegNo;
        this.attendance = attendance;
        this.mobNo = mobNo;
        this.classID = classID;
        this.date = date;
        this.unique_ID = unique_ID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentRegNo() {
        return studentRegNo;
    }

    public void setStudentRegNo(String studentRegNo) {
        this.studentRegNo = studentRegNo;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUnique_ID() {
        return unique_ID;
    }

    public void setUnique_ID(String unique_ID) {
        this.unique_ID = unique_ID;
    }
}
