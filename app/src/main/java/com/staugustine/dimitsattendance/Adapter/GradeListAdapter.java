package com.staugustine.dimitsattendance.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.staugustine.dimitsattendance.ClassDetail_Activity;
import com.staugustine.dimitsattendance.GradeDetailActivity;
import com.staugustine.dimitsattendance.R;
import com.staugustine.dimitsattendance.common.Common;
import com.staugustine.dimitsattendance.model.Class_Names;
import com.staugustine.dimitsattendance.model.Grade_Names;

import java.util.List;

public class GradeListAdapter extends RecyclerView.Adapter<GradeListAdapter.ViewHolder>{

    Context mContext;
    List<Grade_Names> gradeNamesList;
    public CardView cardView;

    public GradeListAdapter(Context mContext, List<Grade_Names> gradeNamesList) {
        this.mContext = mContext;
        this.gradeNamesList = gradeNamesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_adapter, parent, false);
        return new GradeListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       Grade_Names grade_names = gradeNamesList.get(position);
       holder.class_name.setVisibility(View.GONE);
       holder.subject_name.setText(grade_names.getName_grade());
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
                Intent intent = new Intent(view.getContext(), GradeDetailActivity.class);
                intent.putExtra("theme", gradeNamesList.get(holder.getAdapterPosition()).getPosition_bg());
                intent.putExtra("gradeName", gradeNamesList.get(holder.getAdapterPosition()).getName_grade());
                Common.currentClassName = gradeNamesList.get(holder.getAdapterPosition()).getName_grade();
                intent.putExtra("graderoom_ID", gradeNamesList.get(holder.getAdapterPosition()).getId());
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
