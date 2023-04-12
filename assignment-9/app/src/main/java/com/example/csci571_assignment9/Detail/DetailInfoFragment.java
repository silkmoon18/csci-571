package com.example.csci571_assignment9.Detail;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.csci571_assignment9.R;
import com.example.csci571_assignment9.Reservation.BookingData;
import com.example.csci571_assignment9.Reservation.ReservationFormFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailInfoFragment extends Fragment {

    TextView address;
    TextView price;
    TextView phone;
    TextView status;
    TextView category;
    TextView url;
    Button reserve;

    ViewPager2 viewPager;
    InfoPhotoAdapter adapter;

    JSONObject data;

    public DetailInfoFragment(JSONObject data) {
        this.data = data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_info, container, false);

        address = v.findViewById(R.id.text_info_address_data);
        phone = v.findViewById(R.id.text_info_phone_data);
        category = v.findViewById(R.id.text_info_category_data);
        price = v.findViewById(R.id.text_info_price_data);
        status = v.findViewById(R.id.text_info_status_data);
        url = v.findViewById(R.id.text_info_url_data);
        reserve = v.findViewById(R.id.button_info_reserve);

        viewPager = v.findViewById(R.id.viewPager2_info_image);

        try {
            setAddress();
            setPhone();
            setCategory();
            setPrice();
            setStatus();
            setUrl();
            setImages();
            setReserveButton();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return v;
    }

    private void setAddress() throws JSONException {
        String text = data.getString("address");
        if (text.equals(""))
            text = "N/A";
        address.setText(text);
    }

    private void setPhone() throws JSONException {
        String text = data.getString("phone");
        if (text.equals(""))
            text = "N/A";
        phone.setText(text);
    }

    private void setCategory() throws JSONException {
        String text = data.getString("category");
        if (text.equals(""))
            text = "N/A";
        category.setText(text);
    }

    private void setPrice() throws JSONException {
        String text = data.getString("price");
        if (text.equals(""))
            text = "N/A";
        price.setText(text);
    }

    private void setStatus() throws JSONException {
        String text = data.getString("status");
        if (text.equals(""))
            text = "N/A";
        status.setText(text);
        if (text.equals("Open Now"))
            status.setTextColor(Color.GREEN);
        else if (text.equals("Closed"))
            status.setTextColor(Color.RED);
    }

    private void setUrl() throws JSONException {
        String text = data.getString("url");
        if (text.equals("")) {
            text = "N/A";
            url.setText(text);
        }
        else {
            String urlText = "<a href='" + text + "'>Business Link</a>";
            url.setText(Html.fromHtml(urlText, Build.VERSION.SDK_INT));
            url.setClickable(true);
            url.setMovementMethod(LinkMovementMethod.getInstance());
            url.setTextColor(Color.parseColor("#03dac5"));
        }
    }

    private void setImages() throws JSONException {
        adapter = new InfoPhotoAdapter(getParentFragmentManager(), getLifecycle());

        JSONArray photos = data.getJSONArray("photos");
        for (int i = 0; i < photos.length(); i++)
            adapter.addPhoto(photos.getString(i));
        viewPager.setAdapter(adapter);
    }

    private void setReserveButton() {
        reserve.setOnClickListener(view -> {
            DialogFragment dialog = null;
            try {
                dialog = new ReservationFormFragment(data.getString("name"));
            } catch (JSONException e) {
                Log.e(this.getClass().toString(), e.toString());
            }
            dialog.show(getParentFragmentManager(), "Reservation Form");
        });
    }
}