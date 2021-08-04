package com.example.fkom_car_booking.controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fkom_car_booking.BookingListDetails;
import com.example.fkom_car_booking.R;
import com.example.fkom_car_booking.StaffViewDetails;
import com.example.fkom_car_booking.model.Booking;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.myViewHolder> {
    Context context;
    ArrayList<Booking> bookings;
    Aadapter.onDetailsListener monDetailsListener;

    public ViewAdapter(Context c, ArrayList<Booking> b, Aadapter.onDetailsListener onDetailsListener) {
        context = c;
        bookings = b;
        this.monDetailsListener = onDetailsListener;
    }


    @NonNull
    @Override
    public ViewAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewlistlayout,parent,false);
        return new ViewAdapter.myViewHolder(view,monDetailsListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAdapter.myViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: STARTED.");
        holder.bookingID.setText(bookings.get(position).getBookID());
        holder.userID.setText(bookings.get(position).getUserID());
        holder.dateF.setText(bookings.get(position).getDateFrom());
        holder.timeI.setText(bookings.get(position).getTimeIn());
        holder.statusbtn.setText(bookings.get(position).getNotifyStatus());
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView bookingID, userID, dateF, timeI;
        Button statusbtn;
        RelativeLayout parentlayout;
        Aadapter.onDetailsListener onDetailsListener;

        public myViewHolder(View itemView, Aadapter.onDetailsListener onDetailsListener) {
            super(itemView);
            statusbtn = (Button) itemView.findViewById(R.id.statusbtn);
            userID = (TextView) itemView.findViewById(R.id.userID);
            bookingID = (TextView) itemView.findViewById(R.id.bookingID);
            dateF = (TextView) itemView.findViewById(R.id.dateF);
            timeI = (TextView) itemView.findViewById(R.id.timeI);
            parentlayout = itemView.findViewById(R.id.ApprovalList);
            this.onDetailsListener = onDetailsListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onDetailsListener.onDetailsClick(getAdapterPosition());
            Intent intent = new Intent(v.getContext(), StaffViewDetails.class);
            intent.putExtra("Booking_No", bookingID.getText());
            intent.putExtra("UserID", userID.getText());
            v.getContext().startActivity(intent); //go to next activity
        }
    }

    public interface onDetailsListener{
        void onDetailsClick (int position);
    }
}
