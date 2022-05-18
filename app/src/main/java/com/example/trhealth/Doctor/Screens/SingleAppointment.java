package com.example.trhealth.Doctor.Screens;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.trhealth.Patient.MyAppointmentModel;
import com.example.trhealth.Patient.MyReports;
import com.example.trhealth.Patient.MyReportsAdapter;
import com.example.trhealth.Patient.MyReportsModel;
import com.example.trhealth.R;
import com.example.trhealth.adapter.ReportsRecyclerView;
import com.example.trhealth.model.AppointmentStatus;
import com.example.trhealth.model.PatientNotification;
import com.example.trhealth.model.Reports;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SingleAppointment extends AppCompatActivity {

    RecyclerView rvMyReports;
    ReportsRecyclerView myReportsAdapter;
    ArrayList<Reports> myReportsModelList;
    AppCompatButton btnAccept;
    AppCompatButton btnDecline;
    AppCompatButton btnPresciption;
    TextView tvName;
    TextView tvId, dateTime;
    MyAppointmentModel appointmentModel;
    String patientNotificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_appointment);
        tvId = findViewById(R.id.tvId);
        dateTime = findViewById(R.id.dateTime);
        tvName = findViewById(R.id.tvName);
        rvMyReports = findViewById(R.id.rvMyReports);
        rvMyReports.setHasFixedSize(true);
        rvMyReports.setLayoutManager(new LinearLayoutManager(this));
        btnPresciption = findViewById(R.id.btnPresciption);
        btnAccept = findViewById(R.id.btnAccept);
        btnDecline = findViewById(R.id.btnDecline);

        appointmentModel = (MyAppointmentModel) getIntent().getSerializableExtra("appointmentDetails");

        getReports();

        initUI();

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatus(AppointmentStatus.ACCEPTED);
                notifyPatient(AppointmentStatus.ACCEPTED);
                acceptedUI();
            }
        });

        btnPresciption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WritePresciption.class);
                intent.putExtra("patient_id", appointmentModel.getPatientId());
                intent.putExtra("patientNotificationId", patientNotificationId);
                startActivity(intent);
            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus(AppointmentStatus.DECLINED);
                notifyPatient(AppointmentStatus.DECLINED);
                declinedUI();
            }
        });

    }

    private void getReports() {
        myReportsModelList = new ArrayList<>();
        myReportsAdapter = new ReportsRecyclerView(appointmentModel.getPatientId(),this, myReportsModelList);
        rvMyReports.setAdapter(myReportsAdapter);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Reports").child(appointmentModel.getPatientId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        myReportsModelList.add(new Reports(ds.getKey(),ds.getValue(String.class),dataSnapshot.getKey()));
                    }

                }
                myReportsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void notifyPatient(AppointmentStatus status) {
        SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
        patientNotificationId = appointmentModel.getPatientId() + UUID.randomUUID().toString();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Patient_Notification");
        mDatabase.child(appointmentModel.getPatientId()).child(patientNotificationId).setValue(new PatientNotification(status.name(), FirebaseAuth.getInstance().getUid(),
                preferences.getString("doctorName","not mentioned"), ""));
    }

    private void initUI() {
        if (appointmentModel.getAppointmentStatus().equals(AppointmentStatus.ACCEPTED.name())) {
            acceptedUI();
        } else if (appointmentModel.getAppointmentStatus().equals(AppointmentStatus.DECLINED.name())) {
            declinedUI();
        }
        if (appointmentModel != null) {
            tvName.setText(appointmentModel.getPatientName());
            tvId.setText(appointmentModel.getPatientFriendlyId());
            dateTime.setText(appointmentModel.getAppointmentDate() + " " + appointmentModel.getAppointmentTime());
        }
    }

    private void declinedUI() {
        btnDecline.setEnabled(false);
        btnAccept.setVisibility(View.GONE);
        btnDecline.setVisibility(View.VISIBLE);
        btnPresciption.setVisibility(View.GONE);
    }

    private void acceptedUI() {
        btnAccept.setVisibility(View.GONE);
        btnDecline.setVisibility(View.GONE);
        btnPresciption.setVisibility(View.VISIBLE);
    }

    private void updateStatus(AppointmentStatus status) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Booking");
        mDatabase.child(FirebaseAuth.getInstance().getUid()).child(appointmentModel.getAppointmentId()).child("appointmentStatus").setValue(status.name());
    }
}