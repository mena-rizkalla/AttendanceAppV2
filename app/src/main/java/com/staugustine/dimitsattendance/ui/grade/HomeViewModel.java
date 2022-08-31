package com.staugustine.dimitsattendance.ui.grade;


import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.staugustine.dimitsattendance.callback.IGradeCallbackListener;
import com.staugustine.dimitsattendance.model.Grade_Names;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeViewModel extends ViewModel implements IGradeCallbackListener {
    private MutableLiveData<List<Grade_Names>> gradeNamesMutableLiveData;
    private IGradeCallbackListener listener;
    private MutableLiveData<String> messageError = new MutableLiveData<>();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    public HomeViewModel() {
        listener = this;
    }

    public MutableLiveData<List<Grade_Names>> getGradeNamesForAdmin() {
        if (gradeNamesMutableLiveData == null) {
            gradeNamesMutableLiveData = new MutableLiveData<>();
            readGradeForAdmin();
        }
        return gradeNamesMutableLiveData;
    }

    public MutableLiveData<List<Grade_Names>> getGradeNamesForUser() {
        if (gradeNamesMutableLiveData == null) {
            gradeNamesMutableLiveData = new MutableLiveData<>();
            getAvailableGrades();
        }
        return gradeNamesMutableLiveData;
    }

    private void readGradeForAdmin() {
        List<Grade_Names> gradeNamesList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Grade")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        gradeNamesList.clear();
                        if (snapshot.exists()) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Grade_Names gradeNames = dataSnapshot.getValue(Grade_Names.class);
                                gradeNamesList.add(gradeNames);
                            }
                            if (gradeNamesList.size() > 0) {
                                listener.onGradeLoadSuccess(gradeNamesList);
                            } else {
                                listener.onGradeLoadFailed("Empty");
                            }
                        } else {
                            listener.onGradeLoadFailed("List doesn't exist");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void readGradeForUser(String gradeType) {
        List<Grade_Names> gradeNamesList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Grade")
                .child(gradeType)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            gradeNamesList.clear();
                            Grade_Names gradeNames = snapshot.getValue(Grade_Names.class);
                            gradeNamesList.add(gradeNames);
                        }
                        gradeNamesMutableLiveData.setValue(gradeNamesList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("HomeActivity", "onCancelled: " + error.getMessage());
                    }
                });
    }

    private void getAvailableGrades() {
        FirebaseDatabase.getInstance().getReference("Users")
                .child(firebaseUser.getUid())
                .child("grade")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String gradeType = Objects.requireNonNull(snapshot.getValue()).toString();
                        readGradeForUser(gradeType);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("HomeActivity", "onCancelled: " + error.getMessage());
                    }
                });
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    @Override
    public void onGradeLoadSuccess(List<Grade_Names> grade_list) {
        gradeNamesMutableLiveData.setValue(grade_list);
    }

    @Override
    public void onGradeLoadFailed(String message) {
        messageError.setValue(message);
    }
}
