package com.example.carehomemanagement.manager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.example.carehomemanagement.BaseActivity;
import com.example.carehomemanagement.databinding.ActivityCarePlanBinding;
import com.example.carehomemanagement.utils.Validation;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CarePlanActivity extends BaseActivity {
    private String userId, userName, pickedTime;
    private ActivityCarePlanBinding binding;
    FirebaseFirestore fireStore;
    final Calendar myCalendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCarePlanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setFromIntent();
        initView();

    }

    private void initView() {
        fireStore = getFireStoreInstance();
        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                String myFormat = "MM/dd/yy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
                binding.txtDate.setText(dateFormat.format(myCalendar.getTime()));
            }
        };
        binding.ivBack.setOnClickListener(view -> finish());
        binding.txtDate.setText(currentDateWithText());
      /*  binding.txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CarePlanActivity.this, dateListener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });*/
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validation.isValidField(binding.etResident.getText().toString())) {
                    if (Validation.isValidField(binding.etCreatedBy.getText().toString())) {
                        if (Validation.isValidField(binding.etDescription.getText().toString())) {
                            showToast(CarePlanActivity.this, "Updated Successfully");
                            finish();
                        } else
                            binding.etDescription.setError("Please enter valid description");
                    } else
                        binding.etCreatedBy.setError("Please enter valid created user");
                } else
                    binding.etResident.setError("Please enter valid resident name");
            }
        });

    }

    private void setFromIntent() {
        userId = getIntent().getExtras().getString("userId");
        userName = getIntent().getExtras().getString("userName");
        //binding.tvResidentName.setText(userName);
    }
}