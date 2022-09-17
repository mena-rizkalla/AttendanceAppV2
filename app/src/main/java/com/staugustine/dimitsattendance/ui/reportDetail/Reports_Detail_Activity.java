package com.staugustine.dimitsattendance.ui.reportDetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.staugustine.dimitsattendance.Adapter.Reports_Detail_NewAdapter;
import com.staugustine.dimitsattendance.excel.ExcelExporter;
import com.staugustine.dimitsattendance.R;
import com.staugustine.dimitsattendance.databinding.ActivityReportsDetailBinding;

import java.util.Objects;

public class Reports_Detail_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    static final Integer WRITE_EXST = 0x3;
    static final Integer READ_EXST = 0x4;

    private ActivityReportsDetailBinding binding;

    Reports_Detail_NewAdapter reportsNewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportsDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ReportsDetailViewModel reportsDetailViewModel = ViewModelProviders.of(this).get(ReportsDetailViewModel.class);

        String room_ID = getIntent().getStringExtra("ID");
        String classname = getIntent().getStringExtra("class");
        String subjName = getIntent().getStringExtra("subject");
        String date = getIntent().getStringExtra("date");

        setSupportActionBar(binding.toolbarReportsDetail);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        recyclerView = binding.recyclerViewReportsDetail;
        binding.toolbarTitle.setText(date);
        binding.subjNameReportDetail.setText(subjName);
        binding.classnameReportDetail.setText(classname);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        reportsDetailViewModel.getStudentList(classname,subjName,date).observe(this,studentList ->{
            reportsNewAdapter = new Reports_Detail_NewAdapter(Reports_Detail_Activity.this,studentList);

            recyclerView.setAdapter(reportsNewAdapter);
        });

        binding.exportexcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST);
                askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXST);
                ExcelExporter.export(date,classname,subjName);

            }
        });
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Reports_Detail_Activity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    Reports_Detail_Activity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(Reports_Detail_Activity.this,
                        new String[]{permission}, requestCode);

            } else {
                ActivityCompat.requestPermissions(Reports_Detail_Activity.this,
                        new String[]{permission}, requestCode);
            }
        } else {
//            Toast.makeText(this, permission + " is already granted.",
//                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.only_dot, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}