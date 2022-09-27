package com.staugustine.dimitsattendance.ui.main;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.staugustine.dimitsattendance.callback.IMainActivityCallbackListener;
import com.staugustine.dimitsattendance.model.Class_Names;


import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel implements IMainActivityCallbackListener {
    private MutableLiveData<List<Class_Names>> listMutableLiveData;
    private MutableLiveData<String> messageError = new MutableLiveData<>();
    IMainActivityCallbackListener listener;

    public MainViewModel() {
        listener = this;
    }

    public MutableLiveData<List<Class_Names>> getListMutableLiveData(String room_ID) {
        if (listMutableLiveData == null) {
            listMutableLiveData = new MutableLiveData<>();
            readClasses(room_ID);
        }
        return listMutableLiveData;
    }

    private void readClasses(String room_ID) {
        List<Class_Names> classNamesList = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("Classes")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        classNamesList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Class_Names classNames = dataSnapshot.getValue(Class_Names.class);
                            if (classNames.getSpecificId().equals(room_ID)) {
                                classNamesList.add(classNames);
                            }
                        }
                        listener.onGradeLoadSuccess(classNamesList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onGradeLoadFailed(error.getMessage());
                    }
                });
    }

    @Override
    public void onGradeLoadSuccess(List<Class_Names> class_names) {
        listMutableLiveData.setValue(class_names);
    }

    @Override
    public void onGradeLoadFailed(String message) {
        messageError.setValue(message);
    }
}
