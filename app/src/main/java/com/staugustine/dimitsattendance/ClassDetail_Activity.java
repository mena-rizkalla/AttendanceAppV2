package com.staugustine.dimitsattendance;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.staugustine.dimitsattendance.Adapter.StudentsListNewAdapter;
import com.staugustine.dimitsattendance.model.Attendance_Reports;
import com.staugustine.dimitsattendance.model.Attendance_Students_List;
import com.staugustine.dimitsattendance.model.Students_List;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class ClassDetail_Activity extends AppCompatActivity {

    private ImageView themeImage;
    private TextView className, total_students, place_holder;
    private CardView addStudent, reports_open;
    private Button submit_btn,edit_btn;
    private EditText student_name, reg_no, mobile_no;
    private LinearLayout layout_attendance_taken;
    private RecyclerView mRecyclerview;
    private String date;
    private List<Students_List> students_lists;
    private int count;
    private List<Attendance_Students_List> list_students1;


    String room_ID, subject_Name, class_Name;

    public static final String TAG = "ClassDetail_Activity";


    private Handler handler = new Handler();

    StudentsListNewAdapter adapter;

    ProgressBar progressBar;
    Dialog lovelyCustomDialog;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail_);

        getWindow().setExitTransition(null);

        final String theme = getIntent().getStringExtra("theme");
        class_Name = getIntent().getStringExtra("className");
        subject_Name = getIntent().getStringExtra("subjectName");
        room_ID = getIntent().getStringExtra("classroom_ID");


        Toolbar toolbar = findViewById(R.id.toolbar_class_detail);
        setSupportActionBar(toolbar);
        //Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_disease_detail);
        collapsingToolbarLayout.setTitle(subject_Name);

        edit_btn = findViewById(R.id.edit_attendance_btn);
        edit_btn.setVisibility(View.GONE);
        themeImage = findViewById(R.id.image_disease_detail);
        className = findViewById(R.id.classname_detail);
        total_students = findViewById(R.id.total_students_detail);
        layout_attendance_taken = findViewById(R.id.attendance_taken_layout);
        layout_attendance_taken.setVisibility(View.GONE);
        addStudent = findViewById(R.id.add_students);
        reports_open = findViewById(R.id.reports_open_btn);
        className.setText(class_Name);
        mRecyclerview = findViewById(R.id.recyclerView_detail);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        students_lists = new ArrayList<>();
        adapter = new StudentsListNewAdapter(ClassDetail_Activity.this,students_lists);
        mRecyclerview.setAdapter(adapter);
        progressBar = findViewById(R.id.progressbar_detail);
        place_holder = findViewById(R.id.placeholder_detail);
        place_holder.setVisibility(View.GONE);
        submit_btn = findViewById(R.id.submit_attendance_btn);
        submit_btn.setVisibility(View.GONE);

        readStudents();
        firebaseinit();

        switch (theme) {
            case "0":
                themeImage.setImageResource(R.drawable.asset_bg_paleblue);
                break;
            case "1":
                themeImage.setImageResource(R.drawable.asset_bg_green);

                break;
            case "2":
                themeImage.setImageResource(R.drawable.asset_bg_yellow);

                break;
            case "3":
                themeImage.setImageResource(R.drawable.asset_bg_palegreen);

                break;
            case "4":
                themeImage.setImageResource(R.drawable.asset_bg_paleorange);

                break;
            case "5":
                themeImage.setImageResource(R.drawable.asset_bg_white);
                break;

        }

        //---------------------------------

        Runnable r = new Runnable() {
            @Override
            public void run() {
                //readStudents();
                progressBar.setVisibility(View.GONE);
            }
        };
        handler.postDelayed(r, 500);

        //----------------------------------------

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**long count = realm.where(Students_List.class)
                        .equalTo("class_id", room_ID)
                        .count();
                 final String size, size2;
                final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ClassDetail_Activity.this);
                size = String.valueOf(preferences.getAll().size());
                Toast.makeText(getApplicationContext(),String.valueOf(preferences.getAll()),Toast.LENGTH_SHORT).show();
                size2 = String.valueOf(count);**/


                    submitAttendance();


            }
        });

        reports_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassDetail_Activity.this, Reports_Activity.class);
                intent.putExtra("class_name", class_Name);
                intent.putExtra("subject_name", subject_Name);
                intent.putExtra("room_ID", room_ID);
                startActivity(intent);
            }
        });



        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    LayoutInflater inflater = LayoutInflater.from(ClassDetail_Activity.this);
                    final View view1 = inflater.inflate(R.layout.popup_add_student, null);
                    student_name = view1.findViewById(R.id.name_student_popup);
                    reg_no = view1.findViewById(R.id.regNo_student_popup);
                    mobile_no = view1.findViewById(R.id.mobileNo_student_popup);

                    lovelyCustomDialog = new LovelyCustomDialog(ClassDetail_Activity.this)
                            .setView(view1)
                            .setTopColorRes(R.color.theme_light)
                            .setTitle("Add Student")
                            .setIcon(R.drawable.ic_baseline_person_add_24)
                            .setCancelable(false)
                            .setListener(R.id.add_btn_popup, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    String name = student_name.getText().toString();
                                    String regNo = reg_no.getText().toString();
                                    String mobNo = mobile_no.getText().toString();

                                    if (isValid()){
                                    addStudentMethod(name, regNo, mobNo);
                                    }else{
                                        Toast.makeText(ClassDetail_Activity.this, "Please fill all the details..", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            })
                            .setListener(R.id.cancel_btn_popup, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    lovelyCustomDialog.dismiss();
                                }
                            })
                            .show();

            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
                FirebaseDatabase.getInstance().getReference().child("Attendance_Reports").child(date+class_Name+subject_Name).removeValue();
                submit_btn.setVisibility(View.VISIBLE);
                edit_btn.setVisibility(View.INVISIBLE);
            }
        });

    }



    private void readStudents() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Classes").child(class_Name+subject_Name).child("Student_List");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                students_lists.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Students_List students_list = dataSnapshot.getValue(Students_List.class);
                    if (students_list.getClass_id().equals(class_Name+subject_Name)){
                        students_lists.add(students_list);
                    }


                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void firebaseinit() {
        final String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Classes").child(class_Name+subject_Name).child("Student_List");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 count = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Students_List students_list = dataSnapshot.getValue(Students_List.class);
                    if (students_list.getClass_id().equals(class_Name+subject_Name)){
                        count ++;
                    }
                }
                total_students.setText("Total Students : " + count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Attendance_Reports").child(date+class_Name+subject_Name);
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Attendance_Reports attendance_reports = snapshot.getValue(Attendance_Reports.class);
                    if (attendance_reports.getDate().equals(date)) {
                        layout_attendance_taken.setVisibility(View.VISIBLE);
                        submit_btn.setVisibility(View.GONE);
                        edit_btn.setVisibility(View.VISIBLE);
                    } else {
                        layout_attendance_taken.setVisibility(View.GONE);
                        submit_btn.setVisibility(View.VISIBLE);

                        if (!(count == 0)) {
                            submit_btn.setVisibility(View.VISIBLE);
                            place_holder.setVisibility(View.GONE);
                        }else if (count==0) {
                            submit_btn.setVisibility(View.GONE);
                            place_holder.setVisibility(View.VISIBLE);
                        }
                    }
                }else {
                    layout_attendance_taken.setVisibility(View.GONE);
                    submit_btn.setVisibility(View.VISIBLE);

                    if (!(count == 0)) {
                        submit_btn.setVisibility(View.VISIBLE);
                        place_holder.setVisibility(View.GONE);
                    }else if (count==0) {
                        submit_btn.setVisibility(View.GONE);
                        place_holder.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void submitAttendance(){

        final ProgressDialog progressDialog = new ProgressDialog(ClassDetail_Activity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
         date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());


                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                final String dateOnly = String.valueOf(calendar.get(Calendar.DATE));
                @SuppressLint("SimpleDateFormat")
                final String monthOnly = new SimpleDateFormat("MMM").format(calendar.getTime());

                try {


                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Attendance_Reports");
                    Attendance_Reports attendance_reports = new Attendance_Reports();
                    attendance_reports.setClassId(room_ID);
                    // see later
                   // attendance_reports.setAttendance_students_lists(list_students1);
                    attendance_reports.setDate(date);
                    attendance_reports.setDateOnly(dateOnly);
                    attendance_reports.setMonthOnly(monthOnly);
                    //attendance_reports.setDate_and_classID(date+room_ID);
                    attendance_reports.setClassname(class_Name);
                    attendance_reports.setSubjName(subject_Name);
                    reference.child(date+room_ID).setValue(attendance_reports);


                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.commit();
                    Toast.makeText(ClassDetail_Activity.this, "Attendance Submitted", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();


                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(ClassDetail_Activity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                }


    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        super.onDestroy();
    }

    public void addStudentMethod(final String studentName, final String regNo, final String mobileNo) {

        final ProgressDialog progressDialog = new ProgressDialog(ClassDetail_Activity.this);
        progressDialog.setMessage("Creating class..");
        progressDialog.show();


                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Classes").child(class_Name+subject_Name).child("Student_List");
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id",studentName+regNo);
                hashMap.put("name_student",studentName);
                hashMap.put("regNo_student",regNo);
                hashMap.put("mobileNo_student",mobileNo);
                hashMap.put("class_id",room_ID);
                reference.child(studentName+regNo).setValue(hashMap);
                progressDialog.dismiss();
                lovelyCustomDialog.dismiss();

    }

    public boolean isValid(){

        if (student_name.getText().toString().isEmpty() || reg_no.getText().toString().isEmpty() || mobile_no.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_class_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getTitle().equals("Delete Class"))
        {
            AlertDialog alertDialog = new AlertDialog.Builder(ClassDetail_Activity.this)
                    .setMessage("Are you sure you want to delete this class ")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DeleteClass();
                        }
                    })
                    .setNegativeButton("No",null)
                    .show();

        }

        return super.onOptionsItemSelected(item);
    }

    private void DeleteClass() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Classes").child(class_Name+subject_Name);
        reference.removeValue();
        /**DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Attendance_Reports").child(date+class_Name+subject_Name);
        reference1.removeValue();**/
        Intent intent = new Intent(ClassDetail_Activity.this,MainActivity.class);
        startActivity(intent);
        Toast.makeText(ClassDetail_Activity.this,"Class Deleted",Toast.LENGTH_SHORT).show();
    }


}