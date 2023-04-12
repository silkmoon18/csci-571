package com.example.csci571_assignment9.Reservation;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class BookingData {
    private JSONObject data;

    public BookingData(JSONObject data) {
        this.data = data;
    }

    public BookingData(String name, String date, String time, String email) {
        data = new JSONObject();

        try {
            data.put("name", name);
            data.put("date", date);
            data.put("time", time);
            data.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(this.getClass().toString(), e.toString());
        }
    }

    public JSONObject getJsonObject() {
        return data;
    }

    public String getname() {
        try {
            return data.getString("name");
        } catch (JSONException e) {
            return "";
        }
    }
    public String getdate() {
        try {
            return data.getString("date");
        } catch (JSONException e) {
            return "";
        }
    }
    public String gettime() {
        try {
            return data.getString("time");
        } catch (JSONException e) {
            return "";
        }
    }
    public String getemail() {
        try {
            return data.getString("email");
        } catch (JSONException e) {
            return "";
        }
    }
}
