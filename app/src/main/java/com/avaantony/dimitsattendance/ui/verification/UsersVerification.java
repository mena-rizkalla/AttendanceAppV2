package com.avaantony.dimitsattendance.ui.verification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.avaantony.dimitsattendance.Adapter.UserVerificationAdapter;
import com.avaantony.dimitsattendance.databinding.ActivityUsersVerificationBinding;
import com.avaantony.dimitsattendance.model.User;

import java.util.ArrayList;
import java.util.List;


public class UsersVerification extends AppCompatActivity {

    RecyclerView recyclerView;
    UserVerificationAdapter adapter;
    ActivityUsersVerificationBinding binding;
    List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = binding.recyclerViewMain;
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        users = new ArrayList<>();
        adapter = new UserVerificationAdapter(UsersVerification.this, users);
        recyclerView.setAdapter(adapter);

        UserVerificationViewModel viewModel = ViewModelProviders
                .of(this).get(UserVerificationViewModel.class);

        viewModel.getListMutableLiveData().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                adapter = new UserVerificationAdapter(UsersVerification.this, users);
                recyclerView.setAdapter(adapter);
            }
        });

    }


}