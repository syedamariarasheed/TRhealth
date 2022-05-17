package com.example.trhealth.SplashScreens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.trhealth.Admin.AdminSignIn;
import com.example.trhealth.Doctor.Screens.DoctorSignIn;
import com.example.trhealth.Patient.PatientSignIn;
import com.example.trhealth.R;
import com.google.android.material.card.MaterialCardView;

public class ContinueAs extends AppCompatActivity {

    MaterialCardView cvDoctor;
    MaterialCardView cvPatient;
    MaterialCardView cvAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_as);
        overridePendingTransition(0,0);
        View relativeLayout=findViewById(R.id.login_container);
        Animation animation= AnimationUtils.loadAnimation(this,android.R.anim.fade_in);
        relativeLayout.startAnimation(animation);


        cvDoctor = findViewById(R.id.cvDoctor);
        cvDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DoctorSignIn.class));
            }
        });

        cvPatient = findViewById(R.id.cvPatient);
        cvPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PatientSignIn.class));
            }
        });

        cvAdmin = findViewById(R.id.cvAdmin);
        cvAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AdminSignIn.class));
            }
        });
    }
}