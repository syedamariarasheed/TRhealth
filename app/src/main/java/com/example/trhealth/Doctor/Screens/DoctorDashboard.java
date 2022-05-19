package com.example.trhealth.Doctor.Screens;

import android.content.SharedPreferences;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.trhealth.R;
import com.example.trhealth.SplashScreens.ContinueAs;
import com.example.trhealth.SplashScreens.SplashScreen;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorDashboard extends AppCompatActivity {


    MaterialCardView cvMyAppointments;
    MaterialCardView cvViewPatient;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    String uid;
    TextView tvPatientName;
    TextView tvPatientid;
    ImageView logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        logout = findViewById(R.id.ivLogout);
        cvMyAppointments = findViewById(R.id.cvMyAppointments);
        cvViewPatient = findViewById(R.id.cvViewPatient);
        tvPatientName = findViewById(R.id.tvDoctorName);
        tvPatientid = findViewById(R.id.tvDoctorid);

        databaseReference.child("Doctors").child(uid).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                tvPatientName.setText(snapshot.getValue().toString());
                SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("doctorName", snapshot.getValue().toString());
                editor.apply();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("Doctors").child(uid).child("id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                tvPatientid.setText("ID: " + snapshot.getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cvMyAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MyAppointmentDoctor.class));
            }
        });

        cvViewPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewPatient.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(DoctorDashboard.this, ContinueAs.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}