package com.example.trhealth.Doctor.Screens;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.trhealth.Patient.MyAppointmentAdapter;
import com.example.trhealth.Patient.MyAppointmentModel;
import com.example.trhealth.Patient.MyAppointments;
import com.example.trhealth.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class MyAppointmentDoctor extends AppCompatActivity {


    ListView rvMyAppointment;

    MyAppointmentAdapter myReportsAdapter;
    List<MyAppointmentModel> MyAppointmentModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointment_doctor);

        rvMyAppointment = findViewById(R.id.rvMyAppointment);

        MyAppointmentModelList = new ArrayList<>();

        DatabaseReference nm = FirebaseDatabase.getInstance().getReference("Booking").child(FirebaseAuth.getInstance().getUid());
        nm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    MyAppointmentModelList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        MyAppointmentModel post = snapshot.getValue(MyAppointmentModel.class);
                        post.setAppointmentId(snapshot.getKey());
                        MyAppointmentModelList.add(post);
                    }
                    myReportsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        myReportsAdapter = new MyAppointmentAdapter(MyAppointmentDoctor.this, MyAppointmentModelList);
        rvMyAppointment.setAdapter(myReportsAdapter);
    }
}