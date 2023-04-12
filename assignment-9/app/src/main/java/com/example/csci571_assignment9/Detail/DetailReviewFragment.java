package com.example.csci571_assignment9.Detail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.csci571_assignment9.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailReviewFragment extends Fragment {

    RequestQueue requestQueue;
    String urlRoot;

    JSONObject data;

    ArrayList<TextView> reviewNameList = new ArrayList<>();
    ArrayList<TextView> reviewDataList = new ArrayList<>();

    public DetailReviewFragment(JSONObject data) {
        this.data = data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = Volley.newRequestQueue(requireActivity());
        urlRoot = getResources().getString(R.string.url_root);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_review, container, false);

        reviewNameList.add(v.findViewById(R.id.text_review_name_1));
        reviewDataList.add(v.findViewById(R.id.text_review_data_1));

        reviewNameList.add(v.findViewById(R.id.text_review_name_2));
        reviewDataList.add(v.findViewById(R.id.text_review_data_2));

        reviewNameList.add(v.findViewById(R.id.text_review_name_3));
        reviewDataList.add(v.findViewById(R.id.text_review_data_3));

        try {
            getReviews();
        } catch (JSONException e) {
            Log.e(this.getClass().toString(), e.toString());
        }

        return v;
    }

    private void getReviews() throws JSONException {

        String url = urlRoot + "/get_reviews?id=" + data.getString("id");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray res = new JSONArray(response);
                        for (int i = 0; i < res.length(); i++) {
                            JSONObject review = res.getJSONObject(i);
                            reviewNameList.get(i).setText(review.getString("name"));

                            String reviewData = "Rating :" + review.getString("rating") + "/5\n\n" +
                                    review.getString("text") + "\n\n" +
                                    review.getString("time_created").split(" ")[0];
                            reviewDataList.get(i).setText(reviewData);
                        }
                    } catch (JSONException e) {
                        Log.e(this.getClass().toString(), e.toString());
                    }
                },
                e -> Log.e(this.getClass().toString(), e.toString()));

        requestQueue.add(stringRequest);
    }
}