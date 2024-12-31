package com.sem7project.sehatmitr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class RegistrationPage extends AppCompatActivity {

    private ImageView dropdownIcon;
    private Spinner genderSpinner;

    private EditText adhaarUID;
    private EditText fname;
    private EditText lname;
    private EditText dateOfBirth;
    private EditText phoneNumber;
    private Button signupButton;
    private TextView registrationErrorMessage;

    private String adhaarUIDvalue;
    private String fnameValue;
    private String lnameValue;
    private String phoneNoValue;
    private String dobValue;
    private String selectedGender = "Select Gender";

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        // Setup for Spinner and accessing UI elements
        dropdownIcon = findViewById(R.id.dropdownIcon);
        adhaarUID = findViewById(R.id.aadhaar_input);
        fname = findViewById(R.id.first_name);
        lname = findViewById(R.id.last_name);
        dateOfBirth =  findViewById(R.id.date_of_birth);
        phoneNumber = findViewById(R.id.phone_number);
        genderSpinner = findViewById(R.id.spinnerGender);
        signupButton = findViewById(R.id.login_button);
        registrationErrorMessage = findViewById(R.id.error_message);

        EditText fieldArray[] = {adhaarUID, fname, lname, dateOfBirth, phoneNumber};

        dateOfBirth.setOnClickListener(v -> showDatePicker());

        signupButton.setOnClickListener((v) -> {
            adhaarUIDvalue = adhaarUID.getText().toString().trim();
            adhaarUIDvalue = adhaarUIDvalue.replace(" ", "");
            fnameValue = fname.getText().toString().trim();
            lnameValue = lname.getText().toString().trim();
            dobValue = dateOfBirth.getText().toString().trim();
            phoneNoValue = phoneNumber.getText().toString().trim();
            phoneNoValue = phoneNoValue.replace(" ", ""); // removing the middle space
            phoneNoValue = "+91" + phoneNoValue;
            String[] details = {adhaarUIDvalue, fnameValue, lnameValue, dobValue, phoneNoValue, selectedGender};

//            verifyDetails(details);
            if(!verifyDetails(details)){
                registrationErrorMessage.setVisibility(View.VISIBLE);
                registrationErrorMessage.setText("PLease enter valid details");
            }
            else{
                registrationErrorMessage.setVisibility(View.VISIBLE);
                registrationErrorMessage.setText("Your Details are valid");
                // further process begins here (for now go to OTP page)
                Intent intent = new Intent(RegistrationPage.this, OtpVerificationPage.class);
                intent.putExtra("from", "register_page");
                intent.putExtra("uid", adhaarUIDvalue);
                intent.putExtra("fname", fnameValue);
                intent.putExtra("lname", lnameValue);
                intent.putExtra("dob", dobValue);
                intent.putExtra("phoneNumber", phoneNoValue);
                intent.putExtra("gender", selectedGender);
                startActivity(intent);
            }
        });

        for(EditText field : fieldArray){
            field.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    registrationErrorMessage.setVisibility(View.INVISIBLE);

                }
                @Override
                public void afterTextChanged(Editable editable) {}
            });
        }
        // for PHONE NUMBER FORMAT - 99999 88888
        phoneNumber.addTextChangedListener(new TextWatcher() {
            private static final String SPACE = " ";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();

                // Remove spaces to avoid adding extra spaces while formatting
                if (text.length() > 0 && text.contains(SPACE)) {
                    text = text.replace(SPACE, "");
                }
                // Apply formatting: Add a space after the first 5 digits
                if (text.length() > 5) {
                    text = text.substring(0, 5) + SPACE + text.substring(5);
                }
                // Update the EditText with the formatted text
                phoneNumber.removeTextChangedListener(this);  // Prevent infinite loop
                phoneNumber.setText(text);
                phoneNumber.setSelection(text.length());      // Move cursor to the end
                phoneNumber.addTextChangedListener(this);
            }
        });
        // for ADHAAR FORMAT - 1111 2222 3333
        adhaarUID.addTextChangedListener(new TextWatcher() {
            private static final String FORMAT = "#### #### ####";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String current = s.toString().replace(" ", "");
                if (current.length() <= 12) {
                    StringBuilder formatted = new StringBuilder();
                    for (int i = 0; i < current.length(); i++) {
                        if (i % 4 == 0 && i > 0) {
                            formatted.append(" ");
                        }
                        formatted.append(current.charAt(i));
                    }
                    adhaarUID.removeTextChangedListener(this);
                    adhaarUID.setText(formatted.toString());
                    adhaarUID.setSelection(formatted.length());
                    adhaarUID.addTextChangedListener(this);
                }
            }
        });





        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, getResources().getTextArray(R.array.gender_options)) {
            @Override
            public boolean isEnabled(int position) {
                // Disable the first item (Select Gender) from being selected
                return position != 0; // Disable item at position 0
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                // Set the disabled option color to gray to indicate it's not selectable
                if (position == 0) {
                    tv.setTextColor(Color.GRAY); // Color for disabled item
                } else {
                    tv.setTextColor(Color.BLACK); // Color for enabled items
                }
                return view;
            }
        };

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        genderSpinner.setAdapter(adapter);

        // Set an onClickListener to change the arrow to "drop-up" when the spinner is clicked
        genderSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Change icon to "drop-up" when spinner is touched
                    dropdownIcon.setImageResource(R.drawable.ic_arrow_drop_up);
                }
                return false; // Allow spinner to also open the dropdown
            }
        });

        // Set an onItemSelectedListener to change the arrow back to "drop-down" when an item is selected
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedGender = parentView.getItemAtPosition(position).toString();
                registrationErrorMessage.setVisibility(View.INVISIBLE);
                genderSpinner.setBackgroundResource(R.drawable.rounded_edit_text);
                if (!selectedGender.equals("Select Gender")) {
                    Toast.makeText(RegistrationPage.this, "Selected: " + selectedGender, Toast.LENGTH_SHORT).show();
                }
                // Change the arrow back to "drop-down" when an option is selected
                dropdownIcon.setImageResource(R.drawable.ic_arrow_drop_down);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle when no item is selected (optional)
                dropdownIcon.setImageResource(R.drawable.ic_arrow_drop_down);
            }
        });
    }

    private void showDatePicker() {
        // Get current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Check if the selected date is in the future
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);
                    if (selectedDate.after(Calendar.getInstance())) {
                        Toast.makeText(this, "Future dates are not allowed.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Set the selected date to the EditText
                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        dateOfBirth.setText(date);
                    }
                }, year, month, day);

        // Prevent selecting future dates
        dateOfBirth.setError(null);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
    }


//        String[] details = {adhaarUIDvalue, fnameValue, lnameValue, dobValue, phoneNoValue, selectedGender};
        private boolean verifyDetails(String[] details){
            int status = 0;

            if (details[0].length() != 12) {  // adhaar uid
                adhaarUID.setError("Enter 12 digit UID");
                status++;
            }

            if (details[1].isEmpty()) { //fname
                fname.setError("Fill this field");
                status++;
            }
            if (details[2].isEmpty()) { //lname
                lname.setError("Fill this field");
                status++;
            }
            if (details[3].isEmpty()) { //DOB
                dateOfBirth.setError("Fill this field");
                status++;
            }
            if (details[4].length() != 13) { // phone number
                phoneNumber.setError("Enter 10 digit phone number");
                status++;
            }
            if (details[5].equals("Select Gender")) { // gender
                genderSpinner.setBackgroundResource(R.drawable.rounded_edit_text_error);
                status++;
            }

            return status == 0;
        }

}