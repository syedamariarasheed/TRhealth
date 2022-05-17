package com.example.trhealth.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.trhealth.R;
import com.example.trhealth.adapter.PatientNotificationAdapter;
import com.example.trhealth.model.PatientNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class MyAppointments extends AppCompatActivity {


    ListView rvMyReports;

    PatientNotificationAdapter myReportsAdapter;
    List<PatientNotification> patientNotificationsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointments);
        rvMyReports = findViewById(R.id.rvMyReports);
        patientNotificationsList = new ArrayList<>();


        DatabaseReference nm = FirebaseDatabase.getInstance().getReference("Patient_Notification").child(FirebaseAuth.getInstance().getUid());
        nm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        PatientNotification post = snapshot.getValue(PatientNotification.class);
                        patientNotificationsList.add(post);
                    }
                    myReportsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        myReportsAdapter = new PatientNotificationAdapter(MyAppointments.this, patientNotificationsList);
        rvMyReports.setAdapter(myReportsAdapter);
    }
}