package com.avaantony.dimitsattendance.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.avaantony.dimitsattendance.R;
import com.avaantony.dimitsattendance.model.User;

import java.util.List;

public class UserVerificationAdapter extends RecyclerView.Adapter<UserVerificationAdapter.ViewHolder> {

    Context context;
    List<User> list;


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
        holder.addChips();
        User user = list.get(position);
        holder.teacher_name.setText(user.getUsername());

        holder.chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = group.findViewById(checkedId);

            if (chip != null)
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(user.getId()).child("grade").setValue(chip.getChipText());

        });

        holder.check.setOnClickListener(v ->
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(user.getId()).child("active")
                        .setValue("1").addOnSuccessListener(
                                unused ->
                                        Toast.makeText(context, "Verified", Toast.LENGTH_SHORT).show()));

        holder.wrong.setOnClickListener(v ->
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(user.getId()).child("active").setValue("0")
                        .addOnSuccessListener(unused ->
                                Toast.makeText(context, "unverified", Toast.LENGTH_SHORT).show()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView teacher_name;
        ImageView wrong, check;
        ChipGroup chipGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            teacher_name = itemView.findViewById(R.id.teacher_name);
            wrong = itemView.findViewById(R.id.wrong_btn);
            check = itemView.findViewById(R.id.true_btn);
            chipGroup = itemView.findViewById(R.id.chip_group);
        }

        private void addChips() {
            FirebaseDatabase.getInstance().getReference("Grade").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Chip chip = new Chip(context);
                            chip.setText(dataSnapshot.getKey());
                            chip.setCheckable(true);
                            chip.setTextColor(context.getResources().getColor(R.color.white));
                            chip.setTextAppearance(R.style.TextAppearance_Design_HelperText);
                            chipGroup.addView(chip);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }


}
