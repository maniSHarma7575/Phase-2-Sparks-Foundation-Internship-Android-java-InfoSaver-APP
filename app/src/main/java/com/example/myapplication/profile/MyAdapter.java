package com.example.myapplication.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.example.myapplication.profile.fragments.EducationFragment;
import com.example.myapplication.profile.fragments.PersonalFragment;
import com.example.myapplication.profile.fragments.ProfessionalFragment;

public class MyAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;
    EducationFragment educationFragment = new EducationFragment();
    PersonalFragment personalFragment = new PersonalFragment();
    ProfessionalFragment professionalFragment = new ProfessionalFragment();

    Bundle bundle;

    public MyAdapter(Context context, FragmentManager fm, int totalTabs,Bundle bundle) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
        this.bundle=bundle;
        educationFragment.setArguments(bundle);
        professionalFragment.setArguments(bundle);
        personalFragment.setArguments(bundle);
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return personalFragment;
            case 1:
                return educationFragment;
            case 2:
                return professionalFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
