package com.example.marwatalaat.smartparking.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.marwatalaat.smartparking.HistoryFragment;
import com.example.marwatalaat.smartparking.ParkingFragment;
import com.example.marwatalaat.smartparking.ProgressFragment;
import com.example.marwatalaat.smartparking.R;
import com.example.marwatalaat.smartparking.model.User;

/**
 * Created by MarwaTalaat on 11/7/2016.
 */

public class TabPagerAdapter extends FragmentPagerAdapter {
    private  TypedArray tabIcons;
    User user;

    public TabPagerAdapter(FragmentManager fm , User user , Context context) {
        super(fm);
        this.user = user;
         tabIcons  = context.getResources().obtainTypedArray(R.array.icons);


    }

    @Override
    public Fragment getItem(int position) {
        if(position==0) return ParkingFragment.getInstance(user);
        else  return  ProgressFragment.getInstance(user);
       // else return new HistoryFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    public void setupTabIcons(TabLayout tabs) {
        for(int i = 0; i<getCount() ; i++){
            tabs.getTabAt(i).setIcon(tabIcons.getDrawable(i));

        }

    }
}
