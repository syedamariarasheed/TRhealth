package com.example.trhealth.Patient;

import android.content.SharedPreferences;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.trhealth.Doctor.Screens.DoctorDashboard;
import com.example.trhealth.R;
import com.example.trhealth.SplashScreens.ContinueAs;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PatientDashboard extends AppCompatActivity {

    MaterialCardView cv_addReports;
    MaterialCardView cvMyReports;
    MaterialCardView cvBookAppointments;
    MaterialCardView cvMyAppointments;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    String uid;
    TextView tvPatientName;
    TextView tvPatientid;
    ImageView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        uid= mAuth.getCurrentUser().getUid();

        logout = findViewById(R.id.ivLogout);
        cv_addReports = findViewById(R.id.cv_addReports);
        cvMyReports = findViewById(R.id.cvMyReports);
        cvBookAppointments = findViewById(R.id.cvBookAppointments);
        cvMyAppointments = findViewById(R.id.cvMyAppointments);
        tvPatientName = findViewById(R.id.tvPatientName);
        tvPatientid = findViewById(R.id.tvPatientid);
        databaseReference.child("Patients").child(uid).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                tvPatientName.setText(snapshot.getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("Patients").child(uid).child("id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                tvPatientid.setText("ID: "+snapshot.getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        cv_addReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientDashboard.this,ScanReportActivity.class);
                startActivity(intent);
            }
        });
        cvMyReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientDashboard.this,MyReports.class);
                startActivity(intent);
            }
        });

        cvBookAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientDashboard.this,BookAppointment.class);
                startActivity(intent);
            }
        });

        cvMyAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientDashboard.this,MyAppointments.class);
                startActivity(intent);
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
                Intent intent = new Intent(PatientDashboard.this, ContinueAs.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}