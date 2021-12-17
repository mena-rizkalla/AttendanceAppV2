package com.staugustine.dimitsattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.staugustine.dimitsattendance.Adapter.ClassListNewAdapter;
import com.staugustine.dimitsattendance.Adapter.GradeListAdapter;
import com.staugustine.dimitsattendance.common.Common;
import com.staugustine.dimitsattendance.model.Class_Names;
import com.staugustine.dimitsattendance.model.Grade_Names;
import com.staugustine.dimitsattendance.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    String gradeType;
    BottomAppBar bottomAppBar;
    FloatingActionButton fab_main;
    RecyclerView recyclerView;
    TextView sample,verify_btn;
    List<Grade_Names> gradeNamesList;

    GradeListAdapter gradeListNewAdapter;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        bottomAppBar = findViewById(R.id.bottomAppBar);
        verify_btn = findViewById(R.id.verify_btn);
        fab_main = findViewById(R.id.fab_main);
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, Insert_Grade_Activity.class);
                startActivity(intent);
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();




        sample = findViewById(R.id.classes_sample);
        recyclerView = findViewById(R.id.recyclerView_main);

        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        gradeNamesList = new ArrayList<>();
        gradeListNewAdapter = new GradeListAdapter(HomeActivity.this,gradeNamesList);
        recyclerView.setAdapter(gradeListNewAdapter);




        if (Common.currentUserType.equals("admin")) {
            verify_btn.setVisibility(View.VISIBLE);
            readGrade();
        }else {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("grade");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    gradeType= (String) snapshot.getValue();
                    readGradeUser(gradeType);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,UsersVerification.class);
                startActivity(intent);
            }
        });
    }

    private void readGradeUser(String gradeType) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Grade").child(gradeType);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gradeNamesList.clear();

                    Grade_Names gradeNames = snapshot.getValue(Grade_Names.class);
                    gradeNamesList.add(gradeNames);

                gradeListNewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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