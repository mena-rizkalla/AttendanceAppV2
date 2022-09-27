package com.avaantony.dimitsattendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.avaantony.dimitsattendance.common.Common;

import java.util.HashMap;
import java.util.Objects;

public class Insert_class_Activity extends AppCompatActivity {

    String gradeDetailroomid,gradeType;
    Button create_button;
    EditText _className;
    EditText _subjectName;

    private  String position_bg = "0";

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_class_);

        Toolbar toolbar = findViewById(R.id.toolbar_insert_class);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        gradeDetailroomid = getIntent().getStringExtra("gradeDetailName");
        gradeType= Common.currentGrade;

        create_button = findViewById(R.id.button_createClass);
        _className = findViewById(R.id.className_createClass);
        _subjectName = findViewById(R.id.subjectName_createClass);



//        final RadioRealButton button1 = (RadioRealButton) findViewById(R.id.button1);
//        final RadioRealButton button2 = (RadioRealButton) findViewById(R.id.button2);
//        final RadioRealButton button3 = (RadioRealButton) findViewById(R.id.button3);
//        final RadioRealButton button4 = (RadioRealButton) findViewById(R.id.button4);
//        final RadioRealButton button5 = (RadioRealButton) findViewById(R.id.button5);
//        final RadioRealButton button6 = (RadioRealButton) findViewById(R.id.button6);

//        RadioRealButtonGroup group = (RadioRealButtonGroup) findViewById(R.id.group);
//        group.setOnClickedButtonPosition(new RadioRealButtonGroup.OnClickedButtonPosition() {
//            @Override
//            public void onClickedButtonPosition(int position) {
//                position_bg = String.valueOf(position);
//            }
//        });

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValid()) {

                    final ProgressDialog progressDialog = new ProgressDialog(Insert_class_Activity.this);
                    progressDialog.setMessage("Creating class..");
                    progressDialog.show();

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Classes");
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("specificId",gradeDetailroomid);
                    hashMap.put("id",_className.getText().toString() + _subjectName.getText().toString());
                    hashMap.put("name_class",_className.getText().toString());
                    hashMap.put("name_subject",_subjectName.getText().toString());
                    hashMap.put("position_bg",position_bg);
                    hashMap.put("gradeType",gradeType);
                    reference.child(_className.getText().toString() + _subjectName.getText().toString()).setValue(hashMap);

                    progressDialog.dismiss();
                    Toast.makeText(Insert_class_Activity.this, "Successfully created", Toast.LENGTH_SHORT).show();
                    finish();

                }else{
                    Toast.makeText(Insert_class_Activity.this, "Fill all details", Toast.LENGTH_SHORT).show();
                }

                //-------

            }
        });


    }

    public boolean isValid(){

        return !_className.getText().toString().isEmpty() && !_subjectName.getText().toString().isEmpty();
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