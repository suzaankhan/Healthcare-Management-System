package com.sem7project.sehatmitr.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sem7project.sehatmitr.R;

import java.util.List;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder> {

    private List<Hospital> hospitalList;
    private Context context;

    public HospitalAdapter(Context context, List<Hospital> hospitalList) {
        this.context = context;
        this.hospitalList = hospitalList;
    }

    @NonNull
    @Override
    public HospitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hospital_card, parent, false);
        return new HospitalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalViewHolder holder, int position) {
        Hospital hospital = hospitalList.get(position);
        holder.hospitalName.setText(hospital.getName());
        holder.hospitalAddress.setText(hospital.getAddress());

        holder.callButton.setOnClickListener(v -> {
            // Add call functionality here
        });

        holder.mapButton.setOnClickListener(v -> {
            // Add map functionality here
        });
    }

    @Override
    public int getItemCount() {
        return hospitalList.size();
    }

    static class HospitalViewHolder extends RecyclerView.ViewHolder {
        TextView hospitalName, hospitalAddress;
        Button callButton, mapButton;

        HospitalViewHolder(@NonNull View itemView) {
            super(itemView);
            hospitalName = itemView.findViewById(R.id.hospital_name);
            hospitalAddress = itemView.findViewById(R.id.hospital_address);
            callButton = itemView.findViewById(R.id.btn_call);
            mapButton = itemView.findViewById(R.id.btn_map);
        }
    }
}

