package com.example.fkom_car_booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.fkom_car_booking.controller.Aadapter;
import com.example.fkom_car_booking.controller.Util;
import com.example.fkom_car_booking.model.Booking;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminBookingList extends AppCompatActivity implements Aadapter.onDetailsListener {

    RecyclerView listview;
    ArrayList<Booking> list;
    Aadapter adapter;
    Button backbtn, statusbtn;

    //Database
    DatabaseReference reference;

    private static final String TAG = "listapproval";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_booking_list);

        Util.blackIconStatusBar(AdminBookingList.this, R.color.bgr);
        Log.d(TAG, "Currently At");
        listview = (RecyclerView) findViewById(R.id.ApprovalList);
        listview.setHasFixedSize(true);
        listview.setLayoutManager( new LinearLayoutManager(this));
        backbtn = findViewById(R.id.backbtn);
        statusbtn = findViewById(R.id.statusbtn);

        reference = FirebaseDatabase.getInstance().getReference().child("Booking");
        list = new ArrayList<Booking>();
        adapter = new Aadapter(AdminBookingList.this,list,this::onDetailsClick);
        listview.setAdapter(adapter);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1: snapshot.getChildren()){
                    for(DataSnapshot BookingSnapshot: dataSnapshot1.getChildren()){
                        Booking bookings = BookingSnapshot.getValue(Booking.class);
                        list.add(bookings);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AdminHome.class));
                finish();
            }
        });
    }

    @Override
    public void onDetailsClick(int position) {
        Log.d(TAG, "onDetailsClick: Clicked!" + position);
    }
}