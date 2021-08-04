package com.example.fkom_car_booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class StaffProfile extends AppCompatActivity {

    TextView nameinfo, idinfo, mailinfo;
    Button backbtn;
    ImageView logout;
    String StaffID;
    private static final String TAG = "StaffProfile";

    //Database
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_profile);
        Util.blackIconStatusBar(StaffProfile.this, R.color.bgr);
        Log.d(TAG, "Currently at:");

        nameinfo = findViewById(R.id.nameinfo);
        idinfo = findViewById(R.id.idinfo);
        mailinfo = findViewById(R.id.mailinfo);
        backbtn = findViewById(R.id.backbtn);
        logout = findViewById(R.id.logout);

        //database
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser fbUser = firebaseAuth.getCurrentUser();
        Log.v("User ID:", fbUser.getUid());
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Users").child(fbUser.getUid());

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                nameinfo.setText(users.getStaffName());
                StaffID = users.getStaffID();
                idinfo.setText(users.getStaffID());
                mailinfo.setText(users.getStaffEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StaffProfile.this, "User profile not found!",Toast.LENGTH_SHORT).show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Logging out "+StaffID.toLowerCase(), Toast.LENGTH_LONG).show();
                FirebaseMessaging.getInstance().unsubscribeFromTopic(StaffID.toLowerCase());
                firebaseAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),StaffHome.class));
                finish();
            }
        });
    }
}