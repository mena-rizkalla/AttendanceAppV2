package com.ajstudios.easyattendance.BottomSheet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.ajstudios.easyattendance.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Student_Edit_Sheet extends BottomSheetDialogFragment {

    public String _name;
    public String _regNo;
    public String _mobNo;
    public View save;
    public EditText name_student, regNo_student, mobNo_student;
    public CardView call;

    public Student_Edit_Sheet(String stuName, String regNo, String mobileNo) {
        _name = stuName;
        _regNo = regNo;
        _mobNo = mobileNo;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.bottomsheet_student_edit, container, false);

        name_student = v.findViewById(R.id.stu_name_edit);
        regNo_student = v.findViewById(R.id.stu_regNo_edit);
        mobNo_student = v.findViewById(R.id.stu_mobNo_edit);
        call = v.findViewById(R.id.call_edit);
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

        return v;
    }


}
