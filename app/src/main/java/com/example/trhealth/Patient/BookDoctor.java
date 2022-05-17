package com.example.trhealth.Patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trhealth.R;
import com.example.trhealth.Utils.Loading_Dialog;
import com.example.trhealth.model.Appointment;
import com.example.trhealth.model.AppointmentStatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BookDoctor extends AppCompatActivity {

    RecyclerView rvDoctorTimings;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;

    BookDoctorAdapter bookDoctorAdapter;

    List<BookDoctorModel> bookDoctorModelList;
    LinearLayoutManager HorizontalLayout;

    String docName = null;
    String docAddress = null;
    String docSpecialty = null;
    String doctorId = null;
    String patientId = null;
    String patientName = null;
    String patientPhoneNo = null;
    String patientEmail = null;
    String appointmentDate = null;
    String appointmentTime = null;

    TextView doctorName, doctorAddress, doctorSpecialty;
    Button bookAppointment;

    FirebaseDatabase rootNode;
    DatabaseReference reference, reference2;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    private ProgressDialog loadingDialog;

    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_doctor);
        rvDoctorTimings = findViewById(R.id.rvDoctorTimings);
        doctorName = findViewById(R.id.name);
        doctorAddress = findViewById(R.id.address);
        doctorSpecialty = findViewById(R.id.speciality);
        bookAppointment = findViewById(R.id.btnSave);

        bookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog("");
                makeAppointment();
            }
        });


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);


        appointmentDate = df.format(c);
        appointmentTime = simpleDateFormat.format(Calendar.getInstance().getTime());


        docName = getIntent().getStringExtra("docName");
        docAddress = getIntent().getStringExtra("docSpeciality");
        docSpecialty = getIntent().getStringExtra("docAddress");
        doctorId = getIntent().getStringExtra("docId");


        if (docName != null && docAddress != null && docSpecialty != null) {
            doctorName.setText("" + docName);
            doctorAddress.setText("" + docAddress);
            doctorSpecialty.setText("" + docSpecialty);

        }

        getPatientsDetail();


        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());

        // Set LayoutManager on Recycler View
        rvDoctorTimings.setLayoutManager(RecyclerViewLayoutManager);


        bookDoctorModelList = new ArrayList<>();

        BookDoctorModel BookDoctorModel = new BookDoctorModel("Thursday 9th April", "3 slots available", "Timings : 9am to 6pm");
        bookDoctorModelList.add(BookDoctorModel);

        BookDoctorModel BookDoctorModel1 = new BookDoctorModel("Friday 10th April", "1 slots available", "Timings : 9am to 6pm");
        bookDoctorModelList.add(BookDoctorModel1);

        BookDoctorModel BookDoctorModel2 = new BookDoctorModel("Saturday 11th April", "5 slots available", "Timings : 9am to 6pm");
        bookDoctorModelList.add(BookDoctorModel2);

        BookDoctorModel BookDoctorModel3 = new BookDoctorModel("Sunday 12th April", "3 slots available", "Timings : 9am to 6pm");
        bookDoctorModelList.add(BookDoctorModel3);


        bookDoctorAdapter = new BookDoctorAdapter(BookDoctor.this, bookDoctorModelList);

        HorizontalLayout = new LinearLayoutManager(BookDoctor.this, LinearLayoutManager.HORIZONTAL, false);
        rvDoctorTimings.setLayoutManager(HorizontalLayout);
        rvDoctorTimings.setAdapter(bookDoctorAdapter);
    }


    private void getPatientsDetail() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        patientId = mAuth.getUid();

        databaseReference.child("Patients").child(patientId).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                patientName = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("Patients").child(patientId).child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                patientEmail = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("Patients").child(patientId).child("phone").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                patientPhoneNo = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void makeAppointment() {

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Booking").child(doctorId);
        reference2 = reference.child(doctorId + "_bookingId" + String.valueOf(System.currentTimeMillis()));


        String doctorName = docName;

        Appointment appointment = new Appointment(appointmentDate, appointmentTime, doctorName,
                doctorId, patientId, patientPhoneNo, patientName, patientEmail);

        Map<String, Object> updates = new HashMap<String, Object>();
        updates.put("appointmentDate", appointmentDate);
        updates.put("appointmentTime", appointmentTime);
        updates.put("doctorName", doctorName);
        updates.put("doctorId", doctorId);
        updates.put("patientId", patientId);
        updates.put("patientPhoneNo", patientPhoneNo);
        updates.put("patientName", patientName);
        updates.put("patientEmail", patientEmail);
        updates.put("appointmentStatus", AppointmentStatus.NONE.name());

        reference2.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    hideLoadingDialog();
                    Toast.makeText(BookDoctor.this, "Appointment Booked", Toast.LENGTH_SHORT).show();
                } else {
                    hideLoadingDialog();
                    Toast.makeText(BookDoctor.this, "Error, Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    protected void showLoadingDialog(String message) {
        loadingDialog = Loading_Dialog.ctor(this);
        loadingDialog.setMessage("");
        loadingDialog.show();
    }

    protected void hideLoadingDialog() {
        loadingDialog.dismiss();

    }

}