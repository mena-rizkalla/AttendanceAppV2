package com.staugustine.dimitsattendance.callback;

import com.staugustine.dimitsattendance.model.User;

import java.util.List;

public interface IUserVerificationCallbackListener {
    void onGradeLoadSuccess(List<User> users);
    void onGradeLoadFailed(String message);
}
