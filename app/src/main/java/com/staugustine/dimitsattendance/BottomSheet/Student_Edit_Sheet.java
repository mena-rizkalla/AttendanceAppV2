package com.staugustine.dimitsattendance.BottomSheet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.staugustine.dimitsattendance.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.staugustine.dimitsattendance.StudentProfile;
import com.staugustine.dimitsattendance.common.Common;
import com.staugustine.dimitsattendance.model.Attendance_Students_List;

import java.util.Iterator;

public class Student_Edit_Sheet extends BottomSheetDialogFragment {

    public String _name;
    public TextView totalDaysOff;
    public TextView totalDays;
    public TextView totalDaysOn;
    public String _regNo;
    public String _mobNo;
    public String _uniqueId;
    public View save;
    public EditText name_student, regNo_student, mobNo_student;
    public CardView call;
    public CardView detail;
    private int INITIAL_DAYS_OFF = 0;
    private int INITIAL_DAYS_ON = 0;

    public Student_Edit_Sheet(String stuName, String regNo, String mobileNo, String uniqueId) {
        _name = stuName;
        _regNo = regNo;
        _mobNo = mobileNo;
        _uniqueId = uniqueId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.bottomsheet_student_edit, container, false);

        totalDaysOn = v.findViewById(R.id.total_days_on);
        totalDays = v.findViewById(R.id.total_days);
        totalDaysOff = v.findViewById(R.id.total_days_off);
        name_student = v.findViewById(R.id.stu_name_edit);
        regNo_student = v.findViewById(R.id.stu_regNo_edit);
        mobNo_student = v.findViewById(R.id.stu_mobNo_edit);
        call = v.findViewById(R.id.call_edit);
        detail = v.findViewById(R.id.detail);
        save = v.findViewById(R.id.save);

        name_student.setText(_name);
        regNo_student.setText(_regNo);
        mobNo_student.setText(_mobNo);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Student_List").child(_name+_regNo);
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("name_student",name_student.getText().toString());
                hashMap.put("regNo_student",regNo_student.getText().toString());
                hashMap.put("mobileNo_student",mobNo_student.getText().toString());
                reference.updateChildren(hashMap);**/
                Toast.makeText(getContext(),"Updated",Toast.LENGTH_SHORT).show();
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "tel:" + _mobNo.trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on clicking on a specific student to open their profile, store his/her id to call later from the StudentProfile Activity
                SharedPreferences.Editor editor = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("id", _uniqueId);
                editor.apply();

                // open the StudentProfile Activity and pass the required info to there
                Intent intent = new Intent(getContext(), StudentProfile.class);
                intent.putExtra("id",_uniqueId);
                intent.putExtra("name",_name);
                //intent.putExtra("class_id",attendanceStudentsList.getClassID());
                startActivity(intent);
            }
        });

        downloadReport();
        return v;
    }

    private void downloadReport() {
        // refer to the Correct Reference
        FirebaseDatabase.getInstance().getReference("Classes").child(Common.currentClassName)
                .child("Attendance")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // you already guessed it :)
                        if (snapshot.exists()) {

                            int TotalDays = (int) snapshot.getChildrenCount();
                            totalDays.setText(""+TotalDays);
                            // for each item under the reference "attendance" do the following :
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                // iterator to iterate over each object under the refernce
                                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                                while (iterator.hasNext()) {
                                    // declaring the data and storing them in a StudentAttendanceModel object
                                    DataSnapshot dataSnapshot1 = iterator.next();
                                    Attendance_Students_List list = dataSnapshot1.getValue(Attendance_Students_List.class);

                                    // spend all your life avoiding nullPointerExceptions
                                    assert list != null;
                                    // download only the days off for this student
                                    if (list.getUnique_ID().equals(_uniqueId)
                                            && list.getAttendance().equals("Absent")) {

                                        // add their value to 0 and setting the TextView of the days off
                                        INITIAL_DAYS_OFF =+ 1;
                                        totalDaysOff.setText(" " + INITIAL_DAYS_OFF);


                                    }else if (list.getUnique_ID().equals(_uniqueId)
                                            && list.getAttendance().equals("Present")){
                                        INITIAL_DAYS_ON =+1;
                                        totalDaysOn.setText(""+INITIAL_DAYS_ON);
                                    }

                                }
                            }
                        } else {
                            Toast.makeText(getContext(), "err", Toast.LENGTH_SHORT).show();
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }


}
