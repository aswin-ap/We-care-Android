package com.example.carehomemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.carehomemanagement.data.prefrence.SessionManager;
import com.example.carehomemanagement.databinding.ActivityLoginBinding;
import com.example.carehomemanagement.manager.MHomeActivity;
import com.example.carehomemanagement.staff.SHomeActivity;
import com.example.carehomemanagement.utils.NetworkManager;
import com.example.carehomemanagement.utils.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private long mLastClickTime = 0;
    private final FirebaseFirestore fb = getFireStoreInstance();
    private boolean isMatch;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManager = new SessionManager(LoginActivity.this);
        initView();
    }

    private void initView() {

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validation.isValidEmail(binding.txtEmail.getText().toString())) {
                    if (!binding.txtPass.getText().toString().isEmpty()) {
                        login(binding.txtEmail.getText().toString(), binding.txtPass.getText().toString());
                    } else
                        binding.txtPass.setError("Please enter valid password");
                } else
                    binding.txtEmail.setError("Please enter valid email");
            }
        });
    }

    private void login(String userName, String passWord) {
        if (NetworkManager.isNetworkAvailable(LoginActivity.this)) {
            binding.containerNoInternet.setVisibility(View.GONE);
            showLoading(this);
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            fb.collection("user")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                hideLoading();
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    if (documentSnapshot.get("email").toString().equals(userName) &&
                                            documentSnapshot.get("password").toString().equals(passWord)) {

                                        isMatch = true;
                                        sessionManager.setDocumentId(documentSnapshot.getId());
                                        sessionManager.setUserName(documentSnapshot.get("name").toString());
                                        sessionManager.setUserTypeId(documentSnapshot.get("type").toString());
                                        sessionManager.setLogin(true);
                                        Intent intent;
                                        if (documentSnapshot.get("type").toString().equals("m")) {
                                            intent = new Intent(LoginActivity.this, MHomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            showToast(LoginActivity.this, "Login Successfully");
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            intent = new Intent(LoginActivity.this, SHomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            showToast(LoginActivity.this, "Login Successfully");
                                            startActivity(intent);
                                            finish();
                                        }


                                    } else
                                        isMatch = false;
                                }

                                if (!isMatch)
                                    showToast(LoginActivity.this, "Enter valid user details");
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideLoading();
                    showToast(LoginActivity.this, getString(R.string.error));
                }
            });
        } else
            binding.containerNoInternet.setVisibility(View.VISIBLE);
        // showSnackBar(binding.getRoot(), getString(R.string.check_internet));
    }
}