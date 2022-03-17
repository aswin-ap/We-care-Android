package com.example.carehomemanagement.staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carehomemanagement.databinding.ActivityShomeBinding;

public class SHomeActivity extends AppCompatActivity {
    private ActivityShomeBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        binding.btnAllPatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SHomeActivity.this, PatientsActivity.class);
                startActivity(i);
            }
        });
    }
}