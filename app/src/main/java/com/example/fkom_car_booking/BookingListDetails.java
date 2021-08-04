package com.example.fkom_car_booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.fkom_car_booking.controller.MySingleton;
import com.example.fkom_car_booking.controller.Util;
import com.example.fkom_car_booking.model.Booking;
import com.example.fkom_car_booking.model.Notify;
import com.example.fkom_car_booking.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookingListDetails extends AppCompatActivity {

    private TextView getUID, name, id, wO, dF, dT, cI, cO, purposes, dName, nop, locate, getBookID;
    private Button back, appbtn, rejbtn;
    Booking booking;
    Users users;
    //Database
    DatabaseReference databaseReference;
    ArrayList<Booking> list;

    private static final String TAG = "BookingListDetails";

    //Create new instance for Notify class
    Notify notify;

    //notification
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAvwt6S4I:APA91bGofgyM3zWk0GPpZxGLdagzmSvKm1XvJ8MJchX_LpuNqJi1gQsnha7-z7JH55y43YYsdaZfTHcO4VOlwpoWN7S_qc1E4SwATQgRd3m4h009R_LBnQ9znG6vcK3Rq8E4hWUbClt7";
    final private String contentType = "application/json";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list_details);
        Util.blackIconStatusBar(BookingListDetails.this, R.color.bgr);
        Log.d(TAG, "Currently At");

        getUID = findViewById(R.id.getUID);
        getBookID = findViewById(R.id.getBookID);
        name = findViewById(R.id.name);
        id = findViewById(R.id.id);
        wO = findViewById(R.id.wO);
        dF = findViewById(R.id.dF);
        dT = findViewById(R.id.dT);
        cI = findViewById(R.id.cI);
        cO = findViewById(R.id.cO);
        purposes = findViewById(R.id.purposes);
        locate = findViewById(R.id.locate);
        dName = findViewById(R.id.dName);
        nop = findViewById(R.id.nop);
        back = findViewById(R.id.back);
        appbtn = findViewById(R.id.appbtn);
        rejbtn = findViewById(R.id.rejbtn);
        list = new ArrayList<>();

        //create new instance for bookings class
        booking = new Booking();

        Intent intent = this.getIntent();
        String bookNo = intent.getStringExtra("Booking_No");
        String uID = intent.getStringExtra("UserID");
        getUID.setText(uID);
        getBookID.setText(bookNo);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Booking");
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
	
	//retrieve data from 2 tables (users and booking)
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot buID : snapshot.getChildren()) {
                    Users users = buID.getValue(Users.class);

                    if (users.getUserID() != null && getUID.getText() != null && users.getUserID().equals(getUID.getText())) {
                        name.setText(users.getStaffName());
                        id.setText(users.getStaffID());
                    }

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot bookingDetails : snapshot.getChildren()) {
                                for (DataSnapshot bookingDetails1 : bookingDetails.getChildren()) {
                                    Booking booking = bookingDetails1.getValue(Booking.class);

                                    if (booking.getBookID() != null && getBookID.getText() != null && booking.getBookID().equals(getBookID.getText())) {
                                        wO.setText(booking.getWorkorder());
                                        dF.setText(booking.getDateFrom());
                                        dT.setText(booking.getDateTo());
                                        cI.setText(booking.getTimeIn());
                                        cO.setText(booking.getTimeOut());
                                        purposes.setText(booking.getProgramme());
                                        locate.setText(booking.getAddress());
                                        dName.setText(booking.getDrivername());
                                        nop.setText(String.valueOf(booking.getNopassanger()));
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        appbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyUser();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminBookingList.class));
                finish();
            }
        });

        rejbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectBooking();
            }
        });
    }


    //method for send notification for reject booking
    private void rejectBooking() {
        //Start add into notification fragment process
        notify = new Notify();
        DatabaseReference NotRef = FirebaseDatabase.getInstance().getReference().child("Notify");
        notify.setBookID(getBookID.getText().toString());
        notify.setUserID(getUID.getText().toString());
        String key = NotRef.push().getKey();
        notify.setNotifyID(key);
        NotRef.child(key).setValue(notify);

        //Update notify status
        DatabaseReference dbNotify = FirebaseDatabase.getInstance().getReference("Booking").child(getUID.getText().toString()).child(getBookID.getText().toString());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("notifyStatus", "Rejected");
        dbNotify.updateChildren(hashMap);

        //Send push notification
        String StaffID = id.getText().toString();
        TOPIC = "/topics/" + StaffID.toLowerCase(); //topic must match with what the receiver subscribed to
        Log.v(TAG,StaffID);
        NOTIFICATION_TITLE = "FKOM Car Booking";
        NOTIFICATION_MESSAGE = "Dear " + name.getText().toString() + ", Your Booking " + getBookID.getText().toString() + " is rejected!";

        JSONObject notification = new JSONObject();
        JSONObject notificationBody = new JSONObject();
        try {
            notificationBody.put("title", NOTIFICATION_TITLE);
            notificationBody.put("message", NOTIFICATION_MESSAGE);

            notification.put("to", TOPIC);
            notification.put("data", notificationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage());
        }
        sendNotify(notification, StaffID);
    }

    //method for send notification for reject booking
    private void sendNotify(JSONObject notification, String staffID) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(BookingListDetails.this,  ""+ staffID + " has been Notified!", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onResponse: " + response);
                        startActivity(new Intent(getApplicationContext(), AdminBookingList.class));
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BookingListDetails.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    //method for send notification for approved booking
    private void notifyUser() {
        //Start add into notification fragment process
        notify = new Notify();
        DatabaseReference NotRef = FirebaseDatabase.getInstance().getReference().child("Notify");
        notify.setBookID(getBookID.getText().toString());
        notify.setUserID(getUID.getText().toString());
        String key = NotRef.push().getKey();
        notify.setNotifyID(key);
        NotRef.child(key).setValue(notify);

        //Update notify status
        DatabaseReference dbNotify = FirebaseDatabase.getInstance().getReference("Booking").child(getUID.getText().toString()).child(getBookID.getText().toString());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("notifyStatus", "Approved");
        dbNotify.updateChildren(hashMap);

        //Send push notification
        String StaffID = id.getText().toString();
        TOPIC = "/topics/" + StaffID.toLowerCase(); //topic must match with what the receiver subscribed to
        Log.v(TAG,StaffID);
        NOTIFICATION_TITLE = "FKOM Car Booking";
        NOTIFICATION_MESSAGE = "Dear " + name.getText().toString() + ", Your Booking " + getBookID.getText().toString() + " has been approved!";

        JSONObject notification = new JSONObject();
        JSONObject notificationBody = new JSONObject();
        try {
            notificationBody.put("title", NOTIFICATION_TITLE);
            notificationBody.put("message", NOTIFICATION_MESSAGE);

            notification.put("to", TOPIC);
            notification.put("data", notificationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage());
        }
        sendNotification(notification, StaffID);
    }

    //method for send notification for approved booking
    private void sendNotification(JSONObject notification, final String staffID) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(BookingListDetails.this,  ""+ staffID + " has been Notified!", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onResponse: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BookingListDetails.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}