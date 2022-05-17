package com.example.trhealth.Patient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trhealth.R;

import java.util.List;

public class BookDoctorAdapter extends RecyclerView.Adapter<BookDoctorAdapter.MyView> {

    Context context;
    List<BookDoctorModel> bookDoctorModelList;


    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.row_doctor_timings,
                        parent,
                        false);

        // return itemView
        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        holder.day.setText(bookDoctorModelList.get(position).getDay());
        holder.timings.setText(bookDoctorModelList.get(position).getTimings());
        holder.slots.setText(bookDoctorModelList.get(position).getSlots());
    }

    @Override
    public int getItemCount() {
        return bookDoctorModelList.size();
    }

    public class MyView
            extends RecyclerView.ViewHolder {

        TextView day;
        TextView slots;
        TextView timings;

        public MyView(View view) {
            super(view);
            day = (TextView) view.findViewById(R.id.day);
            slots = (TextView) view.findViewById(R.id.slots);
            timings = (TextView) view.findViewById(R.id.timings);
        }
    }

    public BookDoctorAdapter(Context context, List<BookDoctorModel> bookDoctorModelList) {
        this.bookDoctorModelList = bookDoctorModelList;
        this.context = context;
    }
}
