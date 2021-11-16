package com.staugustine.dimitsattendance.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.staugustine.dimitsattendance.R;
import com.staugustine.dimitsattendance.model.Attendance_Students_List;

import java.util.List;

public class SpecificAttendanceAdapter extends RecyclerView.Adapter<SpecificAttendanceAdapter.MyViewHolder> {

    Context context;
    List<Attendance_Students_List> attendance_students_lists;

    public SpecificAttendanceAdapter(Context context, List<Attendance_Students_List> attendance_students_lists) {
        this.context = context;
        this.attendance_students_lists = attendance_students_lists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.specific_attendance_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Attendance_Students_List model = new Attendance_Students_List();
        holder.student_name.setText(model.getStudentName());
        holder.status.setText(model.getAttendance());
        holder.id.setText(model.getUnique_ID());
        if(model.getDate_and_classID() == null){
            holder.date.setVisibility(View.INVISIBLE);
        }else {
            holder.date.setText(model.getDate_and_classID());

        }

    }

    @Override
    public int getItemCount() {
        return attendance_students_lists.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView student_name,status,id,date;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            student_name = itemView.findViewById(R.id.student_name);
            status = itemView.findViewById(R.id.status);
            id = itemView.findViewById(R.id.txt_id);
            date = itemView.findViewById(R.id.report_date);
        }
    }
}
