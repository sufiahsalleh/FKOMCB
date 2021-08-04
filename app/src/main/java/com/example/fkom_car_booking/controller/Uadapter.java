package com.example.fkom_car_booking.controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fkom_car_booking.R;
import com.example.fkom_car_booking.StaffHistoryDetails;
import com.example.fkom_car_booking.model.Booking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class Uadapter extends RecyclerView.Adapter<Uadapter.myViewHolder> {

    Context context;
    ArrayList<Booking> booking;
    onHistoryListener monHistoryListener;

    public Uadapter(Context c, ArrayList<Booking> b, onHistoryListener onHistoryListener) {
        context = c;
        booking = b;
        this.monHistoryListener = onHistoryListener;
    }


    @NonNull
    @Override
    public Uadapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.historylayout,parent,false);
        return new Uadapter.myViewHolder(view,monHistoryListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Uadapter.myViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: STARTED.");
        final Booking booking = this.booking.get(position);
        holder.getBID.setText(booking.getBookID());
        holder.bpurp.setText(booking.getProgramme());
        holder.dateF.setText(booking.getDateFrom());
        holder.timeI.setText(booking.getTimeIn());
        holder.statusbtn.setText(booking.getNotifyStatus());
        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth firebaseAuth;
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser fbUser = firebaseAuth.getCurrentUser();
                Log.v("UID", fbUser.getUid());
                DatabaseReference  dbBook = FirebaseDatabase.getInstance().getReference().child("Booking").child(fbUser.getUid()).child(booking.getBookID());
                        dbBook.removeValue();
                Toast.makeText(context, "Booking Application Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return booking.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView bpurp, dateF, timeI, getBID;
        Button statusbtn;
        ImageView deletebtn;
        onHistoryListener onHistoryListener;
        RelativeLayout historylayout;

        public myViewHolder(View itemView, onHistoryListener onHistoryListener) {
            super(itemView);
            getBID = (TextView) itemView.findViewById(R.id.getBID);
            bpurp = (TextView) itemView.findViewById(R.id.bpurp);
            dateF = (TextView) itemView.findViewById(R.id.dateF);
            timeI = (TextView) itemView.findViewById(R.id.timeI);
            statusbtn = (Button) itemView.findViewById(R.id.statusbtn);
            historylayout = itemView.findViewById(R.id.historyView);
            deletebtn = (ImageView) itemView.findViewById(R.id.deletebtn);
            this.onHistoryListener = onHistoryListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onHistoryListener.onHistoryClick(getAdapterPosition());

            Intent intent = new Intent(v.getContext(), StaffHistoryDetails.class);
            intent.putExtra("BOOK_ID", getBID.getText());
            v.getContext().startActivity(intent); //go to next activity
        }

    }

    public interface onHistoryListener{
        void onHistoryClick (int position);
    }
}
