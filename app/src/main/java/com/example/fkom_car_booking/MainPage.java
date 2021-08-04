package com.example.fkom_car_booking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fkom_car_booking.controller.Util;
import com.example.fkom_car_booking.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.net.UnknownServiceException;

public class MainPage extends AppCompatActivity {
    Button signinbtn, signupbtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Util.blackIconStatusBar(MainPage.this, R.color.bgr);

        signinbtn = findViewById(R.id.signinbtn);
        signupbtn = findViewById(R.id.signupbtn);

        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Login = new Intent(MainPage.this, Login.class);
                startActivity(Login);
                finish();
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Register = new Intent(MainPage.this, Register.class);
                startActivity(Register);
                finish();
            }
        });
    }
}