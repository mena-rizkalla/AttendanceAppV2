package com.staugustine.dimitsattendance;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.staugustine.dimitsattendance.common.Common;
import com.staugustine.dimitsattendance.model.Attendance_Students_List;
import com.staugustine.dimitsattendance.model.Class_Names;

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

public class ExportGrade {

    static Context context;
    static WritableSheet sheetA;
    static WritableSheet sheetB;
    static WritableWorkbook workbook;
    static int n=0;




    public static void export(String GradeName){
        File sd = Environment.getExternalStorageDirectory();
        String excelFile = GradeName+"studentData.xls";

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


            // column and row titles
//            sheetB.addCell(new Label(0, 0, "sheet B 1"));
//            sheetB.addCell(new Label(1, 0, "sheet B 2"));
//            sheetB.addCell(new Label(0, 1, "sheet B 3"));
//            sheetB.addCell(new Label(1, 1, "sheet B 4"));

            File file = new File(directory, excelFile);
            WorkbookSettings workbookSettings = new WorkbookSettings();
            workbookSettings.setLocale(new Locale(Locale.ENGLISH.getLanguage(),Locale.ENGLISH.getCountry()));

            try {
                workbook = Workbook.createWorkbook(file, workbookSettings);
         //       sheetA = workbook.createSheet("SheetA",0);

            } catch (IOException e) {
                e.printStackTrace();
            }





            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Classes");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<String> list = new ArrayList<>();
                    List<String> list1 = new ArrayList<>();

                    //if (reference.child("gradeType").toString().equals(GradeName)){

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                            Class_Names class_names = dataSnapshot.getValue(Class_Names.class);
                            if(class_names.getGradeType().equals(GradeName)){
                                list.add(class_names.getGradeType());
                                list1.add(class_names.getName_class()+class_names.getName_subject());
                            }


                        }

                    for (int l = 0; l < list1.size(); l++) {
                        String className = list1.get(l);



                        ExcelExporter.export("05-Dec-2021",className,"");

//                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Classes").child(className).child("Attendance").child("05-Dec-2021");
//                        reference1.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                List<String> list2 = new ArrayList<>();
//                                List<String> list3 = new ArrayList<>();
//
//                                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                                    Attendance_Students_List attendance_students_list = dataSnapshot.getValue(Attendance_Students_List.class);
//                                    list2.add(attendance_students_list.getStudentName());
//                                    list3.add(attendance_students_list.getAttendance());
//
//                                }
//
//                                    try {
//                                        workbook = Workbook.createWorkbook(file, workbookSettings);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                    for (String s:list1) {
//                                        if(s.equals(list1.get(n))){
//                                            sheetB = workbook.createSheet("sheet B"+s,n);
//
//                                            for (int i = 0; i < list2.size(); i++) {
//
//                                                try {
//                                                    sheetB.addCell(new Label(0,i,list2.get(i)));
//                                                    sheetB.addCell(new Label(2,i,list3.get(i)));
//
//                                                } catch (WriteException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//
//
//                                        }
//
//                                    }
//                                n++;
//
//
//                                    try {
//                                        workbook.write();
//                                        workbook.close();
//                                    } catch (IOException | WriteException e) {
//                                        e.printStackTrace();
//                                    }
//
//
//
//
//
//
//
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });

                    }


//                    int i;
//                    for (i = 0; i < list.size(); i++) {
//                        try {
//                            sheetA.addCell(new Label(0,i,list.get(i)));
//                            sheetA.addCell(new Label(2,i,list1.get(i)));
//
//                        } catch (WriteException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//
//                    try {
//                        workbook.write();
//                        workbook.close();
//                    } catch (IOException | WriteException e) {
//                        e.printStackTrace();
//                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


//             workbook.write();
//            workbook.close();







        }catch (Exception e) {
            e.printStackTrace();
        }
    }


}
