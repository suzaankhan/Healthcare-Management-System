package com.sem7project.sehatmitr.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sem7project.sehatmitr.R;

import java.util.List;
import java.util.stream.Collectors;

public class HealthRecordAdapter extends RecyclerView.Adapter<HealthRecordAdapter.HealthRecordViewHolder> {

    private List<HealthRecord> healthRecordList;
    private Context context;

    public HealthRecordAdapter(Context context, List<HealthRecord> healthRecordList) {
        this.context = context;
        this.healthRecordList = healthRecordList;
    }

    @NonNull
    @Override
    public HealthRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.health_record_card, parent, false);
        return new HealthRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HealthRecordViewHolder holder, int position) {
        HealthRecord record = healthRecordList.get(position);
        holder.hospitalName.setText(record.getHospitalName());
        holder.hospitalAddress.setText(record.getHospitalAddress());
        holder.admissionDate.setText("Admission Date: " + record.getAdmissionDate());
        holder.dischargeDate.setText("Discharge Date: " + record.getDischargeDate());

        // Join lists with commas for display
        holder.medicines.setText("Medicines: " + String.join(", ", record.getMedicines()));
        holder.diseases.setText("Diseases: " + String.join(", ", record.getDiseases()));

        holder.wardType.setText("Ward: " + record.getWardType());

        // Placeholder for the "View X-rays and CT scans" clickable text
        holder.viewScans.setOnClickListener(v -> {
            // Future functionality: open X-rays and CT scans
        });
    }

    @Override
    public int getItemCount() {
        return healthRecordList.size();
    }

    static class HealthRecordViewHolder extends RecyclerView.ViewHolder {
        TextView hospitalName, hospitalAddress, admissionDate, dischargeDate, medicines, diseases, wardType, viewScans;

        HealthRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            hospitalName = itemView.findViewById(R.id.record_hospital_name);
            hospitalAddress = itemView.findViewById(R.id.record_hospital_address);
            admissionDate = itemView.findViewById(R.id.record_admission_date);
            dischargeDate = itemView.findViewById(R.id.record_discharge_date);
            medicines = itemView.findViewById(R.id.record_medicines);
            diseases = itemView.findViewById(R.id.record_diseases);
            wardType = itemView.findViewById(R.id.record_ward_type);
            viewScans = itemView.findViewById(R.id.record_view_scans);
        }
    }
}
