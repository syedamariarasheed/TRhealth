package com.example.trhealth.Patient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.trhealth.R;

import java.util.List;


public class BookAppointmentAdapter extends ArrayAdapter<String> {

    Activity context;
    List<BookAppointmentModel> arrayList;
    Context context1;

    public BookAppointmentAdapter(Activity context,List<BookAppointmentModel> arrayList) {
        super(context, R.layout.row_book_appointment);
        this.context = context;
        this.arrayList = arrayList;
        this.context1 = context;
    }

    @Override
    public int getCount() {

        return arrayList.size();

    }


    public View getView(final int position, View view, ViewGroup parent) {
        final LayoutInflater inflater = context.getLayoutInflater();


        View rowView = inflater.inflate(R.layout.row_book_appointment, null, false);

        TextView doctorName = rowView.findViewById(R.id.name);
        TextView doctorSpeciality = rowView.findViewById(R.id.speciality);
        TextView doctorAddress = rowView.findViewById(R.id.address);
        RatingBar ratingBar = rowView.findViewById(R.id.rating);
        doctorName.setText(arrayList.get(position).getName());
        doctorSpeciality.setText(arrayList.get(position).getSpeciality());
        doctorAddress.setText(arrayList.get(position).getAddress());
        ratingBar.setRating(Float.parseFloat(arrayList.get(position).getRatings()));

        LinearLayout linearLayout = rowView.findViewById(R.id.linearLayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,BookDoctor.class);
                context1.startActivity(intent);
            }
        });

        return rowView;
    }
}
