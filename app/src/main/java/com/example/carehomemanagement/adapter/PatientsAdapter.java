package com.example.carehomemanagement.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.carehomemanagement.R;
import com.example.carehomemanagement.data.model.PatientModel;
import com.example.carehomemanagement.utils.OnItemClickListener;

import java.util.List;

public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.ViewHolder> {

    Context context;
    List<PatientModel> patientModelList;
    Boolean isAdmin;
    OnItemClickListener listener;


    public PatientsAdapter(Context context, List<PatientModel> TempList, OnItemClickListener listener) {
        this.patientModelList = TempList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patients_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PatientModel model = patientModelList.get(position);

        //set data to textview
        holder.name.setText(model.getFirstName() + " " + model.getLastName());
        holder.age.setText("Age : " + model.getAge());
        if (model.getSex().equals("Male"))
            holder.imageView.setImageResource(R.drawable.old_male);
        else
            holder.imageView.setImageResource(R.drawable.old_female);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });


    }

    @Override
    public int getItemCount() {

        return patientModelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView name, age;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.iv_gender);

            name = (TextView) itemView.findViewById(R.id.tv_name);
            age = (TextView) itemView.findViewById(R.id.tv_age);
        }
    }
}

