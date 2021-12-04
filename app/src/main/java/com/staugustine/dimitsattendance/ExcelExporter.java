package com.staugustine.dimitsattendance;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.staugustine.dimitsattendance.common.Common;
import com.staugustine.dimitsattendance.model.Attendance_Students_List;

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

    static Context context;
    static WritableSheet sheetA;
    static WritableWorkbook workbook;




    public static void export(String date, String classname){
        File sd = Environment.getExternalStorageDirectory();
        String excelFile = date+classname+"studentData.xls";

        File directory = new File(sd.getAbsolutePath());

        //create directory if not exist
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }

        try{

            //file path
            /**File file = new File(directory, excelFile);
            WorkbookSettings workbookSettings = new WorkbookSettings();
            workbookSettings.setLocale(new Locale(Locale.ENGLISH.getLanguage(),Locale.ENGLISH.getCountry()));

            WritableWorkbook workbook;


            workbook = Workbook.createWorkbook(file, workbookSettings);

            WritableSheet sheetA = workbook.createSheet("SheetA",0);


            sheetA.addCell(new Label(0, 0, "sheet A 1"));
            sheetA.addCell(new Label(1, 0, "sheet A 2"));
            sheetA.addCell(new Label(0, 1, "sheet A 3"));
            sheetA.addCell(new Label(0, 5, "sheet A3333"));


            WritableSheet sheetB = workbook.createSheet("sheet B", 1);**/


//            // column and row titles
//            sheetB.addCell(new Label(0, 0, "sheet B 1"));
//            sheetB.addCell(new Label(1, 0, "sheet B 2"));
//            sheetB.addCell(new Label(0, 1, "sheet B 3"));
//            sheetB.addCell(new Label(1, 1, "sheet B 4"));




            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Classes").child(Common.currentClassName).child("Attendance").child(date);
                    reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<String> list = new ArrayList<>();
                    List<String> list1 = new ArrayList<>();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Attendance_Students_List class_names = dataSnapshot.getValue(Attendance_Students_List.class);
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


           // workbook.write();
           // workbook.close();







        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
