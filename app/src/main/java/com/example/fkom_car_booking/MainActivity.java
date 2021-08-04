package com.example.fkom_car_booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fkom_car_booking.controller.Util;
import com.example.fkom_car_booking.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    ImageView logo;
    TextView appname;

    //Database
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Util.blackIconStatusBar(MainActivity.this, R.color.bgr);

        logo = findViewById(R.id.logo);
        appname = findViewById(R.id.appname);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser fbUser = firebaseAuth.getCurrentUser();
        if (fbUser != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(fbUser.getUid());

            databaseReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Users users = snapshot.getValue(Users.class);
                    Log.v("Staff ID:", users.getStaffID().toLowerCase());
                    //Listen to firebase messaging topic to receive notification by username (for example : `CB00001`)
                    FirebaseMessaging.getInstance().subscribeToTopic(users.getStaffID().toLowerCase());

                    //Start here for login based on user
                    if (users.getType().equals("Admin")) {
                        startActivity(new Intent(MainActivity.this, AdminHome.class));
                    } else {
                        startActivity(new Intent(MainActivity.this, StaffHome.class));
                    }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent next = new Intent(MainActivity.this, MainPage.class);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,
                            Pair.create(logo,"logo"),
                            Pair.create(appname,"name"));
                    startActivity(next, options.toBundle());
                }
            }, 2000);
        }
    }
}