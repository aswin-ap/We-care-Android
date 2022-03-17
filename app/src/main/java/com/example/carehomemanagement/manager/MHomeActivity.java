package com.example.carehomemanagement.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carehomemanagement.databinding.ActivityMhomeBinding;

public class MHomeActivity extends AppCompatActivity {
    private ActivityMhomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMhomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();

    }

    private void initView() {
        binding.btnAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MHomeActivity.this, AddPatientActivity.class);
                startActivity(i);
            }
        });
    }
}