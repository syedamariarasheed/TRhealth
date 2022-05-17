package com.example.trhealth.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.trhealth.R;
import com.example.trhealth.adapter.DoctorsRecyclerView;
import com.example.trhealth.model.Doctors;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookAppointment extends AppCompatActivity {

    EditText searchDoctor, searchSpecialty;
    Button search;

    List<BookAppointmentModel> appointmentModelList;
    BookAppointmentAdapter bookAppointmentAdapter;
    ListView rvAllDoctors;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    DoctorsRecyclerView adapter;
    ArrayList<Doctors> doctorsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Doctors");

        rvAllDoctors = findViewById(R.id.rvAllDoctors);
        search = findViewById(R.id.btnSearch);
        searchDoctor = findViewById(R.id.searchDoctor);
        searchSpecialty = findViewById(R.id.searchSpeciality);

        recyclerView = findViewById(R.id.recyclerViewDotors);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        doctorsArrayList = new ArrayList<>();
        adapter = new DoctorsRecyclerView(this, doctorsArrayList);
        recyclerView.setAdapter(adapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Doctors doctors = dataSnapshot.getValue(Doctors.class);
                    doctorsArrayList.add(doctors);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        searchDoctor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchByName(s.toString());
            }
        });

        searchSpecialty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchBySpeciality(s.toString());
            }
        });


    }

    private void searchByName(String searchText) {
        ArrayList<Doctors> filterdNames = new ArrayList<>();
        for (Doctors items : doctorsArrayList) {
            if (items.getName().toLowerCase().contains(searchText.toLowerCase())) {
                filterdNames.add(items);
            }
        }
        adapter.filterList(filterdNames);
    }

    private void searchBySpeciality(String searchText) {
        ArrayList<Doctors> filterdNames = new ArrayList<>();
        for (Doctors items : doctorsArrayList) {
            if (items.getSpecialization().toLowerCase().contains(searchText.toLowerCase())) {
                filterdNames.add(items);
            }
        }
        adapter.filterList(filterdNames);
    }


}