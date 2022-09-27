package com.avaantony.dimitsattendance.Adapter;

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

import com.avaantony.dimitsattendance.ui.main.MainActivity;
import com.avaantony.dimitsattendance.R;
import com.avaantony.dimitsattendance.model.Grade_Names;

import java.util.List;

public class GradeDetailAdapter extends RecyclerView.Adapter<GradeDetailAdapter.ViewHolder> {

    Context mContext;
    List<Grade_Names> gradeNamesList;
    public CardView cardView;

    public GradeDetailAdapter(Context mContext, List<Grade_Names> gradeNamesList) {
        this.mContext = mContext;
        this.gradeNamesList = gradeNamesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_adapter, parent, false);
        return new GradeDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Grade_Names grade_names = gradeNamesList.get(position);
        holder.class_name.setVisibility(View.GONE);
        holder.subject_name.setText(grade_names.getName_grade());
        holder.subject_name.setGravity(1);
        holder.total_students.setVisibility(View.GONE);

        switch (grade_names.getPosition_bg()){
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
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra("theme", gradeNamesList.get(holder.getAdapterPosition()).getPosition_bg());
                intent.putExtra("gradeDetailName", gradeNamesList.get(holder.getAdapterPosition()).getName_grade());
                intent.putExtra("gradeDetailroom_ID", gradeNamesList.get(holder.getAdapterPosition()).getId());
                Pair<View, String> p1 = Pair.create((View) cardView, "ExampleTransition");
                // ActivityOptionsCompat optionsCompat = makeSceneTransitionAnimation(MainActivity.class, p1);
                view.getContext().startActivity(intent);
                //Toast.makeText(mContext, ""+ Common.currentClassName, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return gradeNamesList.size();
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
