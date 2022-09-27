package com.avaantony.dimitsattendance.excel;


import android.os.Environment;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.avaantony.dimitsattendance.model.Attendance_Students_List;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelExporter {

    static WritableSheet sheetA;
    static WritableWorkbook workbook;




    public static void export(String date, String classname,String subjecname){
        File sd = Environment.getExternalStorageDirectory();
        String excelFile = date+classname+"studentData.xls";

        File directory = new File(sd.getAbsolutePath());

        //create directory if not exist
        if (!directory.isDirectory()) {
            //noinspection ResultOfMethodCallIgnored
            directory.mkdirs();
        }

        try{


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Classes").child(classname+subjecname).child("Attendance").child(date);
                    reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<String> list = new ArrayList<>();
                    List<String> list1 = new ArrayList<>();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Attendance_Students_List class_names = dataSnapshot.getValue(Attendance_Students_List.class);
                        assert class_names != null;
                        list.add(class_names.getStudentName());
                        list1.add(class_names.getAttendance());

                    }

                    File file = new File(directory, excelFile);
                    WorkbookSettings workbookSettings = new WorkbookSettings();
                    workbookSettings.setLocale(new Locale(Locale.ENGLISH.getLanguage(),Locale.ENGLISH.getCountry()));

                    try {
                        workbook = Workbook.createWorkbook(file, workbookSettings);
                        sheetA = workbook.createSheet("SheetA",0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    int i;
                    for (i = 0; i < snapshot.getChildrenCount(); i++) {
                            try {
                                sheetA.addCell(new Label(0,i,list.get(i)));
                                sheetA.addCell(new Label(2,i,list1.get(i)));
                                sheetA.addCell(new Label(4,i,date));


                            } catch (WriteException e) {
                                e.printStackTrace();
                            }
                        }
                    try {
                        workbook.write();
                        workbook.close();
                    } catch (IOException | WriteException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
