package com.example.fkom_car_booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fkom_car_booking.controller.Util;
import com.example.fkom_car_booking.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class Login extends AppCompatActivity {

    EditText emailtxt, passtxt;
    TextView RegisterTV;
    Button loginbtn;
    private static final String TAG = "Login";

    //Model
    Users users;

    //Database
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Util.blackIconStatusBar(Login.this, R.color.bgr);

        emailtxt = findViewById(R.id.emailtxt);
        passtxt = findViewById(R.id.passtxt);
        RegisterTV = findViewById(R.id.RegisterTV);
        loginbtn = findViewById(R.id.loginbtn);

        //Database Initialization
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        RegisterTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
                finish();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = emailtxt.getText().toString().trim();
                String Password = passtxt.getText().toString().trim();

                if(Email.isEmpty()){
                    emailtxt.setError("Email is required!");
                    emailtxt.requestFocus();
                    return;
                } else if(Password.isEmpty()){
                    passtxt.setError("Password is required!");
                    passtxt.requestFocus();
                    return;
                } else {
                    firebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                FirebaseUser fbUser = firebaseAuth.getCurrentUser();
                                Log.v("User ID:", fbUser.getUid());

                                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(fbUser.getUid());

                                databaseReference.addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Users users = snapshot.getValue(Users.class);
                                        Log.v("Staff ID:", users.getStaffID().toLowerCase());
                                        //Listen to firebase messaging topic to receive notification by username (for example : `CB00001`)
                                        FirebaseMessaging.getInstance().subscribeToTopic(users.getStaffID().toLowerCase());

                                        //Start here for login based on user
                                        if (users.getType().equals("Admin")){
                                            startActivity(new Intent(Login.this, AdminHome.class));
                                        }else{
                                            startActivity(new Intent(Login.this, StaffHome.class));
                                        }
                                        //End here
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            } else {
                                Toast.makeText(Login.this, "Failed to login. Please check your Email or Password!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
    }
}