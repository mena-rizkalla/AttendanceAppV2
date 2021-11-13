package com.staugustine.dimitsattendance.Adapter;

import static androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.staugustine.dimitsattendance.ClassDetail_Activity;
import com.staugustine.dimitsattendance.R;
import com.staugustine.dimitsattendance.model.Class_Names;
import com.staugustine.dimitsattendance.model.Students_List;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ClassListNewAdapter extends RecyclerView.Adapter<ClassListNewAdapter.ViewHolder>{

    Context mContext;
    List<Class_Names> classNamesList;
    public CardView cardView;
    public Activity mActivity;

    public ClassListNewAdapter(Context mContext, List<com.staugustine.dimitsattendance.model.Class_Names> classNamesList) {
        this.mContext = mContext;
        this.classNamesList = classNamesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_adapter, parent, false);
        return new ClassListNewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Class_Names classNames = classNamesList.get(position);

        numberOfStudent(holder.total_students,classNames);
        holder.class_name.setText(classNames.getName_class());
        holder.subject_name.setText(classNames.getName_subject());

        switch (classNames.getPosition_bg()) {
            case "0":
                holder.imageView_bg.setImageResource(R.drawable.asset_bg_paleblue);
                holder.frameLayout.setBackgroundResource(R.drawable.gradient_color_1);
                break;
            case "1":
                holder.imageView_bg.setImageResource(R.drawable.asset_bg_green);
                holder.frameLayout.setBackgroundResource(R.drawable.gradient_color_2);
                break;
            case "2":
                holder.imageView_bg.setImageResource(R.drawable.asset_bg_yellow);
                holder.frameLayout.setBackgroundResource(R.drawable.gradient_color_3);
                break;
            case "3":
                holder.imageView_bg.setImageResource(R.drawable.asset_bg_palegreen);
                holder.frameLayout.setBackgroundResource(R.drawable.gradient_color_4);
                break;
            case "4":
                holder.imageView_bg.setImageResource(R.drawable.asset_bg_paleorange);
                holder.frameLayout.setBackgroundResource(R.drawable.gradient_color_5);
                break;
            case "5":
                holder.imageView_bg.setImageResource(R.drawable.asset_bg_white);
                holder.frameLayout.setBackgroundResource(R.drawable.gradient_color_6);
                holder.subject_name.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.text_color_secondary));
                holder.class_name.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.text_color_secondary));
                holder.total_students.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.text_color_secondary));
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ClassDetail_Activity.class);
                intent.putExtra("theme", classNamesList.get(holder.getAdapterPosition()).getPosition_bg());
                intent.putExtra("className", classNamesList.get(holder.getAdapterPosition()).getName_class());
                intent.putExtra("subjectName", classNamesList.get(holder.getAdapterPosition()).getName_subject());
                intent.putExtra("classroom_ID", classNamesList.get(holder.getAdapterPosition()).getId());
                Pair<View, String> p1 = Pair.create((View) cardView, "ExampleTransition");
               // ActivityOptionsCompat optionsCompat = makeSceneTransitionAnimation(MainActivity.class, p1);
                view.getContext().startActivity(intent);
            }
        });

    }

    private void numberOfStudent(TextView total_students, Class_Names classNames) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Classes").child(classNames.getId()).child("Student_List");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               int count = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Students_List students_list = dataSnapshot.getValue(Students_List.class);
                    if (students_list.getClass_id().equals(classNames.getName_class()+classNames.getName_subject())){
                        count ++;
                    }
                }
                total_students.setText("Total Students : " + count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return classNamesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView class_name;
        public TextView subject_name;
        public TextView total_students;
        public ImageView imageView_bg;
        public RelativeLayout frameLayout;
        public CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            class_name = itemView.findViewById(R.id.className_adapter);
            subject_name = itemView.findViewById(R.id.subjectName_adapter);
            imageView_bg = itemView.findViewById(R.id.imageClass_adapter);
            frameLayout = itemView.findViewById(R.id.frame_bg);
            cardView = itemView.findViewById(R.id.cardView_adapter);
            total_students = itemView.findViewById(R.id.totalStudents_adapter);

        }
    }


}
