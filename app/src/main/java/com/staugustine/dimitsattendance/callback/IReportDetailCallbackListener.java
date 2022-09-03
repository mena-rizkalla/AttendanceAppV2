package com.staugustine.dimitsattendance.callback;

import com.staugustine.dimitsattendance.model.Attendance_Students_List;
import com.staugustine.dimitsattendance.model.Grade_Names;

import java.util.List;

public interface IReportDetailCallbackListener {
    void onReportDetailLoadSuccess(List<Attendance_Students_List> attendance_students_lists);
    void onReportDetailLoadFailed(String message);
}
