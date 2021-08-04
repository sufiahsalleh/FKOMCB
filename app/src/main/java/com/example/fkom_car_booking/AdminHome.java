package com.example.fkom_car_booking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fkom_car_booking.controller.Util;
import com.example.fkom_car_booking.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class AdminHome extends AppCompatActivity implements View.OnClickListener {

    private CardView appbtn, profilebtn;
    private static final String TAG = "AdminHome";
    Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        Util.blackIconStatusBar(AdminHome.this, R.color.bgr);

        appbtn = (CardView) findViewById(R.id.appbtn);
        profilebtn = (CardView) findViewById(R.id.profilebtn);

        appbtn.setOnClickListener(this);
        profilebtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;

        switch (view.getId()){
            case R.id.appbtn : i = new Intent(this,AdminBookingList.class); startActivity(i) ; break;
            case R.id.profilebtn : i =
                new Intent(this,AdminProfile.class); startActivity(i) ; break;
            default:break;
        }
    }
}