package com.ajstudios.easyattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ajstudios.easyattendance.Adapter.ReportsNewAdapter;
import com.ajstudios.easyattendance.Adapter.Reports_Detail_Adapter;
import com.ajstudios.easyattendance.Adapter.Reports_Detail_NewAdapter;
import com.ajstudios.easyattendance.realm.Attendance_Students_List;
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
import io.realm.Sort;

public class Reports_Detail_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    Reports_Detail_Adapter mAdapter;

    TextView subj, className, toolbar_title;

    Realm realm;

    Reports_Detail_NewAdapter reportsNewAdapter;
    List<com.ajstudios.easyattendance.model.Attendance_Students_List> attendance_students_lists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports__detail);
        Realm.init(this);
        realm = Realm.getDefaultInstance();

        String room_ID = getIntent().getStringExtra("ID");
        String classname = getIntent().getStringExtra("class");
        String subjName = getIntent().getStringExtra("subject");
        String date = getIntent().getStringExtra("date");

        Toolbar toolbar = findViewById(R.id.toolbar_reports_detail);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView_reports_detail);
        subj = findViewById(R.id.subjName_report_detail);
        className = findViewById(R.id.classname_report_detail);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(date);
        subj.setText(subjName);
        className.setText(classname);

        readReportsDetail(room_ID,classname,subjName,date);


        RealmResults<Attendance_Students_List> list = realm.where(Attendance_Students_List.class)
                            .equalTo("date_and_classID", room_ID)
                            .sort("studentName", Sort.ASCENDING)
                            .findAllAsync();


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        attendance_students_lists = new ArrayList<>();

        reportsNewAdapter = new Reports_Detail_NewAdapter(Reports_Detail_Activity.this,attendance_students_lists);

       // mAdapter = new Reports_Detail_Adapter( list,Reports_Detail_Activity.this, room_ID);
        recyclerView.setAdapter(reportsNewAdapter);

    }

    private void readReportsDetail(String room_ID, String classname, String subjName, String date) {
        //attendance_students_lists.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Classes").child(classname+subjName).child("Attendance").child(date);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    com.ajstudios.easyattendance.model.Attendance_Students_List attendanceStudentsList = dataSnapshot.getValue(com.ajstudios.easyattendance.model.Attendance_Students_List.class);
                        attendance_students_lists.add(attendanceStudentsList);

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