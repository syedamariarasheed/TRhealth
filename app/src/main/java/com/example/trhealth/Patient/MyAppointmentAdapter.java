package com.example.trhealth.Patient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.trhealth.Doctor.Screens.MyAppointmentDoctor;
import com.example.trhealth.Doctor.Screens.SingleAppointment;
import com.example.trhealth.R;
import java.util.List;

public class MyAppointmentAdapter  extends ArrayAdapter<String> {


    Activity context;
    List<MyAppointmentModel> arrayList;
    Context context1;

    public MyAppointmentAdapter(Activity context, List<MyAppointmentModel> arrayList) {
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


        View rowView = inflater.inflate(R.layout.row_my_reports, null, false);

        TextView reportName = rowView.findViewById(R.id.reportName);
        TextView reportDate = rowView.findViewById(R.id.reportDate);
        ImageView btnView = rowView.findViewById(R.id.btnView);

        reportName.setText(arrayList.get(position).getPatientName());


        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof MyAppointmentDoctor){
                    Intent intent = new Intent(context1,SingleAppointment.class);
                    intent.putExtra("appointmentDetails",  arrayList.get(position));
                    context1.startActivity(intent);
                }
            }
        });
        return rowView;
    }
}
