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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StaffHistoryDetails extends AppCompatActivity {

    private TextView dF, dT, cI, cO, wo, purposes, dName, nop, locate, getBID;
    Button back;

    //Database
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    //List
    ArrayList<Booking> list;

    private static final String TAG = "HistoryDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_history_details);
        Util.blackIconStatusBar(StaffHistoryDetails.this, R.color.bgr);
        Log.d(TAG, "Currently At");

        getBID = findViewById(R.id.getBID);
        dF = findViewById(R.id.dF);
        dT = findViewById(R.id.dT);
        cI = findViewById(R.id.cI);
        cO = findViewById(R.id.cO);
        wo = findViewById(R.id.wo);
        purposes = findViewById(R.id.purposes);
        locate = findViewById(R.id.locate);
        dName= findViewById(R.id.dName);
        nop = findViewById(R.id.nop);
        back = findViewById(R.id.back);
        list = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser fbUser = firebaseAuth.getCurrentUser();
        Log.v("UID", fbUser.getUid());

        Intent intent = this.getIntent();
        String booknum = intent.getStringExtra("BOOK_ID");
        getBID.setText(booknum);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Booking").child(fbUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot bookingDetails : snapshot.getChildren()){
                    Booking booking = bookingDetails.getValue(Booking.class);
                    if(booking.getBookID() != null && getBID.getText() != null && booking.getBookID().equals(getBID.getText())){
                        dF.setText(booking.getDateFrom());
                        dT.setText(booking.getDateTo());
                        cI.setText(booking.getTimeIn());
                        cO.setText(booking.getTimeOut());
                        wo.setText(booking.getWorkorder());
                        purposes.setText(booking.getProgramme());
                        locate.setText(booking.getAddress());
                        dName.setText(booking.getDrivername());
                        nop.setText(String.valueOf(booking.getNopassanger()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),StaffBookingHistory.class));
                finish();
            }
        });
    }
}