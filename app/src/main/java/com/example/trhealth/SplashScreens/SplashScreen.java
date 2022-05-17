package com.example.trhealth.SplashScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.trhealth.Doctor.Screens.DoctorDashboard;
import com.example.trhealth.Patient.PatientDashboard;
import com.example.trhealth.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {


    DatabaseReference databaseReference;

    private boolean isFirstAnimation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        init();

         /*Simple hold animation to hold ImageView in the centre of the screen at a slightly larger
        scale than the ImageView's original one.*/
        Animation hold = AnimationUtils.loadAnimation(this, R.anim.hold);

        /* Translates ImageView from current position to its original position, scales it from
        larger scale to its original scale.*/
        final Animation translateScale = AnimationUtils.loadAnimation(this, R.anim.translate_scale);

        final ImageView imageView = findViewById(R.id.header_icon);

        translateScale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!isFirstAnimation) {
                    imageView.clearAnimation();
                    if (getSharedPreferences("prefs", MODE_PRIVATE).getBoolean("isDoctorLoggedIn", false)) {
                        Intent intent = new Intent(SplashScreen.this, DoctorDashboard.class);
                        startActivity(intent);
                    } else if (getSharedPreferences("prefs", MODE_PRIVATE).getBoolean("isPatientLoggedIn", false)) {
                        Intent intent = new Intent(SplashScreen.this, PatientDashboard.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashScreen.this, ContinueAs.class);
                        startActivity(intent);
                    }
                    finish();
                }

                isFirstAnimation = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        hold.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.clearAnimation();
                imageView.startAnimation(translateScale);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imageView.startAnimation(hold);
    }

    public void init() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("status");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Boolean.class) == false) {
                    System.exit(0);
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}