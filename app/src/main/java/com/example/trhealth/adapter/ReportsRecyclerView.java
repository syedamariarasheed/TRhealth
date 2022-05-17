package com.example.trhealth.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.trhealth.R;
import com.example.trhealth.model.Reports;

import java.util.ArrayList;

public class ReportsRecyclerView extends RecyclerView.Adapter<ReportsRecyclerView.ViewHolder> {
    String patientId;
    Context context;
    ArrayList<Reports> reportsArrayList;

    public ReportsRecyclerView(String patientId, Context context, ArrayList<Reports> reportsArrayList) {
        this.patientId = patientId;
        this.context = context;
        this.reportsArrayList = reportsArrayList;
    }

    @NonNull
    @Override
    public ReportsRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_my_reports, parent, false);
        return new ReportsRecyclerView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsRecyclerView.ViewHolder holder, int position) {
        Reports reports = reportsArrayList.get(position);
        holder.reportTitle.setText("" + reports.getReportTitle());
        holder.reportDate.setVisibility(View.GONE);
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse( reports.getReportUrl()), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Intent newIntent = Intent.createChooser(intent, "Open File");
                try {
                    context.startActivity(newIntent);
                } catch (ActivityNotFoundException e) {
                    // Instruct the user to install a PDF reader here, or something
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return reportsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView reportTitle, reportDate;
        ImageView btnView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reportTitle = itemView.findViewById(R.id.reportName);
            reportDate = itemView.findViewById(R.id.reportDate);
            btnView = itemView.findViewById(R.id.btnView);
        }
    }
}
