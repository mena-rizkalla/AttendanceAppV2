package com.staugustine.dimitsattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.staugustine.dimitsattendance.Adapter.SpecificAttendanceAdapter;
import com.staugustine.dimitsattendance.common.Common;
import com.staugustine.dimitsattendance.model.Attendance_Students_List;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class StudentProfile extends AppCompatActivity {
    // initialize the variables
    TextView StudentName,txt_id,total_days_off,txt_select_to,txt_select_from,get_reports;
    Button select_from,select_to;
    private int year, month, day, yearTo, monthTo, dayTo;

    // this variable is the initial days off of the student that we'll add their off days on
    private int INITIAL_DAYS_OFF = 0;

    // to get the data we passed here
    Bundle intent;

    RecyclerView recyclerView;
    List<Attendance_Students_List> attendance_students_lists;
    SpecificAttendanceAdapter specificAttendanceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        // declaring the variables and setting recycler view's layout manager and adapter
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        txt_select_from = findViewById(R.id.txt_select_from);
        txt_select_to = findViewById(R.id.txt_select_to);
        select_from = findViewById(R.id.select_from);
        select_to = findViewById(R.id.select_to);
        get_reports = findViewById(R.id.get_reports);


        // getting the data we passed from the StudentsAdapter
        intent = getIntent().getExtras();

        // always avoid the nullPointerExceptions
        if(intent != null){
            // declaring the views and assigning their values
            StudentName = findViewById(R.id.student_name);
            StudentName.setText(intent.getString("name"));
            txt_id = findViewById(R.id.student_id);
            txt_id.setText(intent.getString("id"));
            total_days_off = findViewById(R.id.total_days_off);
        }else {
            // we hope this code never executes :)
            Toast.makeText(StudentProfile.this, "Something wrong", Toast.LENGTH_SHORT).show();
        }
        // download the attendance reports uploaded before
        downloadReport();
        select_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateFrom();
            }
        });

        select_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTo();
            }
        });

        get_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                INITIAL_DAYS_OFF = 0;
                // storing the dates in the format 20-10-2021 as a string
                String DATE_FROM = day + "-" + month + "-" + year;
                String DATE_TO = dayTo + "-" + monthTo + "-" + yearTo;

                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                Date start = null;
                try {
                    start = dateFormat.parse(DATE_FROM);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date end = null;
                try {
                    end = dateFormat.parse(DATE_TO);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //prepare a list of dates to get fulled later
                List datesInRange = new ArrayList<>();

                //eliminate unusable time intervals like hours,minutes...
                Calendar calendar = getCalendarWithoutTime(start);
                Calendar endCalendar = getCalendarWithoutTime(end);

                // get all the dates before the ending date
                while (calendar.before(endCalendar)) {

                    // prepare a variable to store in
                    Date result = calendar.getTime();
                    // reformat the date to be used later and add it to the list then add one day to the latter one
                    final String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(result);
                    datesInRange.add(date);
                    calendar.add(Calendar.DATE, 1);
                }
                // pass the list of selected dates to this method to read the reports
                readModifiedReports(datesInRange);
            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 1) {
            return new DatePickerDialog(this, myDateListenerFrom, year, month, day);
        }
        if (id == 2){
            return new DatePickerDialog(this,myDateListenerTo,year,month,day);
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public void setDateFrom() {
        showDialog(1);
    }

    @SuppressWarnings("deprecation")
    public void setDateTo() {
        showDialog(2);
    }

    private DatePickerDialog.OnDateSetListener myDateListenerFrom = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // store the selected dates in these variables
                    year = arg1;
                    month = arg2 + 1;
                    day = arg3;
                    txt_select_from.setText(day+"-"+month+"-"+year);
                }
            };

    private DatePickerDialog.OnDateSetListener myDateListenerTo = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // store the selected dates in these variables
                    yearTo = arg1;
                    monthTo = arg2 + 1;
                    dayTo = arg3;
                    txt_select_to.setText(dayTo+"-"+monthTo+"-"+yearTo);
                }
            };

    private void readModifiedReports(List datesInRange) {
        attendance_students_lists = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Classes").child(Common.currentClassName)
                .child("Attendance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        // iterator to iterate over each object under the reference
                        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                        while (iterator.hasNext()) {
                            // declaring the data and storing them in a StudentAttendanceModel object
                            DataSnapshot dataSnapshot1 = iterator.next();
                            Attendance_Students_List list = dataSnapshot1.getValue(Attendance_Students_List.class);

                            // spend all your life avoiding nullPointerExceptions
                            assert list != null;
                            // download only the days off for this student
                            if (list.getUnique_ID().equals(intent.getString("id"))
                                    && list.getAttendance().equals("Absent")
                                    && datesInRange.contains(list.getDate())) {

                                // add their value to 0 and setting the TextView of the days off
                                INITIAL_DAYS_OFF += 1;
                                total_days_off.setText(" " + INITIAL_DAYS_OFF);
                                attendance_students_lists.add(list);


                            }else{
                                total_days_off.setText(" " + INITIAL_DAYS_OFF);
                            }

                        }
                        specificAttendanceAdapter = new SpecificAttendanceAdapter(getApplicationContext(),attendance_students_lists);
                        recyclerView.setAdapter(specificAttendanceAdapter);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void downloadReport() {
        attendance_students_lists = new ArrayList<>();
        // refer to the Correct Reference
        FirebaseDatabase.getInstance().getReference("Classes").child(Common.currentClassName)
                .child("Attendance")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // you already guessed it :)
                        if (snapshot.exists()) {
                            // for each item under the reference "attendance" do the following :
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                // iterator to iterate over each object under the refernce
                                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                                while (iterator.hasNext()) {
                                    // declaring the data and storing them in a StudentAttendanceModel object
                                    DataSnapshot dataSnapshot1 = iterator.next();
                                    Attendance_Students_List list = dataSnapshot1.getValue(Attendance_Students_List.class);

                                    // spend all your life avoiding nullPointerExceptions
                                    assert list != null;
                                    // download only the days off for this student
                                    if (list.getUnique_ID().equals(intent.getString("id"))
                                            && list.getAttendance().equals("Absent")) {

                                        // add their value to 0 and setting the TextView of the days off
                                        INITIAL_DAYS_OFF += 1;
                                        total_days_off.setText(" " + INITIAL_DAYS_OFF);
                                        attendance_students_lists.add(list);


                                    }

                                }
                                specificAttendanceAdapter = new SpecificAttendanceAdapter(getApplicationContext(),attendance_students_lists);
                                recyclerView.setAdapter(specificAttendanceAdapter);
                            }
                        } else {
                            Toast.makeText(StudentProfile.this, "err", Toast.LENGTH_SHORT).show();
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(StudentProfile.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
    private static Calendar getCalendarWithoutTime(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

}