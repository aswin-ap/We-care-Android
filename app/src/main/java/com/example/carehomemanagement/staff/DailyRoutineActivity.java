package com.example.carehomemanagement.staff;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.carehomemanagement.BaseActivity;
import com.example.carehomemanagement.R;
import com.example.carehomemanagement.data.prefrence.SessionManager;
import com.example.carehomemanagement.databinding.ActivityDailyRoutineBinding;
import com.example.carehomemanagement.utils.NetworkManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DailyRoutineActivity extends BaseActivity {
    private SessionManager sessionManager;
    private ActivityDailyRoutineBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDailyRoutineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        binding.txtDate.setText(currentDateWithText());

        binding.txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
            }
        });
    }

    void updateInfo() {
        if (NetworkManager.isNetworkAvailable(DailyRoutineActivity.this)) {
            showLoading(this);

            Map<String, Object> user = new HashMap<>();
            user.put("date", currentDateWithText());
            user.put("userId", sessionManager.getDocumentId());
            user.put("wake_up_time", sessionManager.getUserId());
            user.put("breakfast", sessionManager.getUserId());
            user.put("b_qty", sessionManager.getUserId());
            user.put("mid_morning", sessionManager.getUserId());
            user.put("lunch", sessionManager.getUserId());
            user.put("l_qty", sessionManager.getUserId());
            user.put("mid_afternoon", sessionManager.getUserId());
            user.put("ma_qty", sessionManager.getUserId());
            user.put("dinner", sessionManager.getUserId());
            user.put("d_qty", sessionManager.getUserId());
            user.put("evening", sessionManager.getUserId());
            user.put("eve_qty", sessionManager.getUserId());
            user.put("drink", sessionManager.getUserId());
            user.put("drink_qty", sessionManager.getUserId());
            user.put("description", sessionManager.getUserId());


            FirebaseFirestore fireStoreInstance = getFireStoreInstance();
            fireStoreInstance.collection("daily_routine")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            hideLoading();
                            showToast(DailyRoutineActivity.this, "Updated Successfully");
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
}