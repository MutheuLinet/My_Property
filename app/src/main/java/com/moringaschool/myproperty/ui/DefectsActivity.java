package com.moringaschool.myproperty.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moringaschool.myproperty.R;
import com.moringaschool.myproperty.adapters.DefectsPagerAdapter;
import com.moringaschool.myproperty.databinding.ActivityDefectsBinding;
import com.moringaschool.myproperty.models.Defect;
import com.moringaschool.myproperty.models.Property;

import java.util.ArrayList;
import java.util.List;

public class DefectsActivity extends AppCompatActivity {
    ActivityDefectsBinding defBind;
    List<Defect> allDefects;
    DefectsPagerAdapter adapter;
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defBind = ActivityDefectsBinding.inflate(getLayoutInflater());
        setContentView(defBind.getRoot());

        Intent intent = getIntent();
        allDefects  = (List<Defect>) intent.getSerializableExtra("allDefects");
        int position = intent.getIntExtra("position", 0);
        adapter = new DefectsPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, allDefects);
        defBind.viewPager.setAdapter(adapter);
        defBind.viewPager.setCurrentItem(position);


    }
}