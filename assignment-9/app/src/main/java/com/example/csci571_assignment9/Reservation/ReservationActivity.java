package com.example.csci571_assignment9.Reservation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.csci571_assignment9.R;
import com.example.csci571_assignment9.Search.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class ReservationActivity extends AppCompatActivity {

    Toolbar toolbar;

    TextView noFound;

    RecyclerView recyclerView;
    BookingsAdapter bookingsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        noFound = findViewById(R.id.text_bookings_noFound);
        recyclerView = findViewById(R.id.recyclerView_bookings_container);
        toolbar = findViewById(R.id.toolbar_reservation);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        setBookingsView();

        updateNoFoundVisibility();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setBookingsView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        bookingsAdapter = new BookingsAdapter(MainActivity.bookingDataList);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(bookingsAdapter);
        Context context = this;

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;
                ColorDrawable background = new ColorDrawable(Color.parseColor("#c10000"));
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                Drawable icon = ContextCompat.getDrawable(context, R.drawable.ic_delete);
                assert icon != null;
                int intrinsicWidth = icon.getIntrinsicWidth();
                int intrinsicHeight = icon.getIntrinsicHeight();
                int height = itemView.getHeight();

                int margin = (height - intrinsicHeight) / 2;
                int top = itemView.getTop() + (height - intrinsicHeight) / 2;
                int left = itemView.getRight() - margin - intrinsicWidth;
                int right = itemView.getRight() - margin;
                int bot = top + intrinsicHeight;

                icon.setBounds(left, top, right, bot);
                icon.draw(c);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                MainActivity.deleteReservation(position);

                bookingsAdapter.notifyItemRemoved(position);
                bookingsAdapter.notifyItemRangeChanged(position, MainActivity.bookingDataList.size());

                updateNoFoundVisibility();

                makeSnackbar();
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void makeSnackbar() {
        Snackbar snackbar = Snackbar.make(recyclerView, getResources().getString(R.string.text_bookings_removing),
                Snackbar.LENGTH_LONG);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snackbar.getView().getLayoutParams();
        params.setMargins(20, 0, 20, 20);
        snackbar.getView().setLayoutParams(params);
        snackbar.getView().setBackground(AppCompatResources.getDrawable(this, R.drawable.rounded_snackbar));

        snackbar.show();
    }

    private void updateNoFoundVisibility() {
        if (MainActivity.bookingDataList.size() > 0)
            noFound.setVisibility(View.INVISIBLE);
        else
            noFound.setVisibility(View.VISIBLE);
    }
}