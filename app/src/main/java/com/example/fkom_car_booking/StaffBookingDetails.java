package com.example.fkom_car_booking;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fkom_car_booking.controller.Util;
import com.example.fkom_car_booking.model.Booking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StaffBookingDetails extends AppCompatActivity {

    Button backbtn, nextbtn;
    TextView dateF, dateT, Cin, Cout;
    EditText purp, driver,location, workorderET, programmeET;
    Spinner passenger;

    //Database
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    //create new instance for Bookings class
    Booking booking;

    //array
    String[] Passenger = {"0", "1", "2", "3", "4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_booking_details);
        Util.blackIconStatusBar(StaffBookingDetails.this, R.color.bgr);

        dateF = findViewById(R.id.dateFrom);
        dateT = findViewById(R.id.dateTo);
        Cin = findViewById(R.id.checkin);
        Cout = findViewById(R.id.checkout);
        workorderET = findViewById(R.id.workorderET);
        purp = findViewById(R.id.programmeET);
        location = findViewById(R.id.location);
        driver = findViewById(R.id.drivers);
        passenger = findViewById(R.id.passenger);
        backbtn = findViewById(R.id.backbtn);
        nextbtn = findViewById(R.id.nextbtn);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //to get current user
        FirebaseUser fbUser = firebaseAuth.getCurrentUser();
        Log.v("User ID", fbUser.getUid());

        //create new instance for bookings class
        booking = new Booking();

        //Refer to firebase
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Booking").child(fbUser.getUid());

        //Set user category drop down button
        ArrayAdapter passangerArray = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Passenger);
        passangerArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        passenger.setAdapter(passangerArray);

        dateF.setText(getIntent().getStringExtra("datef"));
        dateT.setText(getIntent().getStringExtra("datet"));
        Cin.setText(getIntent().getStringExtra("timein"));
        Cout.setText(getIntent().getStringExtra("timeout"));

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set and get data value from user
                booking.setUserID(fbUser.getUid());
                booking.setWorkorder(workorderET.getText().toString().trim());
                booking.setProgramme(purp.getText().toString().trim());
                booking.setDateFrom(dateF.getText().toString().trim());
                booking.setDateTo(dateT.getText().toString().trim());
                booking.setTimeIn(Cin.getText().toString().trim());
                booking.setTimeOut(Cout.getText().toString().trim());
                booking.setAddress(location.getText().toString().trim());
                booking.setDrivername(driver.getText().toString().trim());
                booking.setNopassanger(Integer.valueOf(passenger.getSelectedItem().toString()));
                booking.setNotifyStatus("Pending");

                if (workorderET.length() == 0) {
                    workorderET.setError("Please enter work order number");
                    workorderET.requestFocus();
                    return;
                } else if (purp.length() == 0) {
                    purp.setError("Please enter programme name");
                    purp.requestFocus();
                    return;
                } else if (location.length() == 0) {
                    location.setError("Please enter location");
                    location.requestFocus();
                } else if (driver.length() == 0) {
                    driver.setError("Please enter driver's name");
                    driver.requestFocus();
                    return;
                } else{
                    //Push the value into database
                    String BookID = FirebaseDatabase.getInstance().getReference().child("Booking").child(fbUser.getUid()).push().getKey();
                    Log.e("onClick: ", BookID);
                    booking.setBookID(BookID);
                    dbref.child(BookID).setValue(booking);
                    //databaseReference.push().setValue(parcel);

                    //Show successful toast messages
                    Toast.makeText(StaffBookingDetails.this, "New Bookings has been successfully done!", Toast.LENGTH_SHORT).show();
                    Intent sendBack = new Intent(StaffBookingDetails.this, StaffHome.class);
                    startActivity(sendBack);
                }
            }
        });

                        backbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent sendBack = new Intent(StaffBookingDetails.this, StaffBooking.class);
                                sendBack.putExtra("from", dateF.getText().toString());
                                sendBack.putExtra("to", dateT.getText().toString());
                                sendBack.putExtra("cin", Cin.getText().toString());
                                sendBack.putExtra("co", Cout.getText().toString()); //send info from B to main
                                setResult(RESULT_OK, sendBack);
                                finish();
                            }
                        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        StaffBookingDetails.super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==125 && resultCode==RESULT_OK){ //untuk makesure data B sampai ke main semula
            dateF.setText(data.getStringExtra("from"));
            dateT.setText(data.getStringExtra("to"));
            Cin.setText(data.getStringExtra("cin"));
            Cout.setText(data.getStringExtra("co"));
        }

    }
}