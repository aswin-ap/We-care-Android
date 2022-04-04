package com.example.carehomemanagement.staff;

import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.example.carehomemanagement.BaseActivity;
import com.example.carehomemanagement.R;
import com.example.carehomemanagement.data.model.PatientModel;
import com.example.carehomemanagement.data.prefrence.SessionManager;
import com.example.carehomemanagement.databinding.ActivityDailyRoutineBinding;
import com.example.carehomemanagement.utils.NetworkManager;
import com.example.carehomemanagement.utils.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class DailyRoutineActivity extends BaseActivity {
    private SessionManager sessionManager;
    private ActivityDailyRoutineBinding binding;
    private ArrayAdapter arrayAdapter;
    String[] quantityArray = {"Select quantity", "All", "3/4", "1/2", "1/4", "None"};
    String breakfastQty, midMorningQty, lunchQty, midAfternoonQty, dinnerQty,
            eveningQty, drinkQty, userId, userName, pickedTime;
    FirebaseFirestore fireStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDailyRoutineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setFromIntent();
        initView();
        checkAvailability();
    }

    private void checkAvailability() {
        if (NetworkManager.isNetworkAvailable(DailyRoutineActivity.this)) {
            showLoading(this);
            fireStore.collection("daily_routine")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            hideLoading();
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    if (documentSnapshot.get("userId").toString().equals(userId) &&
                                    documentSnapshot.get("date").toString().equals(currentDateWithText())) {
                                        showToast(DailyRoutineActivity.this, "Already updated");
                                        finish();
                                    }
                                }
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideLoading();
                    Log.e("exception in request", String.valueOf(e));
                    showToast(DailyRoutineActivity.this, getString(R.string.error));
                }
            });
        }
    }

    private void setFromIntent() {
        userId = getIntent().getExtras().getString("userId");
        userName = getIntent().getExtras().getString("userName");
        binding.tvResidentName.setText(userName);
    }

    private void initView() {
        fireStore = getFireStoreInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                int hour, minute;
                String am_pm;
                if (Build.VERSION.SDK_INT >= 23) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }
                if (hour > 12) {
                    am_pm = "PM";
                    hour = hour - 12;
                } else {
                    am_pm = "AM";
                }
                pickedTime = hour + ":" + minute + " " + am_pm;
                binding.txtTime.setText(pickedTime);
            }
        };
        setupSpinner();
        binding.ivBack.setOnClickListener(view -> {
            this.finish();
        });
        binding.txtDate.setText(currentDateWithText());
        binding.txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(DailyRoutineActivity.this, timeSetListener, 12, 10, false).show();
            }
        });
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.txtTime.getText().toString().equals("Click to Pick time")) {
                    if (Validation.isValidField(binding.etBreakfast.getText().toString()) && Validation.isValidField(breakfastQty)) {
                        if (Validation.isValidField(binding.etMidMorning.getText().toString()) && Validation.isValidField(midMorningQty)) {
                            if (Validation.isValidField(binding.etLunch.getText().toString()) && Validation.isValidField(lunchQty)) {
                                if (Validation.isValidField(binding.etMidAfternoon.getText().toString()) && Validation.isValidField(midAfternoonQty)) {
                                    if (Validation.isValidField(binding.etDinner.getText().toString()) && Validation.isValidField(dinnerQty)) {
                                        if (Validation.isValidField(binding.etEvening.getText().toString()) && Validation.isValidField(eveningQty)) {
                                            if (Validation.isValidField(binding.etDrink.getText().toString()) && Validation.isValidField(drinkQty)) {
                                                updateInfo();
                                            } else
                                                showToast(DailyRoutineActivity.this, "Please enter drink fields");
                                        } else
                                            showToast(DailyRoutineActivity.this, "Please enter evening fields");
                                    } else
                                        showToast(DailyRoutineActivity.this, "Please enter dinner fields");
                                } else
                                    showToast(DailyRoutineActivity.this, "Please enter mid-afternoon fields");
                            } else
                                showToast(DailyRoutineActivity.this, "Please enter lunch fields");
                        } else
                            showToast(DailyRoutineActivity.this, "Please enter mid-Morning fields");
                    } else
                        showToast(DailyRoutineActivity.this, "Please enter breakfast fields");
                } else
                    showToast(DailyRoutineActivity.this, "Please select wakeup time");
            }
        });
    }

    void updateInfo() {
        if (NetworkManager.isNetworkAvailable(DailyRoutineActivity.this)) {
            showLoading(this);

            Map<String, Object> user = new HashMap<>();
            user.put("date", currentDateWithText());
            user.put("userId", userId);
            user.put("wake_up_time", pickedTime);
            user.put("breakfast", binding.etBreakfast.getText().toString());
            user.put("b_qty", breakfastQty);
            user.put("mid_morning", binding.etMidMorning.getText().toString());
            user.put("mid_mrng_qty", midMorningQty);
            user.put("lunch", binding.etLunch.getText().toString());
            user.put("l_qty", lunchQty);
            user.put("mid_afternoon", binding.etMidAfternoon.getText().toString());
            user.put("ma_qty", midAfternoonQty);
            user.put("dinner", binding.etDinner.getText().toString());
            user.put("d_qty", dinnerQty);
            user.put("evening", binding.etEvening.getText().toString());
            user.put("eve_qty", eveningQty);
            user.put("drink", binding.etDrink.getText().toString());
            user.put("drink_qty", drinkQty);
            user.put("description", binding.etDescription.getText().toString());


            FirebaseFirestore fireStoreInstance = getFireStoreInstance();
            fireStoreInstance.collection("daily_routine")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            hideLoading();
                            showToast(DailyRoutineActivity.this, "Updated Successfully");
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideLoading();
                    showToast(DailyRoutineActivity.this, getString(R.string.error));
                }
            });
        } else {
            binding.containerNoInternet.setVisibility(View.VISIBLE);
        }
    }

    private void setupSpinner() {
        arrayAdapter = new ArrayAdapter(DailyRoutineActivity.this, android.R.layout.simple_spinner_item, quantityArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spBreakfast.setAdapter(arrayAdapter);
        binding.spDinner.setAdapter(arrayAdapter);
        binding.spDrink.setAdapter(arrayAdapter);
        binding.spEvening.setAdapter(arrayAdapter);
        binding.spLunch.setAdapter(arrayAdapter);
        binding.spMidAfter.setAdapter(arrayAdapter);
        binding.spMidMorning.setAdapter(arrayAdapter);

        binding.spBreakfast.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();

                if (item == "Select quantity")
                    breakfastQty = "";
                else
                    breakfastQty = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        binding.spMidMorning.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();

                if (item == "Select quantity")
                    midMorningQty = "";
                else
                    midMorningQty = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        binding.spLunch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();

                if (item == "Select quantity")
                    lunchQty = "";
                else
                    lunchQty = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        binding.spMidAfter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();

                if (item == "Select quantity")
                    midAfternoonQty = "";
                else
                    midAfternoonQty = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        binding.spDinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();

                if (item == "Select quantity")
                    dinnerQty = "";
                else
                    dinnerQty = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        binding.spEvening.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();

                if (item == "Select quantity")
                    eveningQty = "";
                else
                    eveningQty = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        binding.spDrink.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();

                if (item == "Select quantity")
                    drinkQty = "";
                else
                    drinkQty = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}