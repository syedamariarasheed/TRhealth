package com.example.trhealth.Patient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.trhealth.R

class ViewPrescriptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_prescription)
        val tvPrescription:TextView = findViewById(R.id.tvPrescription);
        val prescription: String = intent.getStringExtra("pres")?:"Doctor has not prescribed you yet"
        tvPrescription.setText(prescription)
    }
}