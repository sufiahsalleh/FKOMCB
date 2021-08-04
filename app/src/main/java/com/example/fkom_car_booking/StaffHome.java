package com.example.fkom_car_booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fkom_car_booking.controller.Util;
import com.example.fkom_car_booking.model.Booking;
import com.example.fkom_car_booking.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class StaffHome extends AppCompatActivity implements View.OnClickListener {

    private CardView profilebtn, bookbtn, availabilitybtn, bookhist;
    private static final String TAG = "StaffHome";
    Users users;

    //database
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);
        Util.blackIconStatusBar(StaffHome.this, R.color.bgr);

        profilebtn = (CardView) findViewById(R.id.profilebtn);
        bookbtn = (CardView) findViewById(R.id.bookbtn);
        bookhist = (CardView) findViewById(R.id.bookhist);
        availabilitybtn = (CardView) findViewById(R.id.availabilitybtn);

        profilebtn.setOnClickListener(this);
        availabilitybtn.setOnClickListener(this);
        bookbtn.setOnClickListener(this);
        bookhist.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;

        switch (view.getId()){
            case R.id.profilebtn : i = new Intent(this,StaffProfile.class); startActivity(i) ; break;
            case R.id.availabilitybtn : i = new Intent(this,StaffViewAvailability.class); startActivity(i) ; break;
            case R.id.bookbtn : i = new Intent(this, StaffBooking.class); startActivity(i) ; break;
            case R.id.bookhist : i = new Intent(this,StaffBookingHistory.class); startActivity(i) ; break;
            default:break;
        }
    }
}