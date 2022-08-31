package com.staugustine.dimitsattendance.ui.gradeDetail;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.staugustine.dimitsattendance.Adapter.GradeDetailAdapter;
import com.staugustine.dimitsattendance.ExportGrade;
import com.staugustine.dimitsattendance.InsertGradeDetailActivity;
import com.staugustine.dimitsattendance.R;
import com.staugustine.dimitsattendance.model.Grade_Names;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GradeDetailActivity extends AppCompatActivity {

    static final Integer WRITE_EXST = 0x3;
    static final Integer READ_EXST = 0x4;
    BottomAppBar bottomAppBar;
    FloatingActionButton fab_main;
    Button exportGrade;
    RecyclerView recyclerView;
    TextView sample;
    String gradeName;
    String room_ID;

    private int year, month, day;

    GradeDetailAdapter gradeListNewAdapter;
    GradeDetailViewModel gradeDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_detail);

        gradeDetailViewModel = ViewModelProviders.of(this).get(GradeDetailViewModel.class);
        final String theme = getIntent().getStringExtra("theme");
        gradeName = getIntent().getStringExtra("gradeName");
        room_ID = getIntent().getStringExtra("graderoom_ID");

        Calendar calendar= Calendar.getInstance();
        year = calendar.get(Calendar.YEAR)+1;

        exportGrade = findViewById(R.id.exportGrade);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        fab_main = findViewById(R.id.fab_main);
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GradeDetailActivity.this, InsertGradeDetailActivity.class);
                intent.putExtra("gradeName", gradeName);
                startActivity(intent);
            }
        });
        sample = findViewById(R.id.classes_sample);

        recyclerView = findViewById(R.id.recyclerView_main);
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        Toast.makeText(GradeDetailActivity.this,gradeName,Toast.LENGTH_SHORT).show();

        gradeDetailViewModel.getGradeDetailNames(gradeName).observe(this , gradeNames -> {
            gradeListNewAdapter = new GradeDetailAdapter(GradeDetailActivity.this,gradeNames);
            recyclerView.setAdapter(gradeListNewAdapter);

        });
        exportGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST);
                askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXST);
                selectDate();
            }
        });
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(GradeDetailActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    GradeDetailActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(GradeDetailActivity.this,
                        new String[]{permission}, requestCode);

            } else {
                ActivityCompat.requestPermissions(GradeDetailActivity.this,
                        new String[]{permission}, requestCode);
            }
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 1) {
            return new DatePickerDialog(this, myDateListenerFrom, year, month, day);
        }
        return null;
    }

    private void selectDate() {
        showDialog(1);
    }

    private DatePickerDialog.OnDateSetListener myDateListenerFrom = new
            DatePickerDialog.OnDateSetListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // store the selected dates in these variables
                    year = arg1 - 1900;
                    month = arg2 ;
                    day = arg3;
                    //String date = day + "-" + month + "-" + year;
                    Date date1 = new Date(year,month,day);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                    String date2 = dateFormat.format(date1);
                    ExportGrade.export(gradeName,date2);

                }
            };
}