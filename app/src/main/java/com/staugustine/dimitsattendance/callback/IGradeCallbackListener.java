package com.staugustine.dimitsattendance.callback;

import com.staugustine.dimitsattendance.model.Grade_Names;

import java.util.List;

public interface IGradeCallbackListener {
    void onGradeLoadSuccess(List<Grade_Names> grade_list);
    void onGradeLoadFailed(String message);
}
