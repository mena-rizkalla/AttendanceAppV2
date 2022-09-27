package com.avaantony.dimitsattendance;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.avaantony.dimitsattendance.databinding.ActivityStartBinding;
import com.avaantony.dimitsattendance.ui.login.LoginActivity;

public class StartActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.avaantony.dimitsattendance.databinding.ActivityStartBinding binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.login.setOnClickListener(view -> startActivity(new Intent(StartActivity.this, LoginActivity.class)));

        binding.register.setOnClickListener(view -> startActivity(new Intent(StartActivity.this, RegisterActivity.class)));

        binding.buttonAdmin.setOnClickListener(view -> startActivity(new Intent(StartActivity.this, AdminActivity.class)));

    }

}