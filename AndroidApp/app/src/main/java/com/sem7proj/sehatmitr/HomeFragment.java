package com.sem7project.sehatmitr;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sem7project.sehatmitr.utils.HealthRecord;
import com.sem7project.sehatmitr.utils.HealthRecordAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private RecyclerView recyclerViewRecords;
    private HealthRecordAdapter healthRecordAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize recycler viewer
        recyclerViewRecords = view.findViewById(R.id.recycler_view_records);

        // Set up RecycleView properties
        recyclerViewRecords.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initializing adapter with a sample data
        healthRecordAdapter = new HealthRecordAdapter(getContext(), getSampleHealthRecords());
        recyclerViewRecords.setAdapter(healthRecordAdapter);

        return view;
    }

    private List<HealthRecord> getSampleHealthRecords(){

        List<HealthRecord> sampleRecords = new ArrayList<>();

        sampleRecords.add(new HealthRecord("City Hospital",
                "Unit The Umrao Institute Of Medical Science & Research, Mira Road East, Thane - 401107 (Near Railway Station, Opposite Rassaz Mall, Naya Nagar)",
                "2024-01-15", "2024-01-20",
                Arrays.asList("Paracetamol","Ibuprofen"),
                Arrays.asList("Flu", "Cold"),
                "General Ward"));
        sampleRecords.add(new HealthRecord(
                "County Medical Center",
                "J J Marg, Mathar Pakhadi Road, Nagpada-Mumbai Central, Mumbai - 400008 (Near J J Police Station)",
                "2023-12-05",
                "2023-12-10",
                Arrays.asList("Amoxicillin", "Vitamin D","Steroids", "IV"),
                Arrays.asList("Bronchitis", "Asthama"),
                "ICU"
        ));
        sampleRecords.add(new HealthRecord("Wockhart Hospital",
                "Unit The Umrao Institute Of Medical Science & Research, Mira Road East, Thane - 401107 (Near Railway Station, Opposite Rassaz Mall, Naya Nagar)",
                "2024-01-15", "2024-01-20",
                Arrays.asList("Paracetamol","Ibuprofen"),
                Arrays.asList("Flu", "Cold"),
                "General Ward"));
        sampleRecords.add(new HealthRecord("City Hospital",
                "J J Marg, Mathar Pakhadi Road, Nagpada-Mumbai Central, Mumbai - 400008 (Near J J Police Station)",
                "2024-01-15", "2024-01-20",
                Arrays.asList("Paracetamol","Ibuprofen"),
                Arrays.asList("Flu", "Cold", "Tuberculosis"),
                "General Ward"));
        sampleRecords.add(new HealthRecord(
                "County Medical Center",
                "J J Marg, Mathar Pakhadi Road, Nagpada-Mumbai Central, Mumbai - 400008 (Near J J Police Station)",
                "2023-12-05",
                "2023-12-10",
                Arrays.asList("Amoxicillin", "Vitamin D","Steroids", "IV"),
                Arrays.asList("Bronchitis", "Asthama"),
                "ICU"
        ));

        return sampleRecords;
    }
}