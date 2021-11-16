package com.staugustine.dimitsattendance.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.staugustine.dimitsattendance.R;
import com.staugustine.dimitsattendance.StudentProfile;
import com.staugustine.dimitsattendance.model.Attendance_Students_List;

import java.util.List;

public class Reports_Detail_NewAdapter extends RecyclerView.Adapter<Reports_Detail_NewAdapter.ViewHolder> {
    Context mContext;
    List<Attendance_Students_List> attendance_students_lists;

    public Reports_Detail_NewAdapter(Context mContext, List<Attendance_Students_List> attendance_students_lists) {
        this.mContext = mContext;
        this.attendance_students_lists = attendance_students_lists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_detail_adapter_item, parent, false);
        return new Reports_Detail_NewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Attendance_Students_List attendanceStudentsList = attendance_students_lists.get(position);

        holder.namE.setText(attendanceStudentsList.getStudentName());
        holder.regNo.setText(attendanceStudentsList.getStudentRegNo());
        if (attendanceStudentsList.getAttendance().equals("Present")){
            holder.status.setText("P");
            holder.circle.setCardBackgroundColor(mContext.getResources().getColor(R.color.green_new));
        }else{
            holder.status.setText("A");
            holder.circle.setCardBackgroundColor(mContext.getResources().getColor(R.color.red_new));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on clicking on a specific student to open their profile, store his/her id to call later from the StudentProfile Activity
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("id", attendanceStudentsList.getUnique_ID());
                editor.apply();

                // open the StudentProfile Activity and pass the required info to there
                Intent intent = new Intent(mContext.getApplicationContext(), StudentProfile.class);
                intent.putExtra("id",attendanceStudentsList.getUnique_ID());
                intent.putExtra("name",attendanceStudentsList.getStudentName());
                intent.putExtra("class_id",attendanceStudentsList.getClassID());
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return attendance_students_lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView namE;
        public TextView regNo;
        public TextView status;

        public CardView circle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            namE = itemView.findViewById(R.id.student_name_report_detail_adapter);
            regNo = itemView.findViewById(R.id.student_regNo_report_detail_adapter);
            status = itemView.findViewById(R.id.status_report_detail_adapter);
            circle = itemView.findViewById(R.id.cardView_report_detail_adapter);
        }
    }
}
