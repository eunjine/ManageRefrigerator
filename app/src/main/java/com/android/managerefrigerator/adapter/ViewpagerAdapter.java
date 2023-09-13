package com.android.managerefrigerator.adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewpagerAdapter extends FragmentStateAdapter {
    private ArrayList<Fragment> mFragments;

    public ViewpagerAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Fragment> list) {
        super(fragmentActivity);
        this.mFragments = list;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragments.size();
    }
}