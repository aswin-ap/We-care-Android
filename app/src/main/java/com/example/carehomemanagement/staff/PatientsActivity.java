package com.example.carehomemanagement.staff;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.carehomemanagement.BaseActivity;
import com.example.carehomemanagement.R;
import com.example.carehomemanagement.adapter.PatientsAdapter;
import com.example.carehomemanagement.data.model.PatientModel;
import com.example.carehomemanagement.databinding.ActivityPatientsBinding;
import com.example.carehomemanagement.utils.NetworkManager;
import com.example.carehomemanagement.utils.OnItemClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PatientsActivity extends BaseActivity {
    private ActivityPatientsBinding binding;
    private PatientsAdapter adapter;
    private List<PatientModel> patientModels = new ArrayList();
    private FirebaseFirestore fireStore = getFireStoreInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatientsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        getPatients();
    }

    private void initView() {
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        adapter = new PatientsAdapter(this, patientModels, new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                PatientModel model = patientModels.get(position);
            }
        });
        binding.rvPatients.setHasFixedSize(true);
        binding.rvPatients.setAdapter(adapter);
    }

    private void getPatients() {
        if (NetworkManager.isNetworkAvailable(PatientsActivity.this)) {
            showLoading(this);
            fireStore.collection("patients")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            hideLoading();
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    PatientModel model = new PatientModel();
                                    model.setDocumentId(documentSnapshot.getId());
                                    model.setFirstName(documentSnapshot.getString("firstname"));
                                    model.setLastName(documentSnapshot.getString("lastname"));
                                    model.setAge(documentSnapshot.getString("age"));
                                    model.setSex(documentSnapshot.getString("sex"));

                                    patientModels.add(model);
                                }
                                updateRecyclerView();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideLoading();
                    Log.e("exception in request", String.valueOf(e));
                    showToast(PatientsActivity.this, getString(R.string.error));
                }
            });
        } else
            binding.containerNoInternet.setVisibility(View.VISIBLE);
        // showSnackBar(binding.getRoot(), getString(R.string.check_internet));

    }

    private void updateRecyclerView() {
        if (patientModels.size() > 0) {
            binding.ivNoData.setVisibility(View.GONE);
            binding.rvPatients.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        } else {
            binding.ivNoData.setVisibility(View.VISIBLE);
            binding.rvPatients.setVisibility(View.GONE);
            showToast(this, "No patients available");
        }
    }
}