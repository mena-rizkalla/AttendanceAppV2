package com.staugustine.dimitsattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.staugustine.dimitsattendance.common.Common;
import com.staugustine.dimitsattendance.model.Grade_Names;

import java.util.ArrayList;
import java.util.List;

public class GradeDetailActivity extends AppCompatActivity {

    BottomAppBar bottomAppBar;
    FloatingActionButton fab_main;
    Button exportGrade;
    RecyclerView recyclerView;
    TextView sample;
    List<Grade_Names> gradeNamesList;

    String gradeName;
    String room_ID;

    GradeDetailAdapter gradeListNewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_detail);

        final String theme = getIntent().getStringExtra("theme");
        gradeName = getIntent().getStringExtra("gradeName");
        room_ID = getIntent().getStringExtra("graderoom_ID");

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
        gradeNamesList = new ArrayList<>();
        gradeListNewAdapter = new GradeDetailAdapter(GradeDetailActivity.this,gradeNamesList);
        recyclerView.setAdapter(gradeListNewAdapter);

        Toast.makeText(GradeDetailActivity.this,gradeName,Toast.LENGTH_SHORT).show();
        readGradeDetail();
        exportGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExportGrade.export(gradeName);
            }
        });
    }

    private void readGradeDetail() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Grade").child(gradeName).child("GradeDetail");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gradeNamesList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Grade_Names gradeNames = dataSnapshot.getValue(Grade_Names.class);
                    gradeNamesList.add(gradeNames);
                }
                gradeListNewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}