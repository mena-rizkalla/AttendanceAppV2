package com.avaantony.dimitsattendance.callback;

import com.avaantony.dimitsattendance.model.User;

import java.util.List;

public interface IUserVerificationCallbackListener {
    void onGradeLoadSuccess(List<User> users);
    void onGradeLoadFailed(String message);
}
