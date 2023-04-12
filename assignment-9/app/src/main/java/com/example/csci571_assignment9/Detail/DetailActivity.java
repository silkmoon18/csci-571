package com.example.csci571_assignment9.Detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.csci571_assignment9.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    String urlRoot;

    String facebookUrl;
    String twitterUrl;

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager2 viewPager;

    DetailTabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        requestQueue = Volley.newRequestQueue(this);
        urlRoot = getResources().getString(R.string.url_root);

        facebookUrl = "https://www.facebook.com/sharer/sharer.php?u=";
        twitterUrl = "https://twitter.com/intent/tweet?text=";

        toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        tabLayout = findViewById(R.id.tablayout_detail);
        viewPager = findViewById(R.id.viewpager_detail);

        viewPager.setUserInputEnabled(false);

        getDetails();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_facebook:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl)));
                return true;
            case R.id.action_twitter:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(twitterUrl)));
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getDetails() {
        Bundle b = getIntent().getExtras();

        String url = urlRoot + "/get_business?id=" + b.getString("id");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject res = new JSONObject(response);
                        makeDetailPages(res);
                    } catch (JSONException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("autocomplete", error.toString()));

        requestQueue.add(stringRequest);
    }

    private void makeDetailPages(JSONObject data) throws JSONException, UnsupportedEncodingException {
        Objects.requireNonNull(getSupportActionBar()).setTitle(data.getString("name"));

        facebookUrl += data.getString("url");

        String twitterText = "Check Out " + data.getString("name") + " on Yelp." + data.getString("url");
        twitterText = URLEncoder.encode(twitterText, "UTF-8");
        twitterUrl += twitterText;

        adapter = new DetailTabAdapter(getSupportFragmentManager(), getLifecycle());
        adapter.addFragment(new DetailInfoFragment(data), "BUSINESS\nDETAILS");
        adapter.addFragment(new DetailMapFragment(data), "MAP LOCATION");
        adapter.addFragment(new DetailReviewFragment(data), "REVIEWS");

        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(adapter.getTitle(position))
        ).attach();
    }

}