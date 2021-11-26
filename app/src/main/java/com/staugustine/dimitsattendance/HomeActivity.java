package com.staugustine.dimitsattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.staugustine.dimitsattendance.Adapter.ClassListNewAdapter;
import com.staugustine.dimitsattendance.Adapter.GradeListAdapter;
import com.staugustine.dimitsattendance.model.Class_Names;
import com.staugustine.dimitsattendance.model.Grade_Names;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    BottomAppBar bottomAppBar;
    FloatingActionButton fab_main;
    RecyclerView recyclerView;
    TextView sample;
    List<Grade_Names> gradeNamesList;

    GradeListAdapter gradeListNewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        bottomAppBar = findViewById(R.id.bottomAppBar);
        fab_main = findViewById(R.id.fab_main);
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, Insert_Grade_Activity.class);
                startActivity(intent);
            }
        });


        sample = findViewById(R.id.classes_sample);
        recyclerView = findViewById(R.id.recyclerView_main);

        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        gradeNamesList = new ArrayList<>();
        gradeListNewAdapter = new GradeListAdapter(HomeActivity.this,gradeNamesList);
        recyclerView.setAdapter(gradeListNewAdapter);


        readGrade();
    }

    private void readGrade() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Grade");
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