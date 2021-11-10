package com.ajstudios.easyattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ajstudios.easyattendance.Adapter.ReportsNewAdapter;
import com.ajstudios.easyattendance.model.Attendance_Reports;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;

public class Reports_Activity extends AppCompatActivity {

    String subjectName, className, room_ID;
    RecyclerView recyclerView;
    Realm realm;


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

        readReports();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.only_dot, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}