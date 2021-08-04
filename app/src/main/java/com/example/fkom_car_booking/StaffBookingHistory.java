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
import android.widget.Toast;

import com.example.fkom_car_booking.controller.Uadapter;
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

public class StaffBookingHistory extends AppCompatActivity implements Uadapter.onHistoryListener{

    RecyclerView historyView;
    Button backbtn, statusbtn;

    //arraylist
    ArrayList<Booking> list;

    //adapter
    Uadapter adapter;

    //database
    FirebaseAuth firebaseAuth;

    private static final String TAG = "BookingHistory";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_booking_history);
        Util.blackIconStatusBar(StaffBookingHistory.this, R.color.bgr);
        Log.d(TAG, "Currently At");

        backbtn = findViewById(R.id.backbtn);
        statusbtn = findViewById(R.id.statusbtn);
        historyView = (RecyclerView) findViewById(R.id.historyView);
        historyView.setHasFixedSize(true);

        list = new ArrayList<Booking>();
        adapter = new Uadapter(StaffBookingHistory.this,list,StaffBookingHistory.this::onHistoryClick);
        historyView.setAdapter(adapter);
        historyView.setLayoutManager( new LinearLayoutManager(this));

        //Database
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser fbUser = firebaseAuth.getCurrentUser();
        Log.v("UID", fbUser.getUid());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Booking").child(fbUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot1: snapshot.getChildren()){
                            Booking booking = dataSnapshot1.getValue(Booking.class);
                            list.add(booking);
                        }
                        adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StaffBookingHistory.this, "Something is Wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),StaffHome.class));
                finish();
            }
        });
    }

    @Override
    public void onHistoryClick(int position) {
        Log.d(TAG, "onHistoryClick: Clicked!" + position);
    }
}