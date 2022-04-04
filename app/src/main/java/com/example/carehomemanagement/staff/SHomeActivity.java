package com.example.carehomemanagement.staff;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carehomemanagement.LoginActivity;
import com.example.carehomemanagement.data.prefrence.SessionManager;
import com.example.carehomemanagement.databinding.ActivityShomeBinding;
import com.example.carehomemanagement.manager.IncidentReportActivity;
import com.example.carehomemanagement.utils.Constants;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SHomeActivity extends AppCompatActivity {
    private ActivityShomeBinding binding;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        sessionManager = new SessionManager(this);
        binding.textGreeting.setText("Hey,\n"+sessionManager.getUserName());
        binding.btnDailyRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SHomeActivity.this, PatientsActivity.class);
                i.putExtra("FROM", Constants.DAILY_ROUTINE);
                startActivity(i);
            }
        });

        binding.btnIncidentReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SHomeActivity.this, IncidentReportActivity.class);
                startActivity(i);
            }
        });

        binding.btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogOutPress();
            }
        });
    }


    public void onLogOutPress() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SHomeActivity.this);
        builder.setTitle("Logout ?");
        builder.setMessage("Are you sure want to Logout ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sessionManager.clear();
                Intent intent = new Intent(SHomeActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                dialogInterface.dismiss();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();

    }
}