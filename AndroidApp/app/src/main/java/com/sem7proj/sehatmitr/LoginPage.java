package com.sem7project.sehatmitr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginPage extends AppCompatActivity {

    private TextView registerNowText;
    private TextView loginErrorMessage;
    private EditText loginAdhaarUIDInput;
    private Button loginButton;
    private ProgressBar loginProgressBar;
    private String adhaarUidValue;

    private int uid_length = 12;
    private int originalTextColor;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        FirebaseApp.initializeApp(this); // initializing firebase
        firestore = FirebaseFirestore.getInstance();

        // Writing code here
        // Getting access to UI elements
        loginButton = findViewById(R.id.login_button);
        registerNowText = findViewById(R.id.signup_link_clickable);
        loginAdhaarUIDInput = findViewById(R.id.aadhaar_input);
        loginErrorMessage = findViewById(R.id.error_message);
        loginProgressBar = findViewById(R.id.login_progress_bar);

        // orignal text color
        originalTextColor = registerNowText.getCurrentTextColor();

        // action when login button is clicked------------------------------------------------------------------------------------
        loginButton.setOnClickListener((v) -> {
            adhaarUidValue = loginAdhaarUIDInput.getText().toString();
            adhaarUidValue = adhaarUidValue.replace(" ", "");
            loginProgressBar.setVisibility(View.VISIBLE);
//            loginAdhaarUIDInput.getText().toString().length()
            if(adhaarUidValue.length() != uid_length){
                loginProgressBar.setVisibility(View.INVISIBLE);
                loginErrorMessage.setVisibility(View.VISIBLE);
                loginErrorMessage.setText("Please enter a valid UID");
                loginAdhaarUIDInput.setError("UID must be 12 digit");
            }
            else{
                checkIfUserExists(adhaarUidValue, new FirestoreCallback() {
//                    String userPhoneNumber;
                    @Override
                    public void onCallback(boolean userExists, String error, String userPhoneNumber) {
                        if(error != null){
                            loginErrorMessage.setVisibility(View.VISIBLE);
                            loginErrorMessage.setText(error);
                        }
                        else if(userExists){
                            // VERIFY USER HERE THEN MOVE TO OTP
                            Intent movingToOtpFromLogin = new Intent(LoginPage.this, OtpVerificationPage.class);
                            movingToOtpFromLogin.putExtra("from", "login_page");
                            movingToOtpFromLogin.putExtra("uid", adhaarUidValue);
                            movingToOtpFromLogin.putExtra("phoneNumber", userPhoneNumber);
                            startActivity(movingToOtpFromLogin);
                        }
                        else{
                            loginErrorMessage.setVisibility(View.VISIBLE);
                            loginErrorMessage.setText("No such user found");
                        }
                        loginProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

        loginAdhaarUIDInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed here
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Make the error message invisible when the user types
//                loginErrorMessage.setText("");
                loginErrorMessage.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable s) {
                // No action needed here
            }
        });

        // Code related to login-----------------------------------------------------------------------------------------------------

        // Code TO GO TO REGISTER PAGE-----------------------------------------------------------------------------------------------------

        //setting an on click listener on button
        registerNowText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // change text color to blue
                registerNowText.setTextColor(Color.parseColor("#000980"));

                // intent to navigate to registration page
                Intent intent = new Intent(LoginPage.this, RegistrationPage.class);
                startActivity(intent);
            }
        });
        //----------------------------------------------------------------------------------------------------------------------

        // for ADHAAR FORMAT - 1111 2222 3333
        loginAdhaarUIDInput.addTextChangedListener(new TextWatcher() {
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
                    loginAdhaarUIDInput.removeTextChangedListener(this);
                    loginAdhaarUIDInput.setText(formatted.toString());
                    loginAdhaarUIDInput.setSelection(formatted.length());
                    loginAdhaarUIDInput.addTextChangedListener(this);
                }
            }
        });
    }

    // interface for callback
    public interface FirestoreCallback{
        void onCallback(boolean userExists, String error, String userPhoneNumber);
    }

    private void checkIfUserExists(String uid, FirestoreCallback callback){
        DocumentReference docRef = firestore.collection("users").document(uid);
        // perform read operation
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    // User with given uid exists
                    String userPhoneNumber = documentSnapshot.getString("phoneNumber");
                    callback.onCallback(true, null, userPhoneNumber);
                }
                else{
                    // User does not exist
                    callback.onCallback(false, null, null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onCallback(false, "Some error occurred", null);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reset the text color to the original color
        registerNowText.setTextColor(originalTextColor);
    }

}