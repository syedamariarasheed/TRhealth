package com.example.trhealth.Doctor.Screens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.trhealth.R;
import com.example.trhealth.Utils.Loading_Dialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DoctorSignUp extends AppCompatActivity {


    LinearLayout btnBusinessHours;
    public String mondayStartTime,mondayEndTime,tuesdayStartTime,tuesdayEndTime,wednesdayStartTime,wednesdayEndTime
            ,thursStartTime,thursEndTime,friStartTime,friEndTime,satStartTime,satEndTime
            ,sunStartTime,sunEndTime;

    TextInputEditText doctorName;
    TextInputEditText doctorEmail;
    TextInputEditText doctorPassword;
    TextInputEditText doctorPhone;
    TextInputEditText doctorCnic;
    TextInputEditText doctorSpecialization;
    TextInputEditText doctorUniversity;
    TextInputEditText doctorHospital;
    LinearLayout btnSignUp;
    FirebaseAuth mAuth;
    private String currentUserID;
    private DatabaseReference userRef;
    private ProgressDialog loadingDialog;
    private long idCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_sign_up);

        btnBusinessHours = findViewById(R.id.btnBusinessHours);
        doctorName = findViewById(R.id.doctorName);
        doctorEmail = findViewById(R.id.doctorEmail);
        doctorPassword = findViewById(R.id.doctorPassword);
        doctorPhone = findViewById(R.id.doctorPhone);
        doctorCnic = findViewById(R.id.doctorCNIC);
        doctorHospital = findViewById(R.id.doctorHospital);
        doctorSpecialization = findViewById(R.id.doctorSpeciality);
        doctorUniversity = findViewById(R.id.doctorUniversity);
        
        btnSignUp = findViewById(R.id.btnSignUp);

        btnBusinessHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        LayoutInflater inflater = (LayoutInflater)
                                getSystemService(LAYOUT_INFLATER_SERVICE);
                        View popupView = inflater.inflate(R.layout.doctor_timing_popup, null);
                        // create the popup window

                        int width = LinearLayout.LayoutParams.MATCH_PARENT;
                        int height = LinearLayout.LayoutParams.MATCH_PARENT;
                        boolean focusable = true; // lets taps outside the popup also dismiss it
                        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                        // show the popup window
                        // which view you pass in doesn't matter, it is only used for the window tolken
                        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                        // dismiss the popup window when touched

                        AppCompatButton btnAdd = popupView.findViewById(R.id.btnAdd);
                        AppCompatButton btnCancle = popupView.findViewById(R.id.btnCancle);

                        EditText etMondayOpening = popupView.findViewById(R.id.etMondayOpening);
                        EditText etMondayClosing = popupView.findViewById(R.id.etMondayClosing);

                        EditText etTuesdayOpening = popupView.findViewById(R.id.etTuesdayOpening);
                        EditText etTuesdayClosing = popupView.findViewById(R.id.etTuesdayClosing);

                        EditText etWednesdayOpening = popupView.findViewById(R.id.etWednesdayOpening);
                        EditText etWednesdayClosing = popupView.findViewById(R.id.etWedesdayClosing);

                        EditText etThursOpening = popupView.findViewById(R.id.etThursOpening);
                        EditText etThursClosing = popupView.findViewById(R.id.etThursClosing);

                        EditText etFriOpening = popupView.findViewById(R.id.etFriOpening);
                        EditText etFriClosing = popupView.findViewById(R.id.etFriClosing);

                        EditText etSatOpening = popupView.findViewById(R.id.etSatOpening);
                        EditText etSatClosing = popupView.findViewById(R.id.etSatClosing);

                        EditText etSunOpening = popupView.findViewById(R.id.etSunOpening);
                        EditText etSunClosing = popupView.findViewById(R.id.etSunCliosing);

                        AppCompatButton btnMonClear = popupView.findViewById(R.id.btnMonClear);
                        AppCompatButton btnTuesClear = popupView.findViewById(R.id.btnTueClear);
                        AppCompatButton btnWedClear = popupView.findViewById(R.id.btnWedClear);
                        AppCompatButton btnThursClear = popupView.findViewById(R.id.btnThursClear);
                        AppCompatButton btnFriClear = popupView.findViewById(R.id.btnFriClear);
                        AppCompatButton btnSatClear = popupView.findViewById(R.id.btnSatClear);
                        AppCompatButton btnSunClear = popupView.findViewById(R.id.btnSunClear);


                        btnMonClear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                etMondayClosing.setText("");
                                etMondayOpening.setText("");
                                mondayStartTime = null;
                                mondayEndTime = null;
                            }
                        });

                        btnTuesClear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                etTuesdayClosing.setText("");
                                etTuesdayOpening.setText("");
                                tuesdayStartTime = null;
                                tuesdayEndTime = null;
                            }
                        });

                        btnMonClear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                etMondayClosing.setText("");
                                etMondayOpening.setText("");
                                mondayStartTime = null;
                                mondayEndTime = null;
                            }
                        });

                        btnWedClear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                etWednesdayClosing.setText("");
                                etWednesdayOpening.setText("");
                                wednesdayStartTime = null;
                                wednesdayEndTime = null;
                            }
                        });

                        btnThursClear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                etThursClosing.setText("");
                                etThursOpening.setText("");
                                thursStartTime = null;
                                thursEndTime = null;
                            }
                        });

                        btnFriClear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                etFriOpening.setText("");
                                etFriClosing.setText("");
                                friStartTime = null;
                                friEndTime = null;
                            }
                        });

                        btnSatClear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                etSatOpening.setText("");
                                etSatClosing.setText("");
                                satStartTime = null;
                                satEndTime = null;
                            }
                        });

                        btnSunClear.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                etSunClosing.setText("");
                                etSunOpening.setText("");
                                sunStartTime = null;
                                sunEndTime = null;
                            }
                        });

                        etMondayOpening.setInputType(InputType.TYPE_NULL);
                        etMondayOpening.setOnClickListener(new View.OnClickListener() {
                            private TimePickerDialog picker;

                            @Override
                            public void onClick(View v) {
                                final Calendar cldr = Calendar.getInstance();
                                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                                int minutes = cldr.get(Calendar.MINUTE);


                                // time picker dialog

                                picker = new TimePickerDialog(DoctorSignUp.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {


                                                mondayStartTime = sHour+":"+sMinute;
                                                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");

                                                Date date = null;
                                                try {
                                                    date = fmt.parse(mondayStartTime);
                                                } catch (ParseException e) {

                                                    e.printStackTrace();
                                                }

                                                SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");


                                                String formattedTime=fmtOut.format(date);

                                                mondayStartTime = fmt.format(date);
                                                // etMondayOpening.setText(sHour + ":" + sMinute);
                                                etMondayOpening.setText(formattedTime);
                                            }
                                        }, hour, minutes, false);
                                picker.show();
                            }
                        });

                        etMondayClosing.setInputType(InputType.TYPE_NULL);
                        etMondayClosing.setOnClickListener(new View.OnClickListener() {
                            private TimePickerDialog picker;

                            @Override
                            public void onClick(View v) {
                                final Calendar cldr = Calendar.getInstance();
                                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                                int minutes = cldr.get(Calendar.MINUTE);


                                // time picker dialog

                                picker = new TimePickerDialog(DoctorSignUp.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {


                                                mondayEndTime = sHour+":"+sMinute;

                                                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                                                Date date = null;
                                                try {
                                                    date = fmt.parse(mondayEndTime);
                                                } catch (ParseException e) {

                                                    e.printStackTrace();
                                                }

                                                SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");

                                                String formattedTime=fmtOut.format(date);
                                                mondayEndTime = fmt.format(date);
                                                // etMondayOpening.setText(sHour + ":" + sMinute);
                                                etMondayClosing.setText(formattedTime);
                                            }
                                        }, hour, minutes, false);
                                picker.show();
                            }
                        });

                        etTuesdayOpening.setInputType(InputType.TYPE_NULL);
                        etTuesdayOpening.setOnClickListener(new View.OnClickListener() {
                            private TimePickerDialog picker;

                            @Override
                            public void onClick(View v) {
                                final Calendar cldr = Calendar.getInstance();
                                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                                int minutes = cldr.get(Calendar.MINUTE);


                                // time picker dialog

                                picker = new TimePickerDialog(DoctorSignUp.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {

                                                tuesdayStartTime = sHour+":"+sMinute;


                                                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                                                Date date = null;
                                                try {
                                                    date = fmt.parse(tuesdayStartTime);
                                                } catch (ParseException e) {

                                                    e.printStackTrace();
                                                }

                                                SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");

                                                String formattedTime=fmtOut.format(date);
                                                tuesdayStartTime= fmt.format(date);
                                                // etMondayOpening.setText(sHour + ":" + sMinute);
                                                etTuesdayOpening.setText(formattedTime);
                                            }
                                        }, hour, minutes, false);
                                picker.show();
                            }
                        });

                        etTuesdayClosing.setInputType(InputType.TYPE_NULL);
                        etTuesdayClosing.setOnClickListener(new View.OnClickListener() {
                            private TimePickerDialog picker;

                            @Override
                            public void onClick(View v) {
                                final Calendar cldr = Calendar.getInstance();
                                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                                int minutes = cldr.get(Calendar.MINUTE);


                                // time picker dialog

                                picker = new TimePickerDialog(DoctorSignUp.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {

                                                tuesdayEndTime = sHour + ":" + sMinute;

                                                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                                                Date date = null;
                                                try {
                                                    date = fmt.parse(tuesdayEndTime);
                                                } catch (ParseException e) {

                                                    e.printStackTrace();
                                                }

                                                SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");

                                                String formattedTime=fmtOut.format(date);
                                                tuesdayEndTime = fmt.format(date);
                                                // etMondayOpening.setText(sHour + ":" + sMinute);
                                                etTuesdayClosing.setText(formattedTime);
                                            }
                                        }, hour, minutes, false);
                                picker.show();
                            }
                        });

                        etWednesdayOpening.setInputType(InputType.TYPE_NULL);
                        etWednesdayOpening.setOnClickListener(new View.OnClickListener() {
                            private TimePickerDialog picker;

                            @Override
                            public void onClick(View v) {
                                final Calendar cldr = Calendar.getInstance();
                                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                                int minutes = cldr.get(Calendar.MINUTE);


                                // time picker dialog

                                picker = new TimePickerDialog(DoctorSignUp.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {

                                                wednesdayStartTime = sHour+":"+sMinute;

                                                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                                                Date date = null;
                                                try {
                                                    date = fmt.parse(wednesdayStartTime);
                                                } catch (ParseException e) {

                                                    e.printStackTrace();
                                                }

                                                SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");

                                                String formattedTime=fmtOut.format(date);
                                                // etMondayOpening.setText(sHour + ":" + sMinute);
                                                wednesdayStartTime = fmt.format(date);
                                                etWednesdayOpening.setText(formattedTime);
                                            }
                                        }, hour, minutes, false);
                                picker.show();
                            }
                        });

                        etWednesdayClosing.setInputType(InputType.TYPE_NULL);
                        etWednesdayClosing.setOnClickListener(new View.OnClickListener() {
                            private TimePickerDialog picker;

                            @Override
                            public void onClick(View v) {
                                final Calendar cldr = Calendar.getInstance();
                                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                                int minutes = cldr.get(Calendar.MINUTE);


                                // time picker dialog

                                picker = new TimePickerDialog(DoctorSignUp.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {


                                                wednesdayEndTime = sHour+":"+sMinute;

                                                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                                                Date date = null;
                                                try {
                                                    date = fmt.parse(wednesdayEndTime);
                                                } catch (ParseException e) {

                                                    e.printStackTrace();
                                                }

                                                SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");

                                                String formattedTime=fmtOut.format(date);
                                                // etMondayOpening.setText(sHour + ":" + sMinute);
                                                wednesdayEndTime = fmt.format(date);
                                                etWednesdayClosing.setText(formattedTime);
                                            }
                                        }, hour, minutes, false);
                                picker.show();
                            }
                        });

                        etThursOpening.setInputType(InputType.TYPE_NULL);
                        etThursOpening.setOnClickListener(new View.OnClickListener() {
                            private TimePickerDialog picker;

                            @Override
                            public void onClick(View v) {
                                final Calendar cldr = Calendar.getInstance();
                                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                                int minutes = cldr.get(Calendar.MINUTE);


                                // time picker dialog

                                picker = new TimePickerDialog(DoctorSignUp.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {


                                                thursStartTime = sHour+":"+sMinute;


                                                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                                                Date date = null;
                                                try {
                                                    date = fmt.parse(thursStartTime);
                                                } catch (ParseException e) {

                                                    e.printStackTrace();
                                                }

                                                SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");

                                                String formattedTime=fmtOut.format(date);
                                                thursStartTime = fmt.format(date);
                                                // etMondayOpening.setText(sHour + ":" + sMinute);
                                                etThursOpening.setText(formattedTime);
                                            }
                                        }, hour, minutes, false);
                                picker.show();
                            }
                        });
                        etThursClosing.setInputType(InputType.TYPE_NULL);
                        etThursClosing.setOnClickListener(new View.OnClickListener() {
                            private TimePickerDialog picker;

                            @Override
                            public void onClick(View v) {
                                final Calendar cldr = Calendar.getInstance();
                                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                                int minutes = cldr.get(Calendar.MINUTE);


                                // time picker dialog

                                picker = new TimePickerDialog(DoctorSignUp.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {


                                                thursEndTime = sHour+":"+sMinute;

                                                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                                                Date date = null;
                                                try {
                                                    date = fmt.parse(thursEndTime);
                                                } catch (ParseException e) {

                                                    e.printStackTrace();
                                                }

                                                SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");

                                                String formattedTime=fmtOut.format(date);
                                                // etMondayOpening.setText(sHour + ":" + sMinute);
                                                thursEndTime = fmt.format(date);
                                                etThursClosing.setText(formattedTime);
                                            }
                                        }, hour, minutes, false);
                                picker.show();
                            }
                        });
                        etFriOpening.setInputType(InputType.TYPE_NULL);
                        etFriOpening.setOnClickListener(new View.OnClickListener() {
                            private TimePickerDialog picker;

                            @Override
                            public void onClick(View v) {
                                final Calendar cldr = Calendar.getInstance();
                                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                                int minutes = cldr.get(Calendar.MINUTE);


                                // time picker dialog

                                picker = new TimePickerDialog(DoctorSignUp.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {


                                                friStartTime = sHour+":"+sMinute;


                                                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                                                Date date = null;
                                                try {
                                                    date = fmt.parse(friStartTime);
                                                } catch (ParseException e) {

                                                    e.printStackTrace();
                                                }

                                                SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");

                                                String formattedTime=fmtOut.format(date);
                                                // etMondayOpening.setText(sHour + ":" + sMinute);
                                                friStartTime = fmt.format(date);
                                                etFriOpening.setText(formattedTime);
                                            }
                                        }, hour, minutes, false);
                                picker.show();
                            }
                        });

                        etFriClosing.setInputType(InputType.TYPE_NULL);
                        etFriClosing.setOnClickListener(new View.OnClickListener() {
                            private TimePickerDialog picker;

                            @Override
                            public void onClick(View v) {
                                final Calendar cldr = Calendar.getInstance();
                                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                                int minutes = cldr.get(Calendar.MINUTE);


                                // time picker dialog

                                picker = new TimePickerDialog(DoctorSignUp.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {

                                                friEndTime = sHour+":"+sMinute;

                                                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                                                Date date = null;
                                                try {
                                                    date = fmt.parse(friEndTime);
                                                } catch (ParseException e) {

                                                    e.printStackTrace();
                                                }

                                                SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");

                                                String formattedTime=fmtOut.format(date);
                                                // etMondayOpening.setText(sHour + ":" + sMinute);
                                                friEndTime  =fmt.format(date);
                                                etFriClosing.setText(formattedTime);
                                            }
                                        }, hour, minutes, false);
                                picker.show();
                            }
                        });

                        etSatOpening.setInputType(InputType.TYPE_NULL);
                        etSatOpening.setOnClickListener(new View.OnClickListener() {
                            private TimePickerDialog picker;

                            @Override
                            public void onClick(View v) {
                                final Calendar cldr = Calendar.getInstance();
                                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                                int minutes = cldr.get(Calendar.MINUTE);


                                // time picker dialog

                                picker = new TimePickerDialog(DoctorSignUp.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {

                                                satStartTime = sHour+":"+sMinute;


                                                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                                                Date date = null;
                                                try {
                                                    date = fmt.parse(satStartTime);
                                                } catch (ParseException e) {

                                                    e.printStackTrace();
                                                }

                                                SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");

                                                String formattedTime=fmtOut.format(date);
                                                // etMondayOpening.setText(sHour + ":" + sMinute);
                                                satStartTime = fmt.format(date);
                                                etSatOpening.setText(formattedTime);
                                            }
                                        }, hour, minutes, false);
                                picker.show();
                            }
                        });
                        etSatClosing.setInputType(InputType.TYPE_NULL);
                        etSatClosing.setOnClickListener(new View.OnClickListener() {
                            private TimePickerDialog picker;

                            @Override
                            public void onClick(View v) {
                                final Calendar cldr = Calendar.getInstance();
                                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                                int minutes = cldr.get(Calendar.MINUTE);


                                // time picker dialog

                                picker = new TimePickerDialog(DoctorSignUp.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {

                                                satEndTime = sHour+":"+sMinute;

                                                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                                                Date date = null;
                                                try {
                                                    date = fmt.parse(satEndTime);
                                                } catch (ParseException e) {

                                                    e.printStackTrace();
                                                }

                                                SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");

                                                String formattedTime=fmtOut.format(date);
                                                // etMondayOpening.setText(sHour + ":" + sMinute);
                                                satEndTime = fmt.format(date);
                                                etSatClosing.setText(formattedTime);
                                            }
                                        }, hour, minutes, false);
                                picker.show();
                            }
                        });
                        etSunOpening.setInputType(InputType.TYPE_NULL);
                        etSunOpening.setOnClickListener(new View.OnClickListener() {
                            private TimePickerDialog picker;

                            @Override
                            public void onClick(View v) {
                                final Calendar cldr = Calendar.getInstance();
                                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                                int minutes = cldr.get(Calendar.MINUTE);


                                // time picker dialog


                                picker = new TimePickerDialog(DoctorSignUp.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {

                                                sunStartTime = sHour+":"+sMinute;

                                                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                                                Date date = null;
                                                try {
                                                    date = fmt.parse(sunStartTime);
                                                } catch (ParseException e) {

                                                    e.printStackTrace();
                                                }

                                                SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");

                                                String formattedTime=fmtOut.format(date);
                                                // etMondayOpening.setText(sHour + ":" + sMinute);
                                                sunStartTime = fmt.format(date);
                                                etSunOpening.setText(formattedTime);
                                            }
                                        }, hour, minutes, false);
                                picker.show();
                            }
                        });
                        etSunClosing.setInputType(InputType.TYPE_NULL);
                        etSunClosing.setOnClickListener(new View.OnClickListener() {
                            private TimePickerDialog picker;

                            @Override
                            public void onClick(View v) {
                                final Calendar cldr = Calendar.getInstance();
                                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                                int minutes = cldr.get(Calendar.MINUTE);


                                // time picker dialog

                                picker = new TimePickerDialog(DoctorSignUp.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {

                                                sunEndTime = sHour+":"+sMinute;

                                                SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                                                Date date = null;
                                                try {
                                                    date = fmt.parse(sunEndTime);
                                                } catch (ParseException e) {

                                                    e.printStackTrace();
                                                }

                                                SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");

                                                String formattedTime=fmtOut.format(date);
                                                // etMondayOpening.setText(sHour + ":" + sMinute);
                                                sunEndTime = fmt.format(date);
                                                etSunClosing.setText(formattedTime);
                                            }
                                        }, hour, minutes, false);
                                picker.show();
                            }
                        });

