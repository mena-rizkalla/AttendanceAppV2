package com.ajstudios.easyattendance;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ajstudios.easyattendance.Adapter.ClassListAdapter;
import com.ajstudios.easyattendance.Adapter.ClassListNewAdapter;
import com.ajstudios.easyattendance.realm.Attendance_Students_List;
import com.ajstudios.easyattendance.realm.Class_Names;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.RealmClass;

public class MainActivity extends AppCompatActivity {

    BottomAppBar bottomAppBar;
    FloatingActionButton fab_main;
    RecyclerView recyclerView;
    TextView sample;
    List<com.ajstudios.easyattendance.model.Class_Names> classNamesList;

    ClassListAdapter mAdapter;
    ClassListNewAdapter classListNewAdapter;

    Realm realm;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);

        getWindow().setEnterTransition(null);

        bottomAppBar = findViewById(R.id.bottomAppBar);
        fab_main = findViewById(R.id.fab_main);
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Insert_class_Activity.class);
                startActivity(intent);
            }
        });

        realm = Realm.getDefaultInstance();

        RealmResults<Class_Names> results;

        results = realm.where(Class_Names.class)
                .findAll();


        sample = findViewById(R.id.classes_sample);
        recyclerView = findViewById(R.id.recyclerView_main);

        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        classNamesList = new ArrayList<>();
        classListNewAdapter = new ClassListNewAdapter(MainActivity.this,classNamesList);
       // mAdapter = new ClassListAdapter(results, MainActivity.this);
        recyclerView.setAdapter(classListNewAdapter);


        readClasses();


    }

    private void readClasses() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Class_Names");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classNamesList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    com.ajstudios.easyattendance.model.Class_Names classNames = dataSnapshot.getValue(com.ajstudios.easyattendance.model.Class_Names.class);
                    classNamesList.add(classNames);
                }
                classListNewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        realm.refresh();
        realm.setAutoRefresh(true);
        super.onResume();
    }
}
