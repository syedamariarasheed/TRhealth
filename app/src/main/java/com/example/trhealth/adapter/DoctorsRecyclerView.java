package com.example.trhealth.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trhealth.Patient.BookDoctor;
import com.example.trhealth.R;
import com.example.trhealth.model.Doctors;


import java.util.ArrayList;

public class DoctorsRecyclerView extends RecyclerView.Adapter<DoctorsRecyclerView.ViewHolder> {
    Context context;
    ArrayList<Doctors> doctorsLists;

    public DoctorsRecyclerView(Context context, ArrayList<Doctors> doctorsLists) {
        this.context = context;
        this.doctorsLists = doctorsLists;
    }

    @NonNull
    @Override
    public DoctorsRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_book_appointment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorsRecyclerView.ViewHolder holder, int position) {
        Doctors doctors = doctorsLists.get(position);
        holder.docName.setText(""+doctors.getName());
        holder.docSpeciality.setText(""+doctors.getSpecialization());
        holder.docAddress.setText(""+doctors.getHospital());


        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, BookDoctor.class);
                i.putExtra("docName",doctors.getName());
                i.putExtra("docSpeciality",doctors.getSpecialization());
                i.putExtra("docAddress",doctors.getHospital());
                i.putExtra("docId",doctors.getUid());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return doctorsLists.size();
    }

    public void filterList(ArrayList<Doctors> filteredList){
        doctorsLists = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView docName, docSpeciality, docAddress;
        LinearLayout mainLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            docName = itemView.findViewById(R.id.name);
            docSpeciality = itemView.findViewById(R.id.speciality);
            docAddress = itemView.findViewById(R.id.address);
            mainLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}
