package com.avaantony.dimitsattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.avaantony.dimitsattendance.common.Common;
import com.avaantony.dimitsattendance.databinding.ActivityAdminBinding;
import com.avaantony.dimitsattendance.ui.grade.HomeActivity;

public class AdminActivity extends AppCompatActivity {
    private ActivityAdminBinding binding;
    FirebaseAuth auth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        binding.login.setOnClickListener(view -> {
            pd = new ProgressDialog(AdminActivity.this);
            pd.setMessage("Please wait..");
            pd.show();

            String str_password = binding.password.getText().toString();
            if (TextUtils.isEmpty(str_password)) {
                Toast.makeText(AdminActivity.this, "All fields are required ", Toast.LENGTH_SHORT).show();
            } else if (str_password.equals("12")) {
                Intent intent = new Intent(AdminActivity.this, HomeActivity.class);
                Common.currentUserType = "admin";
                pd.dismiss();
                startActivity(intent);
            } else {
                Toast.makeText(AdminActivity.this, "Cannot log in", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }

}