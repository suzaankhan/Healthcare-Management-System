package com.sem7project.sehatmitr;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.sem7project.sehatmitr.utils.CustomSpinnerAdapter;
import com.sem7project.sehatmitr.utils.Hospital;
import com.sem7project.sehatmitr.utils.HospitalAdapter;

import java.util.ArrayList;
import java.util.List;

public class LocateHospitalsFragment extends Fragment {

    private ActivityResultLauncher<String[]> locationPermissionLauncher;
    private ActivityResultLauncher<IntentSenderRequest> locationSettingsLauncher;
    private FusedLocationProviderClient fusedLocationClient;

    private String selectedEmergencyCase = "Select Situation";
    private String selectedRadius = "Select Radius";


    private RecyclerView recyclerViewHospitals;
    private Button findHospitalsButton;
    private HospitalAdapter hospitalAdapter;
    private ProgressDialog loadingDialog;
    private List<Hospital> hospitalList = new ArrayList<>(); // This will hold the hospital data

    public LocateHospitalsFragment() {
        // Required empty public constructor
    }

    public static LocateHospitalsFragment newInstance(String param1, String param2) {
        LocateHospitalsFragment fragment = new LocateHospitalsFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void showLoadingDialog() {
        loadingDialog = new ProgressDialog(requireContext());
        loadingDialog.setMessage("Retrieving location...");
        loadingDialog.setCancelable(false); // Prevent dismissal on back press
        loadingDialog.show();
    }

    private void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Request permissions using ActivityResultLauncher
        locationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean fineLocationGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                    Boolean coarseLocationGranted = result.get(Manifest.permission.ACCESS_COARSE_LOCATION);
                    if (fineLocationGranted != null && fineLocationGranted) {
                        checkLocationSettings();
                    } else {
                        Toast.makeText(getContext(), "Location permissions are required to find hospitals", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Initialize ActivityResultLauncher for location settings
        locationSettingsLauncher = registerForActivityResult(
                new ActivityResultContracts.StartIntentSenderForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Toast.makeText(getContext(), "Location is now turned on", Toast.LENGTH_SHORT).show();
                        getUserLocation();
                    } else {
                        Toast.makeText(getContext(), "Location services are required for this feature", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locate_hospitals, container, false);

        Spinner emergencyCaseSpinner = view.findViewById(R.id.emergencyCaseSpinner);
        Spinner radiusSpinner = view.findViewById(R.id.distanceRangeSpinner);
        findHospitalsButton = view.findViewById(R.id.findHospitalsButton);

        // Dropdown for emergency cases
        String[] emergencyCases = getResources().getStringArray(R.array.emergency_cases);
        CustomSpinnerAdapter emergencyAdapter = new CustomSpinnerAdapter(getContext(), emergencyCases);
        emergencyCaseSpinner.setAdapter(emergencyAdapter);

        // Dropdown for radius (range)
        String[] radiusOptions = getResources().getStringArray(R.array.search_radius);
        CustomSpinnerAdapter radiusAdapter = new CustomSpinnerAdapter(getContext(), radiusOptions);
        radiusSpinner.setAdapter(radiusAdapter);

        emergencyCaseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedEmergencyCase = emergencyCases[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        radiusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRadius = radiusOptions[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        requestLocationPermissions();

        // now working will recycle viewer ui
        // Initialize RecyclerView
        recyclerViewHospitals = view.findViewById(R.id.hospitalRecyclerView);
        recyclerViewHospitals.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the adapter
        hospitalAdapter = new HospitalAdapter(getContext(), hospitalList);
        recyclerViewHospitals.setAdapter(hospitalAdapter);

        findHospitalsButton.setOnClickListener(v -> {

            if(selectedEmergencyCase.equals("Select Situation")
                    || selectedRadius.equals("Select Within Range"))
            {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Incomplete details")
                        .setMessage("Please select situation and range")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // nothing to do
                            }
                        })
                        .show();
            }
            else{
                // This will be implemented in the next step
                addSampleHospitals();
                // notify adapter that the data is changed
                hospitalAdapter.notifyDataSetChanged();
                recyclerViewHospitals.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private void addSampleHospitals() {
        hospitalList.clear(); // Clear any previous data
        hospitalList.add(new Hospital("Wockhart Hospital LTD", "The Umrao IMSR Near Railway Station, Mira Road (E) Dist. Thane, Mumbai Maharashtra, India - 401107", "1234567890"));
        hospitalList.add(new Hospital("Masina Hospital trust", "Sant Savata Mali Marg, near Byculla, Mustafa Bazar, Byculla, Mumbai, Maharashtra 400027", "0987654321"));
        hospitalList.add(new Hospital("S L Raheja Hospital", "Raheja Rugnalaya Marg, Mahim West, Mahim, Mumbai, Maharashtra 400016", "1122334455"));
        hospitalList.add(new Hospital("JJ Hospital Mumbai", "Mohammed Ali Rd, Noor Baug, Mazgaon, Mumbai, Maharashtra 400003", "1231231239"));
        hospitalList.add(new Hospital("Saifee Hospital", "Saifee Hospital, Maharshi Karve Rd, opp. Charni Road, Charni Road East, Opera House, Girgaon, Mumbai, Maharashtra 400004", "9999988888"));
    }


    private void requestLocationPermissions() {
        locationPermissionLauncher.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    private void checkLocationSettings() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                .build();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(requireActivity());
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getUserLocation();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        locationSettingsLauncher.launch(new IntentSenderRequest.Builder(resolvable.getResolution()).build());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    Toast.makeText(getContext(), "Location settings are insufficient.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "Permission not granted", Toast.LENGTH_SHORT).show();
            return;
        }

        fusedLocationClient.getCurrentLocation(
                        Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        Toast.makeText(getContext(), "Current location: " + latitude + ", " + longitude, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(), "Unable to retrieve location", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to retrieve location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}

