package com.staugustine.dimitsattendance;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.staugustine.dimitsattendance.common.Common;
import com.staugustine.dimitsattendance.databinding.ActivityLoginBinding;
import com.staugustine.dimitsattendance.model.User;
import com.staugustine.dimitsattendance.ui.grade.HomeActivity;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    FirebaseAuth auth;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.staugustine.dimitsattendance.databinding.ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        email = binding.email;
        password = binding.password;
        auth = FirebaseAuth.getInstance();

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
            } else {
                auth.signInWithEmailAndPassword(str_email, str_password)
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    User user = dataSnapshot.getValue(User.class);
                                                    assert user != null;
                                                    if (user.getActive().equals("1")) {

                                                        Intent intent = new Intent(LoginActivity.this
                                                                , HomeActivity.class);

                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                                | Intent.FLAG_ACTIVITY_NEW_TASK);

                                                        Common.currentUserType = "user";
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        showBannedDialog();
                                                    }
                                                    dialog.dismiss();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError e) {
                                                dialog.dismiss();
                                                Toast.makeText(LoginActivity.this,
                                                        e.getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                dialog.dismiss();
                                Toast.makeText(LoginActivity.this, "failed", Toast.LENGTH_SHORT).show();
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