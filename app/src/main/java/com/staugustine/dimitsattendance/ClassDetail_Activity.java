package com.staugustine.dimitsattendance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.staugustine.dimitsattendance.common.Common;
import com.staugustine.dimitsattendance.databinding.ActivityClassDetailBinding;
import com.staugustine.dimitsattendance.model.Attendance_Reports;
import com.staugustine.dimitsattendance.model.Students_List;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.staugustine.dimitsattendance.ui.grade.HomeActivity;
import com.staugustine.dimitsattendance.ui.main.MainActivity;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ClassDetail_Activity extends AppCompatActivity {

    static final Integer WRITE_EXST = 0x3;
    static final Integer READ_EXST = 0x4;
    private TextView total_students;
    private TextView place_holder;
    private Button submit_btn;
    private Button edit_btn;
    private EditText student_name, reg_no, edt_search;
    private LinearLayout layout_attendance_taken;
    private RecyclerView mRecyclerview;
    private List<Students_List> students_lists;
    private int count;
    public static final int cellCount = 1;
    Context context;
    String room_ID, subject_Name, class_Name;
    private final Handler handler = new Handler();
    StudentsListNewAdapter adapter;
    ProgressBar progressBar;
    Dialog lovelyCustomDialog;
    ProgressDialog dialog;
    ActivityClassDetailBinding binding;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClassDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setExitTransition(null);

        final String theme = getIntent().getStringExtra("theme");
        class_Name = getIntent().getStringExtra("className");
        subject_Name = getIntent().getStringExtra("subjectName");
        room_ID = getIntent().getStringExtra("classroom_ID");


        Toolbar toolbar = binding.toolbarClassDetail;
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbarLayout = binding.collapsingDiseaseDetail;
        collapsingToolbarLayout.setTitle(subject_Name);

        edit_btn = binding.editAttendanceBtn;
        edit_btn.setVisibility(View.GONE);

        edt_search = binding.edtSearch;
        ImageView themeImage = binding.imageDiseaseDetail;

        TextView className = binding.classnameDetail;
        className.setText(class_Name);

        total_students = binding.totalStudentsDetail;

        layout_attendance_taken = binding.attendanceTakenLayout;
        layout_attendance_taken.setVisibility(View.GONE);

        CardView addStudent = binding.addStudents;
        CardView reports_open = binding.reportsOpenBtn;

        mRecyclerview = binding.recyclerViewDetail;
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        students_lists = new ArrayList<>();
        adapter = new StudentsListNewAdapter(ClassDetail_Activity.this, students_lists);
        mRecyclerview.setAdapter(adapter);

        progressBar = binding.progressbarDetail;
        place_holder = binding.placeholderDetail;
        place_holder.setVisibility(View.GONE);

        submit_btn = binding.submitAttendanceBtn;
        submit_btn.setVisibility(View.GONE);

        Button excel = binding.excel;
        readStudents();
        firebaseInit();

        switch (Objects.requireNonNull(theme)) {
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

        Runnable r = () -> {
            //readStudents();
            progressBar.setVisibility(View.GONE);
        };
        handler.postDelayed(r, 500);


        submit_btn.setOnClickListener(view -> submitAttendance());

        reports_open.setOnClickListener(view -> {
            Intent intent = new Intent(ClassDetail_Activity.this, Reports_Activity.class);
            intent.putExtra("class_name", class_Name);
            intent.putExtra("subject_name", subject_Name);
            intent.putExtra("room_ID", room_ID);
            startActivity(intent);
        });


        addStudent.setOnClickListener(view -> {

            LayoutInflater inflater = LayoutInflater.from(ClassDetail_Activity.this);
            final View view1 = inflater.inflate(R.layout.popup_add_student, null);
            student_name = view1.findViewById(R.id.name_student_popup);
            reg_no = view1.findViewById(R.id.regNo_student_popup);

            lovelyCustomDialog = new LovelyCustomDialog(ClassDetail_Activity.this)
                    .setView(view1)
                    .setTopColorRes(R.color.theme_light)
                    .setTitle("Add Student")
                    .setIcon(R.drawable.ic_baseline_person_add_24)
                    .setCancelable(false)
                    .setListener(R.id.add_btn_popup, view2 -> {

                        String name = student_name.getText().toString();
                        String regNo = reg_no.getText().toString();

                        if (isValid()) {
                            addStudentMethod(name, regNo);
                        } else {
                            Toast.makeText(ClassDetail_Activity.this, "Please fill all the details..", Toast.LENGTH_SHORT).show();
                        }


                    })
                    .setListener(R.id.cancel_btn_popup, view22 -> lovelyCustomDialog.dismiss())
                    .show();

        });

        edit_btn.setOnClickListener(view -> {
            final String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
            FirebaseDatabase.getInstance().getReference().child("Attendance_Reports").child(date + class_Name + subject_Name).removeValue();
            submit_btn.setVisibility(View.VISIBLE);
            edit_btn.setVisibility(View.INVISIBLE);
        });


        // click on excel to select a file
        excel.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(ClassDetail_Activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                selectFile();
            } else {
                ActivityCompat.requestPermissions(ClassDetail_Activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
            }
        });


    }

    //request for storage permission if not given
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectFile();
            } else {
                Toast.makeText(ClassDetail_Activity.this, "Permission Not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void selectFile() {
        //select the file from the file storage
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select File"), 102);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                String filepath = data.getData().getPath();
                //If excel file then only select the file
                if (filepath.endsWith(".xlsx") || filepath.endsWith(".xls")) {
                    readFile(data.getData());
                }
                //else show the error
                else {
                    //Toast.makeText(this,"Please Select an Excel file to upload",Toast.LENGTH_LONG).show();
                    readFile(data.getData());
                }
            }
        }
    }

    private void readFile(final Uri file) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        AsyncTask.execute(() -> {

            final HashMap<String, Object> parentMap = new HashMap<>();

            try {
                XSSFWorkbook workbook;
                //check for the input from the excel file
                try (InputStream inputStream = getContentResolver().openInputStream(file)) {
                    workbook = new XSSFWorkbook(inputStream);
                }
                XSSFSheet sheet = workbook.getSheetAt(0);
                int rowsCount = sheet.getPhysicalNumberOfRows();
                if (rowsCount > 0) {
                    //check rowWise data
                    for (int r = 0; r < rowsCount; r++) {
                        Row row = sheet.getRow(r);
                        if (row.getPhysicalNumberOfCells() == cellCount) {
                            //get cell data
                            String A = getCellData(row, 0);
                            //String B = getCellData(row,1,formulaEvaluator);
                            //initialise the hashmap and put value of a and b into it
                            HashMap<String, Object> quetionMap = new HashMap<>();
                            quetionMap.put("name_student", A);
                            quetionMap.put("regNo_student", String.valueOf(r));
                            quetionMap.put("id", A + r);
                            quetionMap.put("class_id", room_ID);
                            //String id= UUID.randomUUID().toString();
                            parentMap.put(A + r, quetionMap);

                        } else {
                            dialog.dismiss();
                            Toast.makeText(ClassDetail_Activity.this, "row no. " + (r + 1) + " has incorrect data", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    //add the data in firebase if everything is correct
                    runOnUiThread(() -> {
                        //add the data according to timestamp
                        FirebaseDatabase.getInstance().getReference().child("Classes").
                                child(class_Name + subject_Name).child("Student_List").updateChildren(parentMap).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        dialog.dismiss();
                                        Toast.makeText(ClassDetail_Activity.this, "Uploaded Successfully", Toast.LENGTH_LONG).show();
                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(ClassDetail_Activity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                    }
                                });
                    });

                }
                //show the error if file is empty
                else {
                    runOnUiThread(() -> {
                        dialog.dismiss();
                        Toast.makeText(ClassDetail_Activity.this, "File is empty", Toast.LENGTH_LONG).show();

                    });
                    return;
                }
            }
            //show the error message if failed due to file not found
            //show the error message if there is error in input output
            catch (final IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(ClassDetail_Activity.this, e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

    private String getCellData(Row row, int cellposition) {
        String value = "";
        //get cell fom excel sheet
        Cell cell = row.getCell(cellposition);
        switch (cell.getCellType()) {

            case Cell.CELL_TYPE_BOOLEAN:
                return value + cell.getBooleanCellValue();
            case Cell.CELL_TYPE_NUMERIC:
                return value + cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:
                return value + cell.getStringCellValue();
            default:
                return value;
        }
    }

    private void readStudents() {
        FirebaseDatabase.getInstance().getReference("Classes")
                .child(class_Name + subject_Name)
                .child("Student_List")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        students_lists.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Students_List students_list = dataSnapshot.getValue(Students_List.class);
                            assert students_list != null;
                            if (students_list.getClass_id().equals(class_Name + subject_Name)) {
                                students_lists.add(students_list);
                            }
                        }
                        adapter = new StudentsListNewAdapter(ClassDetail_Activity.this, students_lists);
                        mRecyclerview.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void firebaseInit() {
        final String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        FirebaseDatabase.getInstance().getReference("Classes")
                .child(class_Name + subject_Name)
                .child("Student_List")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        count = 0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Students_List students_list = dataSnapshot.getValue(Students_List.class);
                            if (students_list.getClass_id().equals(class_Name + subject_Name)) {
                                count++;
                            }
                        }
                        total_students.setText("Total Students : " + count);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Attendance_Reports").child(date + class_Name + subject_Name);
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Attendance_Reports attendance_reports = snapshot.getValue(Attendance_Reports.class);
                    assert attendance_reports != null;
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
                        } else {
                            submit_btn.setVisibility(View.GONE);
                            place_holder.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    layout_attendance_taken.setVisibility(View.GONE);
                    submit_btn.setVisibility(View.VISIBLE);

                    if (!(count == 0)) {
                        submit_btn.setVisibility(View.VISIBLE);
                        place_holder.setVisibility(View.GONE);
                    } else {
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

    public void submitAttendance() {

        final ProgressDialog progressDialog = new ProgressDialog(ClassDetail_Activity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());


        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        final String dateOnly = String.valueOf(calendar.get(Calendar.DATE));
        @SuppressLint("SimpleDateFormat") final String monthOnly = new SimpleDateFormat("MMM").format(calendar.getTime());

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
            reference.child(date + room_ID).setValue(attendance_reports);


            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.apply();
            editor.clear();
            Toast.makeText(ClassDetail_Activity.this, "Attendance Submitted", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();


        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(ClassDetail_Activity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
        }


    }

    public void addStudentMethod(final String studentName, final String regNo) {

        final ProgressDialog progressDialog = new ProgressDialog(ClassDetail_Activity.this);
        progressDialog.setMessage("Creating class..");
        progressDialog.show();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Classes").child(class_Name + subject_Name).child("Student_List");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", studentName + regNo);
        hashMap.put("name_student", studentName);
        hashMap.put("regNo_student", regNo);
        hashMap.put("class_id", room_ID);
        reference.child(studentName + regNo).setValue(hashMap);
        progressDialog.dismiss();
        lovelyCustomDialog.dismiss();

    }

    public boolean isValid() {

        return !student_name.getText().toString().isEmpty() && !reg_no.getText().toString().isEmpty();
    }

    private void searchStudent(String search_txt) {
        List<Students_List> students_list = new ArrayList<>();
        for (Students_List student : students_lists) {
            if (student.getName_student().toLowerCase().contains(search_txt.toLowerCase())) {
                students_list.add(student);
            }
        }
        adapter = new StudentsListNewAdapter(context, students_list);
        mRecyclerview.setAdapter(adapter);

    }

    private void DeleteClass() {
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Classes").child(class_Name + subject_Name).child("Student_List");
        reference1.removeValue();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Classes").child(class_Name + subject_Name);
        reference.removeValue();

        Intent intent = new Intent(ClassDetail_Activity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Toast.makeText(ClassDetail_Activity.this, "Class Deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {

        if (submit_btn.getVisibility() == View.VISIBLE) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("please click submit button to submit the attendance \n" +
                            "من فضلك اضغط علي زر submit لتسجيل حضور اليوم ")
                    .setCancelable(false)
                    .setPositiveButton("Submit \n تسجيل", (dialogInterface, i) -> {
                        submitAttendance();
                        finish();

                    })
                    .setNegativeButton("edit \n تعديل", (dialogInterface, i) -> dialogInterface.cancel())
                    .setNeutralButton("leave \n خروج", (dialogInterface, i) -> finish());

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_class_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Delete Class")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Are you sure you want to delete this class ");
            builder.setPositiveButton("yes", (dialogInterface, i) -> DeleteClass());
            builder.setNegativeButton("No", null);

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
        if (item.getTitle().equals("Search Students")) {
            edt_search.setVisibility(View.VISIBLE);
            edt_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    searchStudent(s.toString());
                }
            });

        }
        if (item.getTitle().equals("DELETE ATTENDANCE")) {
            final String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
            Common.holidayOrNot = "yes";
            FirebaseDatabase.getInstance().getReference("Attendance_Reports")
                    .child(date + Common.currentClassName)
                    .removeValue()
                    .addOnSuccessListener(unused ->
                            Toast.makeText(ClassDetail_Activity.this, "Processing...", Toast.LENGTH_SHORT).show());

            FirebaseDatabase.getInstance().getReference("Classes").child(Common.currentClassName)
                    .child("Attendance").child(date)
                    .removeValue()
                    .addOnSuccessListener(unused ->
                            Toast.makeText(ClassDetail_Activity.this, "Done !", Toast.LENGTH_SHORT).show());

            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}