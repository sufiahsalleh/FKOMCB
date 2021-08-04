package com.example.fkom_car_booking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.fkom_car_booking.controller.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StaffBooking extends AppCompatActivity {

    TextView dateFrom, dateTo, checkin, checkout;
    Button backbtn, nextbtn;

    //Time Picker
    int t1Hour, t1Minute, t2Hour, t2Minute;

    //Date Picker Dialogue
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener nDateSetListener;

    private static final String TAG = "StaffBooking";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_booking);
        Util.blackIconStatusBar(StaffBooking.this, R.color.bgr);
        Log.d(TAG, "Currently At ");

        backbtn = findViewById(R.id.backbtn);
        nextbtn = findViewById(R.id.nextbtn);
        dateFrom = findViewById(R.id.dateFrom);
        dateTo = findViewById(R.id.dateTo);
        checkin = findViewById(R.id.timein);
        checkout = findViewById(R.id.timeout);

        //calendar
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(
                        StaffBooking.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month +1;
                        String dateF = dayOfMonth + "/" + month + "/" + year;
                        dateFrom.setText(dateF);
                    }
                },year,month,day);
                //Disable past date
                dialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                //show datepickerdialogue
                dialog.show();
            }
        });



        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(
                        StaffBooking.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        String dateT = dayOfMonth + "/" + month + "/" + year;
                        dateTo.setText(dateT);
                    }
                },year,month,day
                );
                //Disable past date
                dialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                //show datepickerdialogue
                dialog.show();
            }
        });

        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        StaffBooking.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                t1Hour = i;
                                t1Minute = i1;

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,t1Hour,t1Minute);

                                checkin.setText(DateFormat.format("hh:mm aa", calendar));
                            }
                        }, 12,0,false
                );
                timePickerDialog.updateTime(t1Hour,t1Minute);
                timePickerDialog.show();
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        StaffBooking.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                t2Hour = i;
                                t2Minute = i1;

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0,0,0,t2Hour,t2Minute);

                                checkout.setText(DateFormat.format("hh:mm aa", calendar));
                            }
                        }, 12,0,false
                );
                timePickerDialog.updateTime(t2Hour,t2Minute);
                timePickerDialog.show();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),StaffHome.class));
                finish();
            }
        });


        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dateFrom.length() == 0) {
                    dateFrom.setError("Please enter date from!");
                    dateFrom.requestFocus();
                    return;
                } else if(dateTo.length() == 0){
                    dateTo.setError("Please enter date to!");
                    dateTo.requestFocus();
                    return;
                } else if(checkin.length() == 0){
                    checkin.setError("Please enter time in!");
                    checkin.requestFocus();
                    return;
                } else if (checkout.length() == 0) {
                    checkout.setError("Please enter time out!");
                    checkout.requestFocus();
                } else {
                    //declare intent from Booking to BookingDetails activity
                    Intent intentBD = new Intent(StaffBooking.this, StaffBookingDetails.class);
                    //bawak info dari si ke textview di interface B activtiy
                    intentBD.putExtra("datef", dateFrom.getText().toString());
                    intentBD.putExtra("datet", dateTo.getText().toString());
                    intentBD.putExtra("timein", checkin.getText().toString());
                    intentBD.putExtra("timeout", checkout.getText().toString());
                    //hantar info dari edit text ke interface bookingdetails // ada forresult sebab dari booking
                    startActivityForResult(intentBD, 125);
                }
            }
        });
    }
}