package com.avaantony.dimitsattendance.ui.gradeDetail;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.avaantony.dimitsattendance.callback.IGradeCallbackListener;
import com.avaantony.dimitsattendance.model.Grade_Names;

import java.util.ArrayList;
import java.util.List;

public class GradeDetailViewModel extends ViewModel implements IGradeCallbackListener{

    private MutableLiveData<List<Grade_Names>> gradeNamesMutableLiveData;
    private IGradeCallbackListener listener;
    private MutableLiveData<String> messageError = new MutableLiveData<>();

    public GradeDetailViewModel(){
        listener = this;
    }

    public MutableLiveData<List<Grade_Names>> getGradeDetailNames(String gradeName) {
        if (gradeNamesMutableLiveData == null) {
            gradeNamesMutableLiveData = new MutableLiveData<>();
            readGradeDetail(gradeName);
        }
        return gradeNamesMutableLiveData;
    }

    private void readGradeDetail(String gradeName) {
        List<Grade_Names> gradeNamesList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Grade").child(gradeName).child("GradeDetail");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gradeNamesList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Grade_Names gradeNames = dataSnapshot.getValue(Grade_Names.class);
                    gradeNamesList.add(gradeNames);
                } if (gradeNamesList.size() > 0) {
                    listener.onGradeLoadSuccess(gradeNamesList);
                } else {
                    listener.onGradeLoadFailed("Empty");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
