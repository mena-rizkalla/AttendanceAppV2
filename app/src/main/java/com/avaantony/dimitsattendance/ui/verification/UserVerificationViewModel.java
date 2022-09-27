package com.avaantony.dimitsattendance.ui.verification;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.avaantony.dimitsattendance.callback.IUserVerificationCallbackListener;
import com.avaantony.dimitsattendance.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserVerificationViewModel extends ViewModel implements IUserVerificationCallbackListener {

    private MutableLiveData<List<User>> listMutableLiveData;
    private MutableLiveData<String> messageError = new MutableLiveData<>();
    IUserVerificationCallbackListener listener;

    public UserVerificationViewModel() {
        listener = this;
    }

    public MutableLiveData<List<User>> getListMutableLiveData() {

        if (listMutableLiveData == null) {
            listMutableLiveData = new MutableLiveData<>();
            readUsers();
        }
        return listMutableLiveData;
    }

    private void readUsers() {
        List<User> list = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);
                        list.add(user);
                    }
                }
                if (list.size() > 0) {
                    onGradeLoadSuccess(list);
                } else {
                    onGradeLoadFailed("FAIlED");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onGradeLoadFailed(error.getMessage());
            }
        });
    }

    @Override
    public void onGradeLoadSuccess(List<User> users) {
        listMutableLiveData.setValue(users);
    }

    @Override
    public void onGradeLoadFailed(String message) {
        messageError.setValue(message);
    }
}
