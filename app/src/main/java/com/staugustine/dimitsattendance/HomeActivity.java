package com.staugustine.dimitsattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.staugustine.dimitsattendance.Adapter.GradeListAdapter;
import com.staugustine.dimitsattendance.common.Common;
import com.staugustine.dimitsattendance.databinding.ActivityHomeBinding;
import com.staugustine.dimitsattendance.model.Grade_Names;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    String gradeType;
    BottomAppBar bottomAppBar;
    FloatingActionButton fab_main;
    RecyclerView recyclerView;
    TextView verify_btn;
    List<Grade_Names> gradeNamesList;
    GradeListAdapter gradeListNewAdapter;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomAppBar = binding.bottomAppBar;
        verify_btn = binding.verifyBtn;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fab_main = binding.fabMain;

        fab_main.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, Insert_Grade_Activity.class);
            startActivity(intent);
        });


        recyclerView = binding.recyclerViewMain;
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        gradeNamesList = new ArrayList<>();
        gradeListNewAdapter = new GradeListAdapter(HomeActivity.this, gradeNamesList);
        recyclerView.setAdapter(gradeListNewAdapter);


        if (Common.currentUserType.equals("admin")) {
            verify_btn.setVisibility(View.VISIBLE);
            readGradeForAdmin();
        } else {
            FirebaseDatabase.getInstance().getReference("Users")
                    .child(firebaseUser.getUid())
                    .child("grade")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            gradeType = Objects.requireNonNull(snapshot.getValue()).toString();
                            readGradeForUser(gradeType);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("HomeActivity", "onCancelled: " + error.getMessage());
                        }
                    });

        }
        verify_btn.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, UsersVerification.class);
            startActivity(intent);
        });
    }

    private void readGradeForUser(String gradeType) {
        FirebaseDatabase.getInstance().getReference("Grade")
                .child(gradeType)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            gradeNamesList.clear();
                            Grade_Names gradeNames = snapshot.getValue(Grade_Names.class);
                            gradeNamesList.add(gradeNames);
                            gradeListNewAdapter = new GradeListAdapter(HomeActivity.this, gradeNamesList);
                            recyclerView.setAdapter(gradeListNewAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("HomeActivity", "onCancelled: " + error.getMessage());
                        Toast.makeText(HomeActivity.this, "" + error.getMessage()
                                , Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void readGradeForAdmin() {
        FirebaseDatabase.getInstance().getReference("Grade")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            gradeNamesList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Grade_Names gradeNames = dataSnapshot.getValue(Grade_Names.class);
                                gradeNamesList.add(gradeNames);
                            }
                            gradeListNewAdapter = new GradeListAdapter(HomeActivity.this, gradeNamesList);
                            recyclerView.setAdapter(gradeListNewAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}