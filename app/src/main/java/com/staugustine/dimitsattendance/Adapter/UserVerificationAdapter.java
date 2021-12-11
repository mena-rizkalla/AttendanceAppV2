package com.staugustine.dimitsattendance.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.staugustine.dimitsattendance.R;
import com.staugustine.dimitsattendance.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserVerificationAdapter extends RecyclerView.Adapter<UserVerificationAdapter.ViewHolder> {

    Context context;
    List<User> list = new ArrayList<>();

    public UserVerificationAdapter() {
    }

    public UserVerificationAdapter(Context context, List<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserVerificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = list.get(position);
        holder.teacher_name.setText(user.getUsername());

        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Users").child(user.getId()).child("active").setValue("1").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Verified", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.wrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Users").child(user.getId()).child("active").setValue("0").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "unverified", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView teacher_name;
        ImageView wrong,check;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            teacher_name = itemView.findViewById(R.id.teacher_name);
            wrong = itemView.findViewById(R.id.wrong_btn);
            check = itemView.findViewById(R.id.true_btn);
        }
    }
}
