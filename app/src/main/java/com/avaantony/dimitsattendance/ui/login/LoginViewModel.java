package com.avaantony.dimitsattendance.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.avaantony.dimitsattendance.model.User;

import java.util.Objects;

public class LoginViewModel extends ViewModel {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    private MutableLiveData<Boolean> loginStatus;
    private MutableLiveData<String> messageError = new MutableLiveData<>();

    public MutableLiveData<Boolean> getStatus(String str_email, String str_password) {
        if (loginStatus == null || !Boolean.TRUE.equals(loginStatus.getValue())) {
            loginStatus = new MutableLiveData<>();
            getLoginStatus(str_email, str_password);
        }
        return loginStatus;
    }

    public MutableLiveData<String> getMessageError(String str_email, String str_password) {
        if (messageError == null) {
            messageError = new MutableLiveData<>();
            getLoginStatus(str_email, str_password);
        }
        return messageError;
    }

    private void getLoginStatus(String str_email, String str_password) {
        auth.signInWithEmailAndPassword(str_email, str_password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    User user = snapshot.getValue(User.class);
                                    assert user != null;
                                    if (user.getActive().equals("1")) {
                                        loginStatus.postValue(true);
                                    } else {
                                        loginStatus.setValue(false);
                                        messageError.setValue("BANNED");
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                loginStatus.setValue(false);
                                messageError.setValue(error.getMessage());
                            }
                        });
            } else {
                loginStatus.setValue(false);
                messageError.setValue("FAILED");
            }
        });
    }

}
