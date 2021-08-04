package com.example.fkom_car_booking;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fkom_car_booking.controller.Util;
import com.example.fkom_car_booking.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneMultiFactorAssertion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    EditText usernameET, staffIdET, staffEmailET, passwordET;
    Spinner usercategory;
    Button registerBtn;
    TextView loginTV;
    private static final String TAG = "Register";

    //Model
    Users users;

    //Database
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Util.blackIconStatusBar(Register.this, R.color.bgr);

        Log.d(TAG, "Currently At:");
        usernameET = findViewById(R.id.usernameET);
        staffIdET = findViewById(R.id.staffIdET);
        staffEmailET = findViewById(R.id.staffEmailET);
        passwordET = findViewById(R.id.passwordET);
        registerBtn = findViewById(R.id.registerBtn);
        loginTV = findViewById(R.id.loginTV);
        usercategory = findViewById(R.id.usercategory);

        ArrayAdapter<CharSequence> usercat = ArrayAdapter.createFromResource(this, R.array.UserCategory, android.R.layout.simple_spinner_item);
        usercat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        usercategory.setAdapter(usercat);

        //Database Initialization
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //new instances for Users Class
        users = new Users();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set and Get all the users value
                String StaffName = usernameET.getText().toString().trim();
                String StaffID = staffIdET.getText().toString().trim();
                String StaffEmail = staffEmailET.getText().toString().trim();
                String Password = passwordET.getText().toString().trim();
                String type = usercategory.getSelectedItem().toString().trim();

                if (StaffName.length() == 0) {
                    usernameET.setError("Please enter your Name");
                    usernameET.requestFocus();
                    return;
                } else if (StaffID.length() == 0) {
                    staffIdET.setError("Please enter your Staff ID");
                    staffIdET.requestFocus();
                    return;
                } else if (StaffEmail.length() == 0) {
                    staffEmailET.setError("Please enter Staff UMP Email");
                    staffEmailET.requestFocus();
                    return;
                } else if(!Patterns.EMAIL_ADDRESS.matcher(StaffEmail).matches()) {
                    staffEmailET.setError("Please provide valid email!");
                    staffEmailET.requestFocus();
                    return;
                } else if (Password.length() == 0) {
                    passwordET.setError("Please enter Password");
                    passwordET.requestFocus();
                    return;
                } else if(Password.length() < 8){
                    passwordET.setError("Minimum password length should be 8 characters. Try again.");
                    passwordET.requestFocus();
                    return;
                }
                else {
                    register(StaffName, StaffID, StaffEmail, Password, type);
                }
            }
        });

        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(Register.this, Login.class);
                startActivity(login);
            }
        });
    }

    private void register(String staffName, String staffID, String staffEmail, String password, String type) {
        firebaseAuth.createUserWithEmailAndPassword(staffEmail, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            final String userid = firebaseUser.getUid();

                            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("userID", userid);
                            hashMap.put("StaffName", staffName);
                            hashMap.put("StaffID", staffID);
                            hashMap.put("StaffEmail", staffEmail);
                            hashMap.put("type", type);

                            dbref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(Register.this, "Registration is successful!", Toast.LENGTH_SHORT).show();
                                        dbref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Users users = snapshot.getValue(Users.class);
                                                if (users.getType().equals("Admin")){
                                                    Intent intent = new Intent(Register.this, AdminHome.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else{
                                                    Intent intent = new Intent(Register.this, StaffHome.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    } else{
                                        Toast.makeText(Register.this, "Registration failed! Try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else{
                            Toast.makeText(Register.this, "Registration failed! Try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
