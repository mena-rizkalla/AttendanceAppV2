package com.staugustine.dimitsattendance;

import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.staugustine.dimitsattendance.Adapter.SpecificAttendanceAdapter;
import com.staugustine.dimitsattendance.common.Common;
import com.staugustine.dimitsattendance.model.Attendance_Students_List;
import com.staugustine.dimitsattendance.model.Class_Names;
import com.staugustine.dimitsattendance.model.Students_List;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.biff.WritableRecordData;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelExporter {

    static Context context;




    public static void export(){
        File sd = Environment.getExternalStorageDirectory();
        String excelFile = "yourFile.xls";

        File directory = new File(sd.getAbsolutePath());

        //create directory if not exist
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }

        try{

            //file path
            File file = new File(directory, excelFile);
            WorkbookSettings workbookSettings = new WorkbookSettings();
            workbookSettings.setLocale(new Locale(Locale.ENGLISH.getLanguage(),Locale.ENGLISH.getCountry()));

            WritableWorkbook workbook;


            workbook = Workbook.createWorkbook(file, workbookSettings);

            WritableSheet sheetA = workbook.createSheet("SheetA",0);


            sheetA.addCell(new Label(0, 0, "sheet A 1"));
            sheetA.addCell(new Label(1, 0, "sheet A 2"));
            sheetA.addCell(new Label(0, 1, "sheet A 3"));
            sheetA.addCell(new Label(0, 5, "sheet A3333"));


            WritableSheet sheetB = workbook.createSheet("sheet B", 1);


//            // column and row titles
//            sheetB.addCell(new Label(0, 0, "sheet B 1"));
//            sheetB.addCell(new Label(1, 0, "sheet B 2"));
//            sheetB.addCell(new Label(0, 1, "sheet B 3"));
//            sheetB.addCell(new Label(1, 1, "sheet B 4"));




            FirebaseDatabase.getInstance().getReference("Classes").child(Common.currentClassName).child("Attendance").child("28-Nov-2021").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        ArrayList<String> list = new ArrayList<>();

                        for (int i = 0; i < snapshot.getChildrenCount(); i++) {

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                Attendance_Students_List class_names = dataSnapshot.getValue(Attendance_Students_List.class);
                                list.add(class_names.getStudentName());



                                try {
                                    sheetB.addCell(new Label(0,i,list.get(i)));
                                } catch (WriteException e) {
                                    e.printStackTrace();
                                }




                            }

                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            workbook.write();
            workbook.close();







        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
