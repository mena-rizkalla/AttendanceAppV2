package com.staugustine.dimitsattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.staugustine.dimitsattendance.Adapter.SpecificAttendanceAdapter;
import com.staugustine.dimitsattendance.common.Common;
import com.staugustine.dimitsattendance.model.Attendance_Students_List;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StudentProfile extends AppCompatActivity {
    // initialize the variables
    TextView StudentName,txt_id,total_days_off;

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
                                recyclerView.setAdapter(specificAttendanceAdapter);
                                specificAttendanceAdapter = new SpecificAttendanceAdapter(getApplicationContext(),attendance_students_lists);
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

}