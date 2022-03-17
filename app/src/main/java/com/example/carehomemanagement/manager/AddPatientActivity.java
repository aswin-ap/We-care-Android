package com.example.carehomemanagement.manager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.example.carehomemanagement.BaseActivity;
import com.example.carehomemanagement.data.prefrence.SessionManager;
import com.example.carehomemanagement.databinding.ActivityAddPatientBinding;
import com.example.carehomemanagement.utils.NetworkManager;
import com.example.carehomemanagement.utils.Validation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddPatientActivity extends BaseActivity {
    private ActivityAddPatientBinding binding;
    private FirebaseFirestore fb;
    private SessionManager sessionManager;
    final Calendar myCalendar = Calendar.getInstance();
    private String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPatientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        fb = getFireStoreInstance();
        sessionManager = new SessionManager(this);
        binding.imageClose.setOnClickListener(v -> onBackPressed());
        DatePickerDialog.OnDateSetListener dateDob = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateDob();
            }
        };
        DatePickerDialog.OnDateSetListener dateDoj = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateDoj();
            }
        };
        binding.rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton button = findViewById(i);
                if (button.getText().equals("Male"))
                    sex = "Male";
                else
                    sex = "Female";
            }
        });

        binding.editDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddPatientActivity.this, dateDob, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        binding.editDateOfJoining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddPatientActivity.this, dateDoj, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validation.isValidField(binding.editFirstName.getText().toString())) {
                    if (Validation.isValidField(binding.editLastName.getText().toString())) {
                        if (Validation.isValidField(binding.editAddress.getText().toString())) {
                            if (Validation.isValidField(binding.editDob.getText().toString())) {
                                if (Validation.isValidField(binding.editAge.getText().toString())) {
                                    if (sex != null && Validation.isValidField(sex)) {
                                        if (Validation.isValidField(binding.editDateOfJoining.getText().toString())) {
                                            if (Validation.isValidField(binding.editNextOfKin.getText().toString())) {
                                                if (Validation.isValidField(binding.editRelationship.getText().toString())) {
                                                    if (Validation.isValidField(binding.editContact.getText().toString())) {
                                                        if (Validation.isValidField(binding.editMedication.getText().toString())) {
                                                            registerPatient();
                                                        } else
                                                            binding.editMedication.setError("Please enter medication");
                                                    } else
                                                        binding.editContact.setError("Please enter valid contact number");
                                                } else
                                                    binding.editRelationship.setError("Please enter relationship with kin");
                                            } else
                                                binding.editNextOfKin.setError("Please enter valid kin");
                                        } else
                                            showToast(AddPatientActivity.this, "Please select date of joining");

                                    } else
                                        showToast(AddPatientActivity.this, "Please select sex");
                                } else
                                    binding.editAge.setError("Please enter age");
                            } else
                                showToast(AddPatientActivity.this, "Please select date of birth");
                        } else
                            binding.editAddress.setError("Please enter valid address");
                    } else
                        binding.editLastName.setError("Please enter valid name");
                } else
                    binding.editFirstName.setError("Please enter valid name");
            }
        });
    }

    private void updateDob() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        binding.editDob.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void updateDoj() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        binding.editDateOfJoining.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void registerPatient() {
        if (NetworkManager.isNetworkAvailable(AddPatientActivity.this)) {
            showLoading(this);

            Map<String, Object> patient = new HashMap<>();
            patient.put("firstname", binding.editFirstName.getText().toString());
            patient.put("lastname", binding.editLastName.getText().toString());
            patient.put("address", binding.editAddress.getText().toString());
            patient.put("dob", binding.editDob.getText().toString());
            patient.put("age", binding.editAge.getText().toString());
            patient.put("sex", sex);
            patient.put("doj", binding.editDateOfJoining.getText().toString());
            patient.put("next_kin", binding.editNextOfKin.getText().toString());
            patient.put("rel_kin", binding.editRelationship.getText().toString());
            patient.put("contact", binding.editContact.getText().toString());
            patient.put("medication", binding.editMedication.getText().toString());
            patient.put("notes", binding.editNotes.getText().toString());

            FirebaseFirestore fireStoreInstance = getFireStoreInstance();
            fireStoreInstance.collection("patients")
                    .add(patient)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            hideLoading();
                            showToast(AddPatientActivity.this, "Added Successfully");
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideLoading();
                    showToast(AddPatientActivity.this, e.getMessage());
                }
            });
        } else {
            binding.containerNoInternet.setVisibility(View.VISIBLE);
        }
    }
}