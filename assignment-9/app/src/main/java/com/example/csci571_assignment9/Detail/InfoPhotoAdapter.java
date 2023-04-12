package com.example.csci571_assignment9.Detail;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class InfoPhotoAdapter extends FragmentStateAdapter {

    final ArrayList<Fragment> fragmentList = new ArrayList<>();

    public InfoPhotoAdapter(FragmentManager fm, Lifecycle lc) {
        super(fm, lc);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    public void addPhoto(String url) {
        Fragment fragment = new DetailInfoImageFragment(url);
        fragmentList.add(fragment);
    }

}
