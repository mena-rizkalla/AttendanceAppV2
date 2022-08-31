package com.staugustine.dimitsattendance.ui.grade;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.staugustine.dimitsattendance.callback.IGradeCallbackListener;
import com.staugustine.dimitsattendance.model.Grade_Names;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel implements IGradeCallbackListener {
    private MutableLiveData<List<Grade_Names>> gradeNamesMutableLiveData ;
    private IGradeCallbackListener listener;
    private MutableLiveData<String> messageError = new MutableLiveData<>();

    public HomeViewModel(){
        listener = this;
    }

    public MutableLiveData<List<Grade_Names>> getGradeNamesForAdmin(){
        if (gradeNamesMutableLiveData == null){
            gradeNamesMutableLiveData = new MutableLiveData<>();
            readGradeForAdmin();
        }
        return gradeNamesMutableLiveData;
    }

    public MutableLiveData<List<Grade_Names>> getGradeNamesForUser(String gradeType){
        if (gradeNamesMutableLiveData == null){
            gradeNamesMutableLiveData = new MutableLiveData<>();
            readGradeForUser(gradeType);
        }
        return gradeNamesMutableLiveData;
    }

    private void readGradeForAdmin() {
        List<Grade_Names> gradeNamesList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Grade")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            //gradeNamesList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Grade_Names gradeNames = dataSnapshot.getValue(Grade_Names.class);
                                gradeNamesList.add(gradeNames);
                            }
                            if (gradeNamesList.size() >0){
                                listener.onGradeLoadSuccess(gradeNamesList);
                            }else {
                                listener.onGradeLoadFailed("Empty");
                            }
                        }else {
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
