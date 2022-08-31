package com.staugustine.dimitsattendance.ui.grade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.staugustine.dimitsattendance.Adapter.GradeListAdapter;
import com.staugustine.dimitsattendance.Insert_Grade_Activity;
import com.staugustine.dimitsattendance.UsersVerification;
import com.staugustine.dimitsattendance.common.Common;
import com.staugustine.dimitsattendance.databinding.ActivityHomeBinding;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    String gradeType;
    BottomAppBar bottomAppBar;
    FloatingActionButton fab_main;
    RecyclerView recyclerView;
    TextView verify_btn;
    GradeListAdapter gradeListNewAdapter;
    FirebaseUser firebaseUser;
    private HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        bottomAppBar = binding.bottomAppBar;
        verify_btn = binding.verifyBtn;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fab_main = binding.fabMain;

        fab_main.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, Insert_Grade_Activity.class);
            startActivity(intent);
        });

        verify_btn.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, UsersVerification.class);
            startActivity(intent);
        });

        recyclerView = binding.recyclerViewMain;
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);


        if (Common.currentUserType.equals("admin")) {
            verify_btn.setVisibility(View.VISIBLE);
            homeViewModel.getGradeNamesForAdmin().observe(this,gradeNames ->{
                gradeListNewAdapter = new GradeListAdapter(HomeActivity.this, gradeNames);
                recyclerView.setAdapter(gradeListNewAdapter);
            });
        } else {
            homeViewModel.getGradeNamesForUser().observe(this,gradeNames ->{
                gradeListNewAdapter = new GradeListAdapter(HomeActivity.this, gradeNames);
                recyclerView.setAdapter(gradeListNewAdapter);
            });
        }
    }





}