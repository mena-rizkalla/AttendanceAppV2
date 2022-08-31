package com.staugustine.dimitsattendance.ui.main;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.staugustine.dimitsattendance.Adapter.ClassListNewAdapter;
import com.staugustine.dimitsattendance.Insert_class_Activity;
import com.staugustine.dimitsattendance.databinding.ActivityMainBinding;
import com.staugustine.dimitsattendance.model.Class_Names;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String room_ID;
    BottomAppBar bottomAppBar;
    FloatingActionButton fab_main;
    RecyclerView recyclerView;
    List<Class_Names> classNamesList;
    private MainViewModel mainViewModel;
    ClassListNewAdapter classListNewAdapter;
    ActivityMainBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setEnterTransition(null);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        room_ID = getIntent().getStringExtra("gradeDetailroom_ID");

        bottomAppBar = binding.bottomAppBar;
        fab_main = binding.fabMain;
        fab_main.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Insert_class_Activity.class);
            intent.putExtra("gradeDetailName", room_ID);
            startActivity(intent);
        });


        recyclerView = binding.recyclerViewMain;
        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        classNamesList = new ArrayList<>();
        classListNewAdapter = new ClassListNewAdapter(MainActivity.this, classNamesList);
        recyclerView.setAdapter(classListNewAdapter);


        mainViewModel.getListMutableLiveData(room_ID).observe(this, class_names -> {
            classListNewAdapter = new ClassListNewAdapter(MainActivity.this, class_names);
            recyclerView.setAdapter(classListNewAdapter);
        });

    }
}
