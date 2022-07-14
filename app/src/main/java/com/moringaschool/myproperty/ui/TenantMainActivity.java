package com.moringaschool.myproperty.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moringaschool.myproperty.R;
import com.moringaschool.myproperty.adapters.TenantUnitRecAdapter;
import com.moringaschool.myproperty.api.ApiCalls;
import com.moringaschool.myproperty.api.RetrofitClient;
import com.moringaschool.myproperty.databinding.ActivityTenantMainBinding;
import com.moringaschool.myproperty.models.Constants;

import com.moringaschool.myproperty.models.Unit;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TenantMainActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityTenantMainBinding mainTenantBind;
    SharedPreferences pref;
    DatabaseReference ref;

    String tenantName;
    List<Unit> allUnits;
    TenantUnitRecAdapter adp;
    Call<List<Unit>> call;
    ApiCalls calls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainTenantBind = ActivityTenantMainBinding.inflate(getLayoutInflater());
        setContentView(mainTenantBind.getRoot());

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        ref = FirebaseDatabase.getInstance().getReference();
        calls = RetrofitClient.getClient();

        allUnits = new ArrayList<>();
        tenantName = pref.getString(Constants.TENANT_NAME, "");
        mainTenantBind.managerName.setText("Hey, "+ tenantName);
        mainTenantBind.questType.setText("Building Name: "+ pref.getString(Constants.PROPERTY_NAME, ""));
        mainTenantBind.questAns.setText("House name: "+ pref.getString(Constants.UNIT_NAME, ""));
        mainTenantBind.questMode.setText("Manager in charge: "+ pref.getString(Constants.DEFECT_MANAGER_NAME, ""));
        mainTenantBind.location.setVisibility(View.GONE);
        mainTenantBind.add.setVisibility(View.GONE);

        mainTenantBind.add.setOnClickListener(this);
        call = calls.propertyUnits(pref.getString(Constants.PROPERTY_NAME, ""));

        call.enqueue(new Callback<List<Unit>>() {
            @Override
            public void onResponse(Call<List<Unit>> call, Response<List<Unit>> response) {
                if (response.isSuccessful()){
                    allUnits = response.body();
                    adp = new TenantUnitRecAdapter(allUnits, TenantMainActivity.this);
                    mainTenantBind.recView.setAdapter(adp);
                    mainTenantBind.recView.setHasFixedSize(true);
                    mainTenantBind.recView.setLayoutManager(new LinearLayoutManager(TenantMainActivity.this));

                }

            }

            @Override
            public void onFailure(Call<List<Unit>> call, Throwable t) {

            }
        });
//        call = calls.tenantDoneDefects(pref.getString(Constants.TENANT_ID, ""));
//
//        call.clone().enqueue(new Callback<List<DoneDefect>>() {
//            @Override
//            public void onResponse(Call<List<DoneDefect>> call, Response<List<DoneDefect>> response) {
//                if (response.isSuccessful()){
//                    adp = new DefectRecAdapter(allDefects, TenantMainActivity.this);
//                    mainTenantBind.recView.setAdapter(adp);
//                    mainTenantBind.recView.setHasFixedSize(true);
//                    mainTenantBind.recView.setLayoutManager(new LinearLayoutManager(TenantMainActivity.this));
//                    adp.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<DoneDefect>> call, Throwable t) {
//                String error = t.getMessage();
//                Toast.makeText(TenantMainActivity.this, error, Toast.LENGTH_SHORT).show();
//
//            }
//        });

//        ref.child("DoneDefects").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    for (DataSnapshot defect: snapshot.getChildren()){
//                        Defect oneDefect = defect.getValue(Defect.class);
//                        allDefects.add(oneDefect);
//                    }
//
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError t) {
//
//            }
//        });
        navigation(mainTenantBind.bottom);
    }

    private void navigation(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.move:
                        startActivity(new Intent(TenantMainActivity.this, TenantsDoneDefects.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.favourites:
                        startActivity(new Intent(TenantMainActivity.this, TenantsDefectsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.back:
                        Intent intent = new Intent(TenantMainActivity.this, SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mainTenantBind.add){
            startActivity(new Intent(TenantMainActivity.this,TenantDefectActivity.class));
        }
    }
}