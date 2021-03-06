package com.homework.getfood.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.homework.getfood.ui.fragment.MakeFragment;
import com.homework.getfood.ui.fragment.OrderFragment;
import com.homework.getfood.R;
import com.homework.getfood.util.StringFetcher;

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles = new String[]{StringFetcher.getString(R.string.title_home), StringFetcher.getString(R.string.title_orders)};

    public MainFragmentPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 1) return new OrderFragment();
        else return new MakeFragment();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }


}
