package com.example.trhealth.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.trhealth.R;
import com.example.trhealth.Utils.Loading_Dialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class PatientSignIn extends AppCompatActivity {

    LinearLayout btnSignUp;
    LinearLayout btnSignIn;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingDialog;
    TextInputEditText tvEmail,tvPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_sign_in);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvPass = findViewById(R.id.tvPass);
        tvEmail = findViewById(R.id.tvEmail);

        mAuth = FirebaseAuth.getInstance();


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),PatientRegister.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoadingDialog("");
                if(!TextUtils.isEmpty(tvEmail.getText().toString()) && !TextUtils.isEmpty(tvPass.getText().toString())){
                    mAuth.signInWithEmailAndPassword(tvEmail.getText().toString(),tvPass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("isPatientLoggedIn", true);
                            editor.apply();
                            hideLoadingDialog();
                            startActivity(new Intent(getApplicationContext(),PatientDashboard.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PatientSignIn.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            hideLoadingDialog();
                        }
                    });
                }else {
                    Toast.makeText(PatientSignIn.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void showLoadingDialog(String message) {
        loadingDialog = Loading_Dialog.ctor(this);
        loadingDialog.setMessage("");
        loadingDialog.show();
    }
    protected void hideLoadingDialog() {
        loadingDialog.dismiss();

    }
}