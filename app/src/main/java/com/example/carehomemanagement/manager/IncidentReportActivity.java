package com.example.carehomemanagement.manager;

import android.os.Bundle;
import android.view.View;

import com.example.carehomemanagement.BaseActivity;
import com.example.carehomemanagement.databinding.ActivityIncidentReportBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class IncidentReportActivity extends BaseActivity {
    private String userId, userName, pickedTime;
    private ActivityIncidentReportBinding binding;
    FirebaseFirestore fireStore;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIncidentReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        fireStore = getFireStoreInstance();
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast(IncidentReportActivity.this,"Updated Successfully");
                finish();
            }
        });
    }
}