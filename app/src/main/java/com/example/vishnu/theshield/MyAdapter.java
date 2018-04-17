package com.example.vishnu.theshield;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Vishnu on 4/15/2018.
 */

public class MyAdapter extends FragmentPagerAdapter {

    MyAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * Return fragment with respect to Position .
     */

    @Override
    public Fragment getItem(int position)
    {
        switch (position){
            case 0 : return new Checker();
            case 1 : return new OptionsFragment();
            case 2 : return new PartnerFragment();
        }
        return null;
    }

    @Override
    public int getCount() {

        return 3;

    }

    /**
     * This method returns the title of the tab according to the position.
     */

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0 :
                return "Checker";
            case 1 :
                return "Options";
            case 2 :
                return "BUdDy";
        }
        return null;
    }
}