package com.staugustine.dimitsattendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.staugustine.dimitsattendance.common.Common;
import com.staugustine.dimitsattendance.databinding.ActivityAdminBinding;
import com.staugustine.dimitsattendance.ui.grade.HomeActivity;

public class AdminActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityAdminBinding binding;

    EditText email,password;
    Button login;
    TextView txt_signup;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        auth = FirebaseAuth.getInstance();

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog pd = new ProgressDialog(AdminActivity.this);
                pd.setMessage("Please wait..");
                pd.show();

                String str_password = password.getText().toString();
                if ( TextUtils.isEmpty(str_password)){
                    Toast.makeText(AdminActivity.this,"All fields are required ",Toast.LENGTH_SHORT).show();
                }else if (str_password.equals("12")){
                    Intent intent = new Intent(AdminActivity.this, HomeActivity.class);
                    Common.currentUserType = "admin";
                    startActivity(intent);
                }

            }
        });
    }
}