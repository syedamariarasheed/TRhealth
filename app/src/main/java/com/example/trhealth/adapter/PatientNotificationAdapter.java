package com.example.trhealth.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.airbnb.lottie.LottieAnimationView;
import com.example.trhealth.Doctor.Screens.MyAppointmentDoctor;
import com.example.trhealth.R;
import com.example.trhealth.model.AppointmentStatus;
import com.example.trhealth.model.PatientNotification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class PatientNotificationAdapter extends ArrayAdapter<String> {


    Activity context;
    List<PatientNotification> arrayList;
    Context context1;
    DatabaseReference databaseReference2;

    public PatientNotificationAdapter(Activity context, List<PatientNotification> arrayList) {
        super(context, R.layout.row_my_reports);
        this.context = context;
        this.context1 = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {

        return arrayList.size();

    }

    public View getView(final int position, View view, ViewGroup parent) {
        final LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.patient_notification_row, null, false);
        TextView title = rowView.findViewById(R.id.title);
        LinearLayout reportContainer = rowView.findViewById(R.id.reportRequest);
        TextView acceptRep = rowView.findViewById(R.id.acceptRep);
        TextView declineRep = rowView.findViewById(R.id.declineRep);
        LottieAnimationView declined = rowView.findViewById(R.id.decline);
        LottieAnimationView accepted = rowView.findViewById(R.id.lottie);
        TextView description = rowView.findViewById(R.id.description);
        TextView prescription = rowView.findViewById(R.id.prescription);
        ImageView btnView = rowView.findViewById(R.id.btnView);
        LinearLayout container = rowView.findViewById(R.id.container);
        title.setText(arrayList.get(position).getDoctorName());

        if (Objects.equals(arrayList.get(position).getAppointmentStatus(), AppointmentStatus.ACCEPTED.name())) {
            description.setText("Accepted your appointment request");
            container.setBackgroundColor(Color.parseColor("#85D9FADC"));
            declined.setVisibility(View.GONE);
            accepted.setVisibility(View.VISIBLE);
        } else if (Objects.equals(arrayList.get(position).getAppointmentStatus(), AppointmentStatus.DECLINED.name())) {
            description.setText("Rejected your appointment request");
            prescription.setVisibility(View.GONE);
            btnView.setVisibility(View.GONE);
            declined.setVisibility(View.VISIBLE);
            accepted.setVisibility(View.GONE);
            container.setBackgroundColor(Color.parseColor("#70FF493C"));
        }

        if (Objects.equals(arrayList.get(position).getNotiType(), "Report")) {
            description.setText("Wants to show your reports");
            btnView.setVisibility(View.GONE);
            prescription.setVisibility(View.GONE);
            reportContainer.setVisibility(View.VISIBLE);

            acceptRep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Report_Request");
                    mDatabase.child(arrayList.get(position).getDoctorId()).child(FirebaseAuth.getInstance().getUid()).child("isAccepted").setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Patient_Notification");
                            mDatabase.child(FirebaseAuth.getInstance().getUid()).child(arrayList.get(position).getDoctorId()).removeValue();
                            context.finish();
                        }
                    });
                }
            });

            declineRep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // delete
                    DatabaseReference mDatabaseRep = FirebaseDatabase.getInstance().getReference("Report_Request");
                    mDatabaseRep.child(arrayList.get(position).getDoctorId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Patient_Notification");
                            mDatabase.child(FirebaseAuth.getInstance().getUid()).child(arrayList.get(position).getDoctorId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    context.finish();
                                }
                            });
                        }
                    });
                }
            });
        }
        return rowView;
    }
}
