package com.example.trhealth.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import com.example.trhealth.R;

public class Loading_Dialog extends ProgressDialog {

    TextView tvMsg;

    public Loading_Dialog(Context context) {
        super(context);
    }

    public static Loading_Dialog ctor(Context context) {
        Loading_Dialog dialog = new Loading_Dialog(context);
        dialog.setIndeterminate(true);
        try {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception e) {

        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);

        tvMsg = findViewById(R.id.tvMsg);
        // tvMsg.setText("Please Wait . . .");

    }

    public void setMessage(String msg) {
        if (!msg.equals("")) {
            tvMsg.setText(msg);
        } else {
            tvMsg.setText("Please Wait . . .");

        }
    }

    @Override
    public void show() {
        super.show();

    }

    @Override
    public void dismiss() {
        super.dismiss();

    }
}

