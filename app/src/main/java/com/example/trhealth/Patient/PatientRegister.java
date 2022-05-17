package com.example.trhealth.Patient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.trhealth.R;
import com.example.trhealth.Utils.Loading_Dialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class PatientRegister extends AppCompatActivity {

    TextInputEditText patientName;
    TextInputEditText patientEmail;
    TextInputEditText patientPassword;
    TextInputEditText patientPhone;
    TextInputEditText patientCnic;
    LinearLayout btnSignUp;
    FirebaseAuth mAuth;
    private String currentUserID;
    private DatabaseReference userRef;
    private ProgressDialog loadingDialog;
    private long idCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register);

        patientName = findViewById(R.id.patientName);
        patientEmail = findViewById(R.id.patientEmail);
        patientPassword = findViewById(R.id.patientPassword);
        patientPhone = findViewById(R.id.patientPhone);
        patientCnic = findViewById(R.id.patientCNIC);
        btnSignUp = findViewById(R.id.btnSignUp);

        mAuth = FirebaseAuth.getInstance();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    idCount = snapshot.getChildrenCount()+1;
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });



    }

    void signup(){
        showLoadingDialog("");
        mAuth.createUserWithEmailAndPassword(patientEmail.getText().toString(), patientPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("FirebaseSignUp","success");
                            mAuth.signInWithEmailAndPassword(patientEmail.getText().toString(), patientPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.i("FirebaseSignIn","success");

                                        currentUserID = mAuth.getCurrentUser().getUid();

                                        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Patients");
                                        HashMap<String, Object> profileMap = new HashMap<>();
                                        profileMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        if (idCount==0){
                                            profileMap.put("id", 1);
                                        }else {
                                            profileMap.put("id", idCount);
                                        }
                                        profileMap.put("name", patientName.getText().toString());
                                        profileMap.put("email", patientEmail.getText().toString());
                                        profileMap.put("phone", patientPhone.getText().toString());
                                        profileMap.put("cnic", patientCnic.getText().toString());
                                        userRef.child(currentUserID).updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = preferences.edit();
                                                    editor.putBoolean("isLoggedIn", true);
                                                    editor.apply();

                                                    hideLoadingDialog();
                                                    startActivity(new Intent(getApplicationContext(),PatientDashboard.class));
                                                } else {
                                                    hideLoadingDialog();
                                                    Toast.makeText(getApplicationContext(), "Error "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                        //  hideLoadingDialog();
                                    } else {
                                        //  hideLoadingDialog();
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            //  hideLoadingDialog();
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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