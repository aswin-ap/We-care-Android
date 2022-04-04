package com.example.carehomemanagement.manager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carehomemanagement.LoginActivity;
import com.example.carehomemanagement.data.prefrence.SessionManager;
import com.example.carehomemanagement.databinding.ActivityMhomeBinding;
import com.example.carehomemanagement.staff.SHomeActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MHomeActivity extends AppCompatActivity {
    private ActivityMhomeBinding binding;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMhomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();

    }

    private void initView() {
        sessionManager = new SessionManager(this);
        binding.textGreeting.setText("Hey,\n"+sessionManager.getUserName());
        binding.btnAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MHomeActivity.this, AddPatientActivity.class);
                startActivity(i);
            }
        });
        binding.btnCarePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MHomeActivity.this, CarePlanActivity.class);
                startActivity(i);
            }
        });

        binding.btnIncidentReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MHomeActivity.this, IncidentReportActivity.class);
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
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MHomeActivity.this);
        builder.setTitle("Logout ?");
        builder.setMessage("Are you sure want to Logout ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sessionManager.clear();
                Intent intent = new Intent(MHomeActivity.this, LoginActivity.class);
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