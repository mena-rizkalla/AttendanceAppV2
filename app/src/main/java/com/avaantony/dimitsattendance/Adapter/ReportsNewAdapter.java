package com.avaantony.dimitsattendance.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avaantony.dimitsattendance.R;
import com.avaantony.dimitsattendance.ui.reportDetail.Reports_Detail_Activity;
import com.avaantony.dimitsattendance.model.Attendance_Reports;

import java.util.List;

public class ReportsNewAdapter extends RecyclerView.Adapter<ReportsNewAdapter.ViewHolder> {
    Context mContext;
    List<com.avaantony.dimitsattendance.model.Attendance_Reports> attendance_reports;

    public ReportsNewAdapter(Context mContext, List<Attendance_Reports> attendance_reports) {
        this.mContext = mContext;
        this.attendance_reports = attendance_reports;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reports_adapter_item, parent, false);
        return new ReportsNewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Attendance_Reports attendanceReport = attendance_reports.get(position);

        holder.month.setText(attendanceReport.getMonthOnly());
        holder.date.setText(attendanceReport.getDateOnly());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Reports_Detail_Activity.class);
                intent.putExtra("ID", attendance_reports.get(holder.getAbsoluteAdapterPosition()).getDate_and_classID());
                intent.putExtra("date", attendance_reports.get(holder.getAbsoluteAdapterPosition()).getDate());
                intent.putExtra("subject", attendance_reports.get(holder.getAbsoluteAdapterPosition()).getSubjName());
                intent.putExtra("class", attendance_reports.get(holder.getAbsoluteAdapterPosition()).getClassname());
               mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return attendance_reports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView month;
        public TextView date;

        public Activity mActivity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            month = itemView.findViewById(R.id.month_report_adapter);
            date = itemView.findViewById(R.id.date_report_adapter);
        }
    }
}
