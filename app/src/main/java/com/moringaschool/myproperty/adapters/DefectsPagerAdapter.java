package com.moringaschool.myproperty.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.moringaschool.myproperty.fragments.DefectFragment;
import com.moringaschool.myproperty.models.Defect;

import java.util.List;

public class DefectsPagerAdapter extends FragmentPagerAdapter {
    List<Defect> allDefects;

    public DefectsPagerAdapter(@NonNull FragmentManager fm, int behavior, List<Defect> allDefects ) {
        super(fm, behavior);
        this.allDefects = allDefects;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        DefectFragment fragment = DefectFragment.newInstance(allDefects.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return allDefects.size();
    }
}