//                        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
//
//                        etMondayOpening.setText(prefs.getString("mondayOpenTiming", ""));
//                        etMondayClosing.setText(prefs.getString("mondayCloseTiming", ""));
//                        etTuesdayOpening.setText(prefs.getString("tuesdayOpenTiming", ""));
//                        etTuesdayClosing.setText(prefs.getString("tuesdayCloseTiming", ""));
//                        etWednesdayOpening.setText(prefs.getString("wedOpenTiming", ""));
//                        etWednesdayClosing.setText(prefs.getString("wedCloseTiming", ""));
//                        etThursOpening.setText(prefs.getString("thurOpenTiming", ""));
//                        etThursClosing.setText(prefs.getString("thurCloseTiming", ""));
//                        etFriOpening.setText(prefs.getString("friOpenTiming", ""));
//                        etFriClosing.setText(prefs.getString("friCloseTiming", ""));
//                        etSatOpening.setText(prefs.getString("satOpenTiming", ""));
//                        etSatClosing.setText(prefs.getString("satCloseTiming", ""));
//                        etSunOpening.setText(prefs.getString("sunOpenTiming", ""));
//                        etSunClosing.setText(prefs.getString("sunCloseTiming", ""));


                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Hidden is implementation of business hours
//                                if (TextUtils.isEmpty(etMondayOpening.getText().toString()) && TextUtils.isEmpty(etMondayClosing.getText().toString()) &&
//                                        TextUtils.isEmpty(etTuesdayOpening.getText().toString()) && TextUtils.isEmpty(etTuesdayClosing.getText().toString())
//                                        && TextUtils.isEmpty(etWednesdayOpening.getText().toString()) && TextUtils.isEmpty(etWednesdayClosing.getText().toString()) &&
//                                        TextUtils.isEmpty(etThursOpening.getText().toString()) && TextUtils.isEmpty(etThursClosing.getText().toString())
//                                        && TextUtils.isEmpty(etFriOpening.getText().toString()) && TextUtils.isEmpty(etFriClosing.getText().toString()) &&
//                                        TextUtils.isEmpty(etSatOpening.getText().toString()) && TextUtils.isEmpty(etSatClosing.getText().toString()) &&
//                                        TextUtils.isEmpty(etSunOpening.getText().toString()) && TextUtils.isEmpty(etSunClosing.getText().toString())
//                                ) {
//                                    Toast.makeText(DoctorSignUp.this, "Please select atleast one time", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    if ((TextUtils.isEmpty(etMondayOpening.getText().toString()) && !TextUtils.isEmpty(etMondayClosing.getText().toString()))
//                                            || (TextUtils.isEmpty(etMondayClosing.getText().toString()) && !TextUtils.isEmpty(etMondayOpening.getText().toString()))
//                                            || (TextUtils.isEmpty(etTuesdayOpening.getText().toString()) && !TextUtils.isEmpty(etTuesdayClosing.getText().toString()))
//                                            || (TextUtils.isEmpty(etTuesdayClosing.getText().toString()) && !TextUtils.isEmpty(etTuesdayOpening.getText().toString()))
//                                            || (TextUtils.isEmpty(etWednesdayOpening.getText().toString()) && !TextUtils.isEmpty(etWednesdayClosing.getText().toString()))
//                                            || (TextUtils.isEmpty(etWednesdayClosing.getText().toString()) && !TextUtils.isEmpty(etWednesdayOpening.getText().toString()))
//                                            || (TextUtils.isEmpty(etThursOpening.getText().toString()) && !TextUtils.isEmpty(etThursClosing.getText().toString()))
//                                            || (TextUtils.isEmpty(etThursClosing.getText().toString()) && !TextUtils.isEmpty(etThursOpening.getText().toString()))
//                                            || (TextUtils.isEmpty(etFriOpening.getText().toString()) && !TextUtils.isEmpty(etFriClosing.getText().toString()))
//                                            || (TextUtils.isEmpty(etFriClosing.getText().toString()) && !TextUtils.isEmpty(etFriOpening.getText().toString()))
//                                            || (TextUtils.isEmpty(etSatOpening.getText().toString()) && !TextUtils.isEmpty(etSatClosing.getText().toString()))
//                                            || (TextUtils.isEmpty(etSatClosing.getText().toString()) && !TextUtils.isEmpty(etSatOpening.getText().toString()))
//                                            || (TextUtils.isEmpty(etSunOpening.getText().toString()) && !TextUtils.isEmpty(etSunClosing.getText().toString()))
//                                            || (TextUtils.isEmpty(etSunClosing.getText().toString()) && !TextUtils.isEmpty(etSunOpening.getText().toString()))
//                                    ) {
//                                        Toast.makeText(DoctorSignUp.this, "Please enter both starting and ending time for a day", Toast.LENGTH_LONG).show();
//                                    } else {
//                                        // Do the work here
//                                        if (!workingHoursDTOArrayList.isEmpty()) {
//                                            workingHoursDTOArrayList = new ArrayList<>();
//                                        }
//                                        editor.putString("mondayOpenTiming", etMondayOpening.getText().toString());
//                                        editor.putString("mondayCloseTiming", etMondayClosing.getText().toString());
//                                        editor.putString("tuesdayOpenTiming", etTuesdayOpening.getText().toString());
//                                        editor.putString("tuesdayCloseTiming", etTuesdayClosing.getText().toString());
//                                        editor.putString("wedOpenTiming", etWednesdayOpening.getText().toString());
//                                        editor.putString("wedCloseTiming", etWednesdayClosing.getText().toString());
//                                        editor.putString("thurOpenTiming", etThursOpening.getText().toString());
//                                        editor.putString("thurCloseTiming", etThursClosing.getText().toString());
//                                        editor.putString("friOpenTiming", etFriOpening.getText().toString());
//                                        editor.putString("friCloseTiming", etFriClosing.getText().toString());
//                                        editor.putString("satOpenTiming", etSatOpening.getText().toString());
//                                        editor.putString("satCloseTiming", etSatClosing.getText().toString());
//                                        editor.putString("sunOpenTiming", etSunOpening.getText().toString());
//                                        editor.putString("sunCloseTiming", etSunClosing.getText().toString());
//                                        editor.apply();
//                                        popupWindow.dismiss();
//
//                                        if (!TextUtils.isEmpty(etMondayOpening.getText().toString())) {
//                                            MerchantWorkingHoursDTO object = new MerchantWorkingHoursDTO();
//                                            object.setOpeningHour(mondayStartTime);
//                                            object.setClosingHour(mondayEndTime);
//                                            object.setDay("monday");
//                                            object.setClosed(false);
//                                            object.setTimeZone(timeZone);
//                                            workingHoursDTOArrayList.add(object);
//                                        } else {
//                                            MerchantWorkingHoursDTO object = new MerchantWorkingHoursDTO();
//                                            object.setOpeningHour("");
//                                            object.setClosingHour("");
//                                            object.setDay("monday");
//                                            object.setClosed(true);
//                                            object.setTimeZone(timeZone);
//                                            workingHoursDTOArrayList.add(object);
//                                        }
//
//                                        if (!TextUtils.isEmpty(etTuesdayOpening.getText().toString())) {
//                                            MerchantWorkingHoursDTO object = new MerchantWorkingHoursDTO();
//                                            object.setOpeningHour(tuesdayStartTime);
//                                            object.setClosingHour(tuesdayEndTime);
//                                            object.setDay("tuesday");
//                                            object.setClosed(false);
//                                            object.setTimeZone(timeZone);
//                                            workingHoursDTOArrayList.add(object);
//                                        } else {
//                                            MerchantWorkingHoursDTO object = new MerchantWorkingHoursDTO();
//                                            object.setOpeningHour("");
//                                            object.setClosingHour("");
//                                            object.setDay("tuesday");
//                                            object.setClosed(true);
//                                            object.setTimeZone(timeZone);
//                                            workingHoursDTOArrayList.add(object);
//                                        }
//
//                                        if (!TextUtils.isEmpty(etWednesdayOpening.getText().toString())) {
//                                            MerchantWorkingHoursDTO object = new MerchantWorkingHoursDTO();
//                                            object.setOpeningHour(wednesdayStartTime);
//                                            object.setClosingHour(wednesdayEndTime);
//                                            object.setDay("wednesday");
//                                            object.setClosed(false);
//                                            object.setTimeZone(timeZone);
//                                            workingHoursDTOArrayList.add(object);
//                                        } else {
//                                            MerchantWorkingHoursDTO object = new MerchantWorkingHoursDTO();
//                                            object.setOpeningHour("");
//                                            object.setClosingHour("");
//                                            object.setDay("wednesday");
//                                            object.setClosed(true);
//                                            object.setTimeZone(timeZone);
//                                            workingHoursDTOArrayList.add(object);
//                                        }
//
//                                        if (!TextUtils.isEmpty(etThursOpening.getText().toString())) {
//                                            MerchantWorkingHoursDTO object = new MerchantWorkingHoursDTO();
//                                            object.setOpeningHour(thursStartTime);
//                                            object.setClosingHour(thursEndTime);
//                                            object.setDay("thursday");
//                                            object.setClosed(false);
//                                            object.setTimeZone(timeZone);
//                                            workingHoursDTOArrayList.add(object);
//                                        } else {
//                                            MerchantWorkingHoursDTO object = new MerchantWorkingHoursDTO();
//                                            object.setOpeningHour("");
//                                            object.setClosingHour("");
//                                            object.setDay("thursday");
//                                            object.setClosed(true);
//                                            object.setTimeZone(timeZone);
//                                            workingHoursDTOArrayList.add(object);
//                                        }
//
//                                        if (!TextUtils.isEmpty(etFriOpening.getText().toString())) {
//                                            MerchantWorkingHoursDTO object = new MerchantWorkingHoursDTO();
//                                            object.setOpeningHour(friStartTime);
//                                            object.setClosingHour(friEndTime);
//                                            object.setDay("friday");
//                                            object.setClosed(false);
//                                            object.setTimeZone(timeZone);
//                                            workingHoursDTOArrayList.add(object);
//                                        } else {
//                                            MerchantWorkingHoursDTO object = new MerchantWorkingHoursDTO();
//                                            object.setOpeningHour("");
//                                            object.setClosingHour("");
//                                            object.setDay("friday");
//                                            object.setClosed(true);
//                                            object.setTimeZone(timeZone);
//                                            workingHoursDTOArrayList.add(object);
//                                        }
//
//                                        if (!TextUtils.isEmpty(etSatOpening.getText().toString())) {
//                                            MerchantWorkingHoursDTO object = new MerchantWorkingHoursDTO();
//                                            object.setOpeningHour(satStartTime);
//                                            object.setClosingHour(satEndTime);
//                                            object.setDay("saturday");
//                                            object.setClosed(false);
//                                            object.setTimeZone(timeZone);
//                                            workingHoursDTOArrayList.add(object);
//                                        } else {
//                                            MerchantWorkingHoursDTO object = new MerchantWorkingHoursDTO();
//                                            object.setOpeningHour("");
//                                            object.setClosingHour("");
//                                            object.setDay("saturday");
//                                            object.setClosed(true);
//                                            object.setTimeZone(timeZone);
//                                            workingHoursDTOArrayList.add(object);
//                                        }
//
//                                        if (!TextUtils.isEmpty(etSunOpening.getText().toString())) {
//                                            MerchantWorkingHoursDTO object = new MerchantWorkingHoursDTO();
//                                            object.setOpeningHour(sunStartTime);
//                                            object.setClosingHour(sunEndTime);
//                                            object.setDay("sunday");
//                                            object.setClosed(false);
//                                            object.setTimeZone(timeZone);
//                                            workingHoursDTOArrayList.add(object);
//                                        } else {
//                                            MerchantWorkingHoursDTO object = new MerchantWorkingHoursDTO();
//                                            object.setOpeningHour("");
//                                            object.setClosingHour("");
//                                            object.setDay("sunday");
//                                            object.setClosed(true);
//                                            object.setTimeZone(timeZone);
//                                            workingHoursDTOArrayList.add(object);
//                                        }
//                                    }
//                                }
                            }


                        });

                        btnCancle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                popupWindow.dismiss();
                            }
                        });
                        popupView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return true;
                            }
                        });
                    }
                }, 500);

            }
        });

        mAuth = FirebaseAuth.getInstance();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                idCount = snapshot.getChildrenCount()+1;
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
    }

    void signup(){
        showLoadingDialog("");
        mAuth.createUserWithEmailAndPassword(doctorEmail.getText().toString(), doctorPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("FirebaseSignUp","success");
                            mAuth.signInWithEmailAndPassword(doctorEmail.getText().toString(), doctorPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.i("FirebaseSignIn","success");

                                        TimingsModel timingsModel = new TimingsModel();
                                        timingsModel.setMondayStartTime(mondayStartTime);
                                        timingsModel.setMondayEndTime(mondayEndTime);
                                        timingsModel.setTuesdayStartTime(tuesdayStartTime);
                                        timingsModel.setTuesdayEndTime(tuesdayEndTime);
                                        timingsModel.setWednesdayStartTime(wednesdayStartTime);
                                        timingsModel.setWednesdayEndTime(wednesdayEndTime);
                                        timingsModel.setThursStartTime(thursStartTime);
                                        timingsModel.setThursEndTime(thursEndTime);
                                        timingsModel.setFriStartTime(friStartTime);
                                        timingsModel.setFriEndTime(friEndTime);
                                        timingsModel.setSatStartTime(satStartTime);
                                        timingsModel.setSatEndTime(satEndTime);
                                        timingsModel.setSundayStartTime(sunStartTime);
                                        timingsModel.setSundayEndTime(sunEndTime);

                                        currentUserID = mAuth.getCurrentUser().getUid();

                                        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Doctors");
                                        HashMap<String, Object> profileMap = new HashMap<>();
                                        DoctorModel doctorModel = new DoctorModel();
                                        if (idCount==0){
                                            doctorModel.setId(1l);
                                        }else {
                                            doctorModel.setId(idCount);
                                        }
                                        doctorModel.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        doctorModel.setTimingsModel(timingsModel);
                                        doctorModel.setName(doctorName.getText().toString());
                                        doctorModel.setCnic(doctorCnic.getText().toString());
                                        doctorModel.setEmail(doctorEmail.getText().toString());
                                        doctorModel.setHospital(doctorHospital.getText().toString());
                                        doctorModel.setPhone(doctorPhone.getText().toString());
                                        doctorModel.setSpecialization(doctorSpecialization.getText().toString());
                                        doctorModel.setUniversity(doctorUniversity.getText().toString());

                                        userRef.child(currentUserID).setValue(doctorModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = preferences.edit();
                                                    editor.putBoolean("isLoggedIn", true);
                                                    editor.putString("doctorName",doctorName.getText().toString());
                                                    editor.apply();

                                                    hideLoadingDialog();
                                                    startActivity(new Intent(getApplicationContext(), DoctorDashboard.class));
                                                } else {
                                                    hideLoadingDialog();
                                                    Toast.makeText(getApplicationContext(), "Error "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                        //  hideLoadingDialog();
                                    } else {
                                        //  hideLoadingDialog();
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            //  hideLoadingDialog();
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    protected void showLoadingDialog(String message) {
        loadingDialog = Loading_Dialog.ctor(this);
        loadingDialog.setMessage("");
        loadingDialog.show();
    }
    protected void hideLoadingDialog() {
        loadingDialog.dismiss();

    }
}