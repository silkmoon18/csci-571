package com.example.csci571_assignment9.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.csci571_assignment9.R;
import com.example.csci571_assignment9.Reservation.BookingData;
import com.example.csci571_assignment9.Reservation.BookingsAdapter;
import com.example.csci571_assignment9.Reservation.ReservationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    FragmentManager fm;

    SearchResultFragment searchResult;
    Toolbar toolbar;

    SharedPreferences sharedPreferences;
    static SharedPreferences.Editor editor;
    public static ArrayList<BookingData> bookingDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();

        searchResult = (SearchResultFragment)fm.findFragmentById(R.id.fragment_searchResult);
        toolbar = findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences("MyBookings", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        try {
            getSavedPreferences();
        } catch (JSONException e) {
            Log.e(this.getClass().toString(), e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_reservation) {
            startActivity(new Intent(this, ReservationActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void displayResult(JSONArray result) throws JSONException {
        searchResult.updateTable(result);
    }

    public void clearResult() {
        searchResult.clearTable();
    }

    private void getSavedPreferences() throws JSONException {
        bookingDataList = new ArrayList<>();

        Map<String,?> keys = sharedPreferences.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            if (entry.getKey().contains("BookingData_")) {
                String saved = (String) entry.getValue();
                JSONObject data = new JSONObject(saved);
                BookingData bookingData = new BookingData(data);
                bookingDataList.add(bookingData);
            }
        }
    }
    public static void createReservation(String name, String date, String time, String email) {
        BookingData data = new BookingData(name, date, time, email);

        boolean found = false;
        for (int i = 0; i < bookingDataList.size(); i++) {
            BookingData item = bookingDataList.get(i);
            if (item.getname().equals(name)) {
                data = new BookingData(name, date, time, email);
                bookingDataList.set(i, data);
                found = true;
            }
        }
        if (!found)
            bookingDataList.add(data);

        JSONObject jsonData = data.getJsonObject();
        editor.putString("BookingData_" + name, jsonData.toString());
        editor.commit();
    }

    public static void deleteReservation(int position) {
        BookingData data = bookingDataList.get(position);

        editor.remove("BookingData_" + data.getname());
        editor.commit();

        bookingDataList.remove(position);
    }
}