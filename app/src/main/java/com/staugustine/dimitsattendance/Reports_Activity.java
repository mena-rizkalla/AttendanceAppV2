package com.staugustine.dimitsattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.staugustine.dimitsattendance.Adapter.ReportsNewAdapter;
import com.staugustine.dimitsattendance.model.Attendance_Reports;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.ss.formula.functions.T;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.realm.Realm;

public class Reports_Activity extends AppCompatActivity {

    String subjectName, className, room_ID;
    Button select_from,select_to,getReports;
    TextView txt_select_from,txt_select_to;
    RecyclerView recyclerView;
    private int year, month, day, yearTo, monthTo, dayTo;


    List<Attendance_Reports> attendance_reports;
    ReportsNewAdapter reportsNewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        Realm.init(this);
        subjectName = getIntent().getStringExtra("subject_name");
        className = getIntent().getStringExtra("class_name");
        room_ID = getIntent().getStringExtra("room_ID");
        recyclerView = findViewById(R.id.recyclerView_reports);

        select_from = findViewById(R.id.select_from);
        select_to = findViewById(R.id.select_to);
        txt_select_from = findViewById(R.id.txt_select_from);
        txt_select_to = findViewById(R.id.txt_select_to);
        getReports = findViewById(R.id.get_reports);

        Toolbar toolbar = findViewById(R.id.toolbar_reports);
        setSupportActionBar(toolbar);
        toolbar.setTitle(subjectName);
        toolbar.setSubtitle(className);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);



        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);

        recyclerView.setLayoutManager(gridLayoutManager);
        attendance_reports = new ArrayList<>();
        reportsNewAdapter = new ReportsNewAdapter(Reports_Activity.this,attendance_reports);
        recyclerView.setAdapter(reportsNewAdapter);

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

        getReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        readReports();
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
        attendance_reports.clear();
        FirebaseDatabase.getInstance().getReference("Attendance_Reports").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Attendance_Reports attendance_report = dataSnapshot.getValue(Attendance_Reports.class);
                    if (attendance_report.getClassId().equals(room_ID) && datesInRange.contains(attendance_report.getDate())){
                        attendance_reports.add(attendance_report);
                    }
                }
                reportsNewAdapter = new ReportsNewAdapter(Reports_Activity.this,attendance_reports);
                recyclerView.setAdapter(reportsNewAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void readReports() {
        attendance_reports.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Attendance_Reports");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Attendance_Reports attendance_report = dataSnapshot.getValue(Attendance_Reports.class);
                    if (attendance_report.getClassId().equals(room_ID)){
                        attendance_reports.add(attendance_report);
                    }
                }
                reportsNewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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