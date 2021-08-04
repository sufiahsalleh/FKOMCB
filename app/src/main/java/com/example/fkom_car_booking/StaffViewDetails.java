package com.example.fkom_car_booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fkom_car_booking.controller.Util;
import com.example.fkom_car_booking.model.Booking;
import com.example.fkom_car_booking.model.Notify;
import com.example.fkom_car_booking.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StaffViewDetails extends AppCompatActivity {

    private TextView getUID, name, id, wO, dF, dT, cI, cO, purposes, dName, nop, locate, getBookID;
    private Button back;
    Booking booking;
    Users users;

    //Database
    DatabaseReference databaseReference;
    ArrayList<Booking> list;

    private static final String TAG = "StaffViewDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_view_details);
        Util.blackIconStatusBar(StaffViewDetails.this, R.color.bgr);
        Log.d(TAG, "Currently At");

        getUID = findViewById(R.id.getUID);
        getBookID = findViewById(R.id.getBookID);
        name = findViewById(R.id.name);
        id = findViewById(R.id.id);
        wO = findViewById(R.id.wO);
        dF = findViewById(R.id.dF);
        dT = findViewById(R.id.dT);
        cI = findViewById(R.id.cI);
        cO = findViewById(R.id.cO);
        purposes = findViewById(R.id.purposes);
        locate = findViewById(R.id.locate);
        dName = findViewById(R.id.dName);
        nop = findViewById(R.id.nop);
        back = findViewById(R.id.back);
        list = new ArrayList<>();

        //create new instance for bookings class
        booking = new Booking();

        Intent intent = this.getIntent();
        String bookNo = intent.getStringExtra("Booking_No");
        String uID = intent.getStringExtra("UserID");
        getUID.setText(uID);
        getBookID.setText(bookNo);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Booking");
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users");

        //retrieve data from 2 tables (users and booking)
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot buID : snapshot.getChildren()) {
                    Users users = buID.getValue(Users.class);

                    if (users.getUserID() != null && getUID.getText() != null && users.getUserID().equals(getUID.getText())) {
                        name.setText(users.getStaffName());
                        id.setText(users.getStaffID());
                    }

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot bookingDetails : snapshot.getChildren()) {
                                for (DataSnapshot bookingDetails1 : bookingDetails.getChildren()) {
                                    Booking booking = bookingDetails1.getValue(Booking.class);

                                    if (booking.getBookID() != null && getBookID.getText() != null && booking.getBookID().equals(getBookID.getText())) {
                                        wO.setText(booking.getWorkorder());
                                        dF.setText(booking.getDateFrom());
                                        dT.setText(booking.getDateTo());
                                        cI.setText(booking.getTimeIn());
                                        cO.setText(booking.getTimeOut());
                                        purposes.setText(booking.getProgramme());
                                        locate.setText(booking.getAddress());
                                        dName.setText(booking.getDrivername());
                                        nop.setText(String.valueOf(booking.getNopassanger()));
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), StaffViewAvailability.class));
                finish();
            }
        });
    }
}