package com.example.trhealth.Doctor.Screens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.example.trhealth.Patient.MyAppointmentModel;
import com.example.trhealth.Patient.MyReports;
import com.example.trhealth.R;
import com.example.trhealth.model.Doctors;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static android.content.Context.MODE_PRIVATE;

public class ViewPatientAdapter extends ArrayAdapter<String> {
    Activity context;
    List<ViewPatientModel> arrayList;
    Context context1;
    String doctorId;
    String patientId;
    String isAccepted;

    public ViewPatientAdapter(Activity context, List<ViewPatientModel> arrayList,
                              String doctorId, String patientId, String isAccepted) {
        super(context, R.layout.row_view_patients);
        this.context = context;
        this.context1 = context;
        this.arrayList = arrayList;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.isAccepted = isAccepted;
    }

    @Override
    public int getCount() {

        return arrayList.size();

    }

    public View getView(final int position, View view, ViewGroup parent) {
        final LayoutInflater inflater = context.getLayoutInflater();


        View rowView = inflater.inflate(R.layout.row_view_patients, null, false);

        TextView patientName = rowView.findViewById(R.id.patientName);
        TextView patientAge = rowView.findViewById(R.id.patientAge);
        TextView patientID = rowView.findViewById(R.id.patientId);
        ImageView btnView = rowView.findViewById(R.id.btnView);
        TextView sendRequest = rowView.findViewById(R.id.sendReq);

        patientName.setText(arrayList.get(position).getPatientName());
        patientAge.setText("CNIC: " + arrayList.get(position).getPatientAge());
        patientID.setText("ID: " + String.valueOf(arrayList.get(position).getId()));


        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context1, SingleAppointment.class);
                intent.putExtra("name", patientName.getText().toString());
                intent.putExtra("age", patientAge.getText().toString());
                context1.startActivity(intent);
            }
        });

        if (Objects.equals(isAccepted, "true")) {
            btnView.setVisibility(View.VISIBLE);
            sendRequest.setVisibility(View.GONE);
            btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, MyReports.class).putExtra("id", patientId));
                }
            });
        } else if (Objects.equals(isAccepted, "false")) {
            btnView.setVisibility(View.GONE);
            sendRequest.setVisibility(View.GONE);
        } else {
            btnView.setVisibility(View.GONE);
            sendRequest.setVisibility(View.VISIBLE);
        }

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Report_Request");
                mDatabase.child(doctorId).child(patientId).child("isAccepted").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        sendRequest.setVisibility(View.GONE);
                    }
                });
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Patient_Notification");
                Map<String, Object> map = new HashMap<>();
                SharedPreferences preferences = context.getSharedPreferences("prefs", MODE_PRIVATE);
                map.put("prescription", "");
                map.put("notiType", "Report");
                map.put("isAccepted", "False");
                map.put("doctorId",FirebaseAuth.getInstance().getUid());
                map.put("doctorName",preferences.getString("doctorName","Not mentioned"));
                db.child(patientId).child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                        .updateChildren(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                            }
                        });
            }
        });
        return rowView;
    }

}
