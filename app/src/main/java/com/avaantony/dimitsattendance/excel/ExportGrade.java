package com.avaantony.dimitsattendance.excel;


import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.avaantony.dimitsattendance.model.Class_Names;

import java.util.ArrayList;
import java.util.List;

public class ExportGrade {


    public static void export(String GradeName, String date) {
        try {

            FirebaseDatabase.getInstance().getReference("Classes")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") List<String> list = new ArrayList<>();
                            List<String> list1 = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                Class_Names class_names = dataSnapshot.getValue(Class_Names.class);
                                assert class_names != null;
                                if (class_names.getGradeType().equals(GradeName)) {
                                    list.add(class_names.getGradeType());
                                    list1.add(class_names.getName_class() + class_names.getName_subject());
                                }


                            }

                            for (int l = 0; l < list1.size(); l++) {
                                String className = list1.get(l);
                                ExcelExporter.export(date, className, "");
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
