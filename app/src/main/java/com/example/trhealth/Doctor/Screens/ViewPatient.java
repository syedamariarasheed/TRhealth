package com.example.trhealth.Doctor.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import com.example.trhealth.Patient.MyAppointmentAdapter;
import com.example.trhealth.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewPatient extends AppCompatActivity {

    ListView rvMyAppointment;

    EditText tvSearch;
    ViewPatientAdapter myReportsAdapter;
    List<ViewPatientModel> viewPatientModelList;
    DatabaseReference databaseReference;
    ArrayList<VPModel> modelArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patient);

        modelArrayList = new ArrayList<>();
        viewPatientModelList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        rvMyAppointment = findViewById(R.id.rvViewPatient);
        tvSearch = findViewById(R.id.tvSearch);


        tvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                databaseReference.child("Users").child("Patients").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {
                            for (DataSnapshot data : snapshot.getChildren()) {
                                VPModel model = data.getValue(VPModel.class);
                                if (model.getId().toString().contains(editable)){
                                    modelArrayList.add(model);
                                }
                            }

                        }
                        ViewPatientModel ViewPatientModel = new ViewPatientModel(modelArrayList.get(0).getId(),modelArrayList.get(0).getName(),modelArrayList.get(0).getCnic());
                        viewPatientModelList.add(ViewPatientModel);

                        myReportsAdapter = new ViewPatientAdapter(ViewPatient.this, viewPatientModelList);
                        rvMyAppointment.setAdapter(myReportsAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


    }
}