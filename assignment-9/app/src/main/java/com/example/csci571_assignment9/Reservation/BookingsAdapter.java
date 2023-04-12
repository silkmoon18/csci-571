package com.example.csci571_assignment9.Reservation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.csci571_assignment9.R;

import java.util.ArrayList;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.RecyclerViewHolder>{
    final ArrayList<BookingData> bookingDataList;

    public BookingsAdapter(ArrayList<BookingData> bookingDataList) {
        this.bookingDataList = bookingDataList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_card, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        BookingData bookingData = this.bookingDataList.get(position);

        holder.number.setText(String.valueOf(position + 1));
        holder.name.setText(bookingData.getname());
        holder.date.setText(bookingData.getdate());
        holder.time.setText(bookingData.gettime());
        holder.email.setText(bookingData.getemail());
    }

    @Override
    public int getItemCount() {
        return bookingDataList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private final TextView number;
        private final TextView name;
        private final TextView date;
        private final TextView time;
        private final TextView email;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            number = itemView.findViewById(R.id.text_booking_number);
            name = itemView.findViewById(R.id.text_booking_name);
            date = itemView.findViewById(R.id.text_booking_date);
            time = itemView.findViewById(R.id.text_booking_time);
            email = itemView.findViewById(R.id.text_booking_email);
        }
    }
}
