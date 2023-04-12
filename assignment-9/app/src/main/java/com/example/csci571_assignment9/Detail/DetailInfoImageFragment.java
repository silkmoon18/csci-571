package com.example.csci571_assignment9.Detail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.csci571_assignment9.R;
import com.squareup.picasso.Picasso;

public class DetailInfoImageFragment extends Fragment {

    ImageView imageView;
    String imageUrl;

    public DetailInfoImageFragment(String url) {
        imageUrl = url;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_info_photo, container, false);

        imageView = v.findViewById(R.id.imageView_info_photo);
        Picasso.get().load(imageUrl).fit().centerInside().into(imageView);

        return v;
    }
}