package com.staugustine.dimitsattendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class InsertGradeDetailActivity extends AppCompatActivity {
    Button create_button;
    EditText _gradeName;
    String gradeName;
    String room_ID;

    private  String position_bg = "2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_grade_detail);


        Toolbar toolbar = findViewById(R.id.toolbar_insert_class);
        setSupportActionBar(toolbar);
        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        final String theme = getIntent().getStringExtra("theme");
        gradeName = getIntent().getStringExtra("gradeName");
        room_ID = getIntent().getStringExtra("graderoom_ID");

        create_button = findViewById(R.id.button_createClass);
        _gradeName = findViewById(R.id.grade_createClass);

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValid()) {

                    final ProgressDialog progressDialog = new ProgressDialog(InsertGradeDetailActivity.this);
                    progressDialog.setMessage("Creating grade..");
                    progressDialog.show();

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Grade");
                    String id = reference.push().getKey();
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("id",id);
                    hashMap.put("name_grade",_gradeName.getText().toString());
                    hashMap.put("position_bg",position_bg);
                    reference.child(gradeName).child("GradeDetail").child(_gradeName.getText().toString()).setValue(hashMap);

                    progressDialog.dismiss();
                    Toast.makeText(InsertGradeDetailActivity.this, "Successfully created", Toast.LENGTH_SHORT).show();
                    finish();



                }else{
                    Toast.makeText(InsertGradeDetailActivity.this, "Fill all details", Toast.LENGTH_SHORT).show();
                }

                //-------

            }
        });
    }

    public boolean isValid(){

        return  !_gradeName.getText().toString().isEmpty();
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