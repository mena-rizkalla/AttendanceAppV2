package com.staugustine.dimitsattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.staugustine.dimitsattendance.common.Common;
import com.staugustine.dimitsattendance.databinding.ActivityRegisterBinding;
import com.staugustine.dimitsattendance.ui.login.LoginActivity;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    EditText username, fullName, email, password;
    Button register;
    TextView txt_login;
    FirebaseAuth auth;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();


        username = binding.username;
        fullName = binding.fullname;
        email = binding.email;
        password = binding.password;
        register = binding.register;
        txt_login = binding.txtLogin;

        txt_login.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));

        register.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please wait..");
            dialog = builder.create();
            dialog.show();

            String str_username = username.getText().toString();
            String str_fullName = fullName.getText().toString();
            String str_email = email.getText().toString();
            String str_password = password.getText().toString();

            if (str_username.isEmpty() || str_fullName.isEmpty() || str_email.isEmpty() || str_password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "All fields are required ", Toast.LENGTH_SHORT).show();
            } else if (str_password.length() < 6) {
                Toast.makeText(RegisterActivity.this, "Password is weak ", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                register(str_username, str_fullName, str_email, str_password);
            }

        });
    }

    private void register(String username, String fullName, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        assert firebaseUser != null;
                        String userid = firebaseUser.getUid();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("id", userid);
                        hashMap.put("username", username.toLowerCase());
                        hashMap.put("fullname", fullName);
                        hashMap.put("active", "0");
                        hashMap.put("imageurl", "https://firebasestorage.googleapis.com/v0/b/dimits-app.appspot.com/o/Screenshot_22.png?alt=media&token=23f595d1-418c-400c-85c2-530791bf990d");

                        FirebaseDatabase.getInstance().getReference()
                                .child("Users")
                                .child(userid)
                                .setValue(hashMap)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        dialog.dismiss();
                                        Toast.makeText(RegisterActivity.this
                                                , "Account created, please sign in"
                                                , Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        Common.currentUserType = "User";
                                        startActivity(intent);
                                    }
                                });
                    } else {
                        dialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "you can't register with this email or password", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}