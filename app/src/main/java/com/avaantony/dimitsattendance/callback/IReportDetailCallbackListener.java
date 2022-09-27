package com.avaantony.dimitsattendance.callback;

import com.avaantony.dimitsattendance.model.Attendance_Students_List;

import java.util.List;

public interface IReportDetailCallbackListener {
    void onReportDetailLoadSuccess(List<Attendance_Students_List> attendance_students_lists);
    void onReportDetailLoadFailed(String message);
}
