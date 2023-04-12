package com.example.csci571_assignment9.Search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.csci571_assignment9.Detail.DetailActivity;
import com.example.csci571_assignment9.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchResultFragment extends Fragment {

    ScrollView tableContainer;
    TableLayout table;
    TextView noFound;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_result, container, false);

        tableContainer = v.findViewById(R.id.scroll_result_container);
        table = v.findViewById(R.id.table_result_table);
        noFound = v.findViewById(R.id.text_result_noFound);

        return v;
    }

    public void updateTable(JSONArray result) throws JSONException {
        if (result.length() > 0) {
            tableContainer.setVisibility(View.VISIBLE);
            noFound.setVisibility(View.INVISIBLE);

            table.removeAllViews();
            for (int i = 0; i < result.length(); i++) {
                JSONObject business = result.getJSONObject(i);

                TableRow tr = createTableRow();
                tr.addView(createBusinessIndex(i + 1));
                tr.addView(createBusinessImage(business.getString("image")));
                tr.addView(createBusinessName(business.getString("name")));
                tr.addView(createBusinessRating(business.getString("rating")));
                tr.addView(createBusinessDistance(business.getString("distance")));

                tr.setOnClickListener(v -> {
                    Bundle b = new Bundle();
                    try {
                        b.putString("id", business.getString("id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(requireActivity(), DetailActivity.class).putExtras(b));
                });

                table.addView(tr, i);
            }
        }
        else {
            tableContainer.setVisibility(View.INVISIBLE);
            noFound.setVisibility(View.VISIBLE);
        }
    }

    public void clearTable() {
        tableContainer.setVisibility(View.INVISIBLE);
        table.removeAllViews();
    }

    private TableRow createTableRow() {
        TableRow tr = new TableRow(requireActivity());
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 10);
        tr.setLayoutParams(params);
        tr.setBackgroundColor(Color.parseColor("#f2f2f2"));

        return tr;
    }

    @SuppressLint("SetTextI18n")
    private TextView createBusinessIndex(int i) {
        TextView textView = new TextView(requireActivity());
        textView.setLayoutParams(new TableRow.LayoutParams(230, 200));
        textView.setTextSize(20);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        textView.setText(Integer.toString(i));

        return textView;
    }

    private ImageView createBusinessImage(String url) {
        ImageView imageView = new ImageView(requireActivity());
        TableRow.LayoutParams params = new TableRow.LayoutParams(160, 140);
        params.gravity = Gravity.CENTER_VERTICAL;
        imageView.setLayoutParams(params);
        Picasso.get().load(url).fit().centerInside().into(imageView);

        return imageView;
    }

    private TextView createBusinessName(String businessName) {
        TextView textView = new TextView(requireActivity());
        TableRow.LayoutParams params = new TableRow.LayoutParams(230, 200);
        params.gravity = Gravity.CENTER;
        params.setMargins(20, 10, 10, 10);
        textView.setLayoutParams(params);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        textView.setText(businessName);

        return textView;
    }

    private TextView createBusinessRating(String rating) {
        TextView textView = new TextView(requireActivity());
        TableRow.LayoutParams params = new TableRow.LayoutParams(150, 200);
        params.gravity = Gravity.CENTER;
        textView.setLayoutParams(params);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        textView.setText(rating);

        return textView;
    }

    private TextView createBusinessDistance(String distance) {
        TextView textView = new TextView(requireActivity());
        TableRow.LayoutParams params = new TableRow.LayoutParams(150, 200);
        params.gravity = Gravity.CENTER;
        textView.setLayoutParams(params);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);

        distance = String.valueOf(Math.round(Float.parseFloat(distance)));
        textView.setText(distance);

        return textView;
    }
}