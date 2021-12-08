package com.staugustine.dimitsattendance;

import static com.staugustine.dimitsattendance.ClassDetail_Activity.READ_EXST;
import static com.staugustine.dimitsattendance.ClassDetail_Activity.WRITE_EXST;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.staugustine.dimitsattendance.Adapter.Reports_Detail_NewAdapter;
import com.staugustine.dimitsattendance.common.Common;
import com.staugustine.dimitsattendance.model.Attendance_Students_List;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;

public class Reports_Detail_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    static final Integer WRITE_EXST = 0x3;
    static final Integer READ_EXST = 0x4;

    TextView subj, className, toolbar_title;
    private Button exportexcel;


    Reports_Detail_NewAdapter reportsNewAdapter;
    List<Attendance_Students_List> attendance_students_lists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports__detail);


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
        exportexcel = findViewById(R.id.exportexcel);

        readReportsDetail(room_ID,classname,subjName,date);



        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        attendance_students_lists = new ArrayList<>();

        reportsNewAdapter = new Reports_Detail_NewAdapter(Reports_Detail_Activity.this,attendance_students_lists);

        recyclerView.setAdapter(reportsNewAdapter);

        exportexcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST);
                askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXST);
                ExcelExporter.export(date,classname,subjName);

            }
        });

    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Reports_Detail_Activity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    Reports_Detail_Activity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(Reports_Detail_Activity.this,
                        new String[]{permission}, requestCode);

            } else {
                ActivityCompat.requestPermissions(Reports_Detail_Activity.this,
                        new String[]{permission}, requestCode);
            }
        } else {
//            Toast.makeText(this, permission + " is already granted.",
//                    Toast.LENGTH_SHORT).show();
        }
    }



    private void readReportsDetail(String room_ID, String classname, String subjName, String date) {
        //attendance_students_lists.clear();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Classes").child(classname+subjName).child("Attendance").child(date);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Attendance_Students_List attendanceStudentsList = dataSnapshot.getValue(Attendance_Students_List.class);
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