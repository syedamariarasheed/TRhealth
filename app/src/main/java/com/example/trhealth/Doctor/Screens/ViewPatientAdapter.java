package com.example.trhealth.Doctor.Screens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trhealth.Patient.MyAppointmentModel;
import com.example.trhealth.R;

import java.util.List;

public class ViewPatientAdapter extends ArrayAdapter<String> {
    Activity context;
    List<ViewPatientModel> arrayList;
    Context context1;

    public ViewPatientAdapter(Activity context, List<ViewPatientModel> arrayList) {
        super(context, R.layout.row_view_patients);
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


        View rowView = inflater.inflate(R.layout.row_view_patients, null, false);

        TextView patientName = rowView.findViewById(R.id.patientName);
        TextView patientAge = rowView.findViewById(R.id.patientAge);
        TextView patientID = rowView.findViewById(R.id.patientId);
        ImageView btnView = rowView.findViewById(R.id.btnView);

        patientName.setText(arrayList.get(position).getPatientName());
        patientAge.setText("CNIC: "+arrayList.get(position).getPatientAge());
        patientID.setText("ID: "+String.valueOf(arrayList.get(position).getId()));


        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context1,SingleAppointment.class);
                intent.putExtra("name",patientName.getText().toString());
                intent.putExtra("age",patientAge.getText().toString());
                context1.startActivity(intent);
            }
        });
        return rowView;
    }

}
