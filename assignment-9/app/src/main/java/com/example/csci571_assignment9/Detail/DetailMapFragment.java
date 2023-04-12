package com.example.csci571_assignment9.Detail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.csci571_assignment9.R;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailMapFragment extends Fragment {

    JSONObject data;

    public DetailMapFragment(JSONObject data) {
        this.data = data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            JSONObject coord = data.getJSONObject("coord");
            double lat = Double.parseDouble(coord.getString("latitude"));
            double lng = Double.parseDouble(coord.getString("longitude"));

            LatLng latLng = new LatLng(lat, lng);
            Fragment mapFragment = new MapFragment(latLng, data.getString("name"));

            getParentFragmentManager()
                    .beginTransaction().replace(R.id.map_container,mapFragment)
                    .commit();

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(this.getClass().toString(), e.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_map, container, false);
    }

}