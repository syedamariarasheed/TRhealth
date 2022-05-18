package com.example.trhealth.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ListView;

import com.example.trhealth.R;
import com.example.trhealth.adapter.DoctorsRecyclerView;
import com.example.trhealth.adapter.ReportsRecyclerView;
import com.example.trhealth.model.Doctors;
import com.example.trhealth.model.Reports;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MyReports extends AppCompatActivity {

    ListView rvMyReports;
    RecyclerView recyclerView;
    ReportsRecyclerView adapter;

    ArrayList<Reports> reportsArrayList;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    FirebaseAuth mAuth;

    String patientId = null;
    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reports);

        rvMyReports = findViewById(R.id.rvMyReports);
        recyclerView = findViewById(R.id.reportsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        if (getIntent().getStringExtra("id") != null) {
            id = getIntent().getStringExtra("id");
        } else {
            id = FirebaseAuth.getInstance().getUid();
        }
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        patientId = mAuth.getUid();


        reportsArrayList = new ArrayList<>();
        adapter = new ReportsRecyclerView(patientId, this, reportsArrayList);
        recyclerView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Reports").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        reportsArrayList.add(new Reports(ds.getKey(), ds.getValue(String.class), dataSnapshot.getKey()));
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}