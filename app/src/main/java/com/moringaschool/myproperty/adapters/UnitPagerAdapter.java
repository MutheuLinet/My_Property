package com.moringaschool.myproperty.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.moringaschool.myproperty.fragments.UnitFragment;
import com.moringaschool.myproperty.models.Unit;

import java.util.List;

public class UnitPagerAdapter extends FragmentPagerAdapter {
    List<Unit> allUnits;
    public UnitPagerAdapter(@NonNull FragmentManager fm, int behavior, List<Unit> allUnits) {
        super(fm, behavior);
        this.allUnits = allUnits;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        UnitFragment fragment = UnitFragment.newInstance(allUnits.get(position));
        return fragment ;
    }

    @Override
    public int getCount() {
        return allUnits.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return allUnits.get(position).getUnitName();
    }
}
