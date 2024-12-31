package com.sem7project.sehatmitr;
// moving from OTP to main page takes place in the addingNewUser() -> moveFromOtpToMainPage() method
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OtpVerificationPage extends AppCompatActivity {

    // UI variables
    private TextView resendOTP;
    private Button verifyOtpButton;
    private EditText otpInput;
    private TextView otpPageErrorMsg;
    private ProgressBar sending_otp_progress_bar;

    //Other constants
    private final int otp_length = 6;
    Long timeoutSeconds = 60L;
    String verificationCode;
    PhoneAuthProvider.ForceResendingToken resendingToken;  // to resend to user

    // variables to store data from login/register page
    private String recievedUID;
    private String recievedFname;
    private String recievedLname;
    private String recievedDob;
    private String recievedPhoneNumber;
    private String recievedGender;
    private String from;
    private boolean otpBlocked = false;

    FirebaseFirestore firestore;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification_page);

        FirebaseApp.initializeApp(this);  //initialize firebase
        resendOTP = findViewById(R.id.resend_otp);
        verifyOtpButton = findViewById(R.id.verifyotp_button);
        otpInput = findViewById(R.id.otpInput);
        otpPageErrorMsg = findViewById(R.id.otp_page_error_message);
        sending_otp_progress_bar = findViewById(R.id.sending_otp_progress);

        // GETTING DETAILS FROM INTENT
        from = getIntent().getExtras().getString("from");
        if(from.equals("login_page")){
            recievedUID = getIntent().getExtras().getString("uid");
            recievedPhoneNumber = getIntent().getExtras().getString("phoneNumber");
            Toast.makeText(this, "Came from login page" + recievedPhoneNumber, Toast.LENGTH_SHORT).show();
        }
        else if(from.equals("register_page")){
            recievedUID = getIntent().getExtras().getString("uid");
            recievedFname = getIntent().getExtras().getString("fname");
            recievedLname = getIntent().getExtras().getString("lname");
            recievedDob = getIntent().getExtras().getString("dob");
            recievedPhoneNumber = getIntent().getExtras().getString("phoneNumber");
            recievedGender = getIntent().getExtras().getString("gender");
            Toast.makeText(this, "From register page Passed Phone No is " + recievedPhoneNumber, Toast.LENGTH_LONG).show();
        }

        // sending OTP
        sendOtp(recievedPhoneNumber, false);


        verifyOtpButton.setOnClickListener((v) -> {
            sending_otp_progress_bar.setVisibility(View.VISIBLE);
            String otpInputValue = otpInput.getText().toString();
            if(otpInputValue.length() != otp_length){
                otpInput.setError("Enter 6 digit OTP");
                otpPageErrorMsg.setTextColor(ContextCompat.getColor(this, R.color.badmsgcolor));
                otpPageErrorMsg.setText("Enter a 6 digit valid OTP");
                otpPageErrorMsg.setVisibility(View.VISIBLE);
                sending_otp_progress_bar.setVisibility(View.INVISIBLE);
            }
            else{
                otpPageErrorMsg.setTextColor(ContextCompat.getColor(this, R.color.goodmsgcolor));
//                otpPageErrorMsg.setText("OTP is valid");
//                otpPageErrorMsg.setVisibility(View.VISIBLE);
//                sending_otp_progress_bar.setVisibility(View.INVISIBLE);
                // actual code
                if(otpBlocked){
                    Toast.makeText(OtpVerificationPage.this, "OTP blocked so cannot proceed", Toast.LENGTH_SHORT).show();
                }
                else{
                    verifyOtp(otpInputValue);
                }
                // below code to avoid otp verification
//                moveFromOtpToMainPage();
            }
        });

        otpInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                otpPageErrorMsg.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

    }

    void sendOtp(String phoneNumber, boolean isResend){
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(recievedPhoneNumber)
                        .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//                                We dont want automatic detection
                                Toast.makeText(OtpVerificationPage.this, "onVerificationCompleted is called", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(OtpVerificationPage.this, "Some error occurred : " + e.getMessage() , Toast.LENGTH_SHORT).show();
                                otpBlocked = true;
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s;
                                resendingToken = forceResendingToken;
                                Toast.makeText(OtpVerificationPage.this, "OTP sent successfully", Toast.LENGTH_SHORT).show();
                                sending_otp_progress_bar.setVisibility(View.INVISIBLE);
                            }
                        });
        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        }
        else{
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    private void verifyOtp(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
        signInOrRegister(credential);
    }

    void signInOrRegister(PhoneAuthCredential phoneAuthCredential){
        // moving to main activity here
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-in successful, proceed to main activity
                        Toast.makeText(OtpVerificationPage.this, "OTP verified successfully", Toast.LENGTH_SHORT).show();
                        if(from.equals("register_page")){
                            addingNewUser();
                        }
                        else{
                            moveFromOtpToMainPage();
                        }
                    } else {
                        // Sign-in failed, show error message
                        Toast.makeText(OtpVerificationPage.this, "Verification failed", Toast.LENGTH_SHORT).show();
                        sending_otp_progress_bar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    void addingNewUser(){
        firestore = FirebaseFirestore.getInstance();

        Map<String, String> data = new HashMap<>();
        data.put("uid", recievedUID);
        data.put("fname", recievedFname);
        data.put("lname", recievedLname);
        data.put("dob", recievedDob);
        data.put("phoneNumber", recievedPhoneNumber);
        data.put("gender", recievedGender);

        firestore.collection("users")
                .document(recievedUID)
                .set(data)
                .addOnSuccessListener(aVoid -> {
                    moveFromOtpToMainPage();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OtpVerificationPage.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    void moveFromOtpToMainPage(){
        // going to homepage
        Intent otpToMainPage = new Intent(OtpVerificationPage.this, MainActivity.class);

        otpToMainPage.putExtra("uid", recievedUID);
        startActivity(otpToMainPage);
        finish();  // this removes otpPage from back stack ie is user clicks back he wont come to otp page
    }

}