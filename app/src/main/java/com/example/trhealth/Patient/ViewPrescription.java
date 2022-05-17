package com.example.trhealth.Patient;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.trhealth.R;
import com.example.trhealth.model.PatientNotification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ViewPrescription extends AppCompatActivity {

    TextView tvPrescription;
    Button btnSave;
    String patientNotificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_presciption);
        tvPrescription = findViewById(R.id.tvPrescription);
        btnSave = findViewById(R.id.btnSave);
        if (getIntent().getStringExtra("patientNotificationId") != null) {
            patientNotificationId = getIntent().getStringExtra("patientNotificationId");
        } else {
            DatabaseReference nm = FirebaseDatabase.getInstance().getReference("Patient_Notification").child(getIntent().getStringExtra("patient_id"));
            nm.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            PatientNotification post = snapshot.getValue(PatientNotification.class);
                            if (Objects.equals(post.getDoctorId(), FirebaseAuth.getInstance().getUid()))
                            {
                                patientNotificationId = snapshot.getKey();
                                return;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Patient_Notification");
                mDatabase.child(getIntent().getStringExtra("patient_id")).child(patientNotificationId).child("prescription").setValue(tvPrescription.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        finish();
                    }
                });
            }
        });
    }
}