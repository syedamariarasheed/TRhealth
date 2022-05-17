package com.example.trhealth.Patient;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.trhealth.R;

import java.util.List;

public class MyReportsAdapter extends ArrayAdapter<String> {

    Activity context;
    List<MyReportsModel> arrayList;

    public MyReportsAdapter(Activity context, List<MyReportsModel> arrayList) {
        super(context, R.layout.row_my_reports);
        this.context = context;
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

        reportName.setText(arrayList.get(position).getName());
        reportDate.setText(arrayList.get(position).getDate());
        return rowView;
    }
}
