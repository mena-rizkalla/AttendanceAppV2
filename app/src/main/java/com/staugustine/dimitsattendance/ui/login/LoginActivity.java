package com.staugustine.dimitsattendance.ui.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.staugustine.dimitsattendance.RegisterActivity;
import com.staugustine.dimitsattendance.common.Common;
import com.staugustine.dimitsattendance.ui.grade.HomeActivity;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    FirebaseAuth auth;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.staugustine.dimitsattendance.databinding.ActivityLoginBinding binding = com.staugustine.dimitsattendance.databinding.ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        email = binding.email;
        password = binding.password;
        auth = FirebaseAuth.getInstance();
        LoginViewModel loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        binding.txtSignup.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        binding.login.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please wait..");
            dialog = builder.create();
            dialog.show();

            String str_email = email.getText().toString();
            String str_password = password.getText().toString();

            if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                Toast.makeText(LoginActivity.this, "All fields are required ", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                loginViewModel.getStatus(str_email, str_password).observe(this, status -> {
                    if (status) {
                        Intent intent = new Intent(LoginActivity.this
                                , HomeActivity.class);

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                | Intent.FLAG_ACTIVITY_NEW_TASK);

                        Common.currentUserType = "user";
                        dialog.dismiss();
                        startActivity(intent);
                        finish();
                    } else {
                        loginViewModel.getMessageError(str_email, str_password).observe(this, message -> {
                            if (message.equals("BANNED")) {
                                showBannedDialog();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    }
                });

            }

        });

    }

    private void showBannedDialog() {
        androidx.appcompat.app.AlertDialog.Builder banned = new androidx.appcompat.app.AlertDialog.Builder(this);
        banned.setTitle("Verification Alert !");
        banned.setMessage("Please, ask the admin or the headmaster to verify your account to use the Application.");
        banned.show();
    }

}