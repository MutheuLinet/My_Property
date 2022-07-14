package com.moringaschool.myproperty.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;

import android.content.Intent;
import android.os.Bundle;

import com.moringaschool.myproperty.adapters.UnitPagerAdapter;
import com.moringaschool.myproperty.databinding.ActivityUnitDetailsBinding;
import com.moringaschool.myproperty.models.Unit;

import java.util.ArrayList;
import java.util.List;

public class UnitDetailsActivity extends AppCompatActivity {
    ActivityUnitDetailsBinding unitBind;
    List<Unit> allUnits;
    UnitPagerAdapter adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unitBind = ActivityUnitDetailsBinding.inflate(getLayoutInflater());
        setContentView(unitBind.getRoot());

        allUnits = new ArrayList<>();
        Intent newIntent = getIntent();
        allUnits = (List<Unit>) newIntent.getSerializableExtra("allUnits");
        int position = newIntent.getIntExtra("position", 0);
        adp = new UnitPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, allUnits);
        unitBind.viewPager.setAdapter(adp);
        unitBind.viewPager.setCurrentItem(position);



    }
}