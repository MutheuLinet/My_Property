package com.moringaschool.myproperty.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moringaschool.myproperty.databinding.ActivityDefectBinding;
import com.moringaschool.myproperty.adapters.DefectRecAdapter;
import com.moringaschool.myproperty.models.Defect;

import java.util.ArrayList;
import java.util.List;

public class DefectActivity extends AppCompatActivity {
    ActivityDefectBinding defBind;
    DefectRecAdapter adapter;
    DatabaseReference ref;
    List<Defect> allDefects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defBind = ActivityDefectBinding.inflate(getLayoutInflater());
        setContentView(defBind.getRoot());

        allDefects = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("defects");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot != null){
                    for (DataSnapshot singleDefect: snapshot.getChildren()){
                        Defect defect = singleDefect.getValue(Defect.class);
                        allDefects.add(defect);
                    }
                }

                adapter = new DefectRecAdapter(allDefects, DefectActivity.this);
//                defBind.myReCView.setAdapter(adapter);
//                defBind.myReCView.setLayoutManager(new LinearLayoutManager(DefectActivity.this));
//                defBind.myReCView.setHasFixedSize(true);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError t) {
                String error = t.getMessage();
                Toast.makeText(DefectActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}