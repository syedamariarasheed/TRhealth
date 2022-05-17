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
import com.airbnb.lottie.LottieAnimationView;
import com.example.trhealth.Doctor.Screens.MyAppointmentDoctor;
import com.example.trhealth.R;
import com.example.trhealth.model.AppointmentStatus;
import com.example.trhealth.model.PatientNotification;

import java.util.List;
import java.util.Objects;

public class PatientNotificationAdapter extends ArrayAdapter<String> {


    Activity context;
    List<PatientNotification> arrayList;
    Context context1;

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

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof MyAppointmentDoctor) {

                }
            }
        });
        return rowView;
    }
}
