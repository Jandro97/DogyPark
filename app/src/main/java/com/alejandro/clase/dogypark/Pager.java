package com.alejandro.clase.dogypark;

import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Belal on 2/3/2016.
 */
//Extending FragmentStatePagerAdapter
public class Pager extends FragmentPagerAdapter {

    //integer to count number of tabs
    int tabCount;
    private final List <Fragment> mFragmentList =new ArrayList<>();

    //Constructor to the class
    public Pager(FragmentManager fm) {
        super(fm);
        //Initializing tab count


    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        /*switch (position) {
            case 0:
                Usuario tab1 = new Usuario();
                return tab1;
            case 1:
                MapsActivity tab2 = new MapsActivity();
                return tab2;
            default:
                return null;
        }*/
        return mFragmentList.get(position);
    }

    public void addFrag(Fragment fragment) {
        mFragmentList.add(fragment);

    }


    @Override
    public int getCount() {
        return mFragmentList.size();
    }


}

