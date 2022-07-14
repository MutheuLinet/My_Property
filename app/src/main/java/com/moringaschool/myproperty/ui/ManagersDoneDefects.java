package com.moringaschool.myproperty.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moringaschool.myproperty.R;
import com.moringaschool.myproperty.adapters.ManagerDoneDefectsAdapter;
import com.moringaschool.myproperty.adapters.TenantDoneDefectsAdapter;
import com.moringaschool.myproperty.api.ApiCalls;
import com.moringaschool.myproperty.api.RetrofitClient;
import com.moringaschool.myproperty.databinding.ActivityManagersDoneDefectsBinding;
import com.moringaschool.myproperty.models.Constants;
import com.moringaschool.myproperty.models.DoneDefect;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ManagersDoneDefects extends AppCompatActivity {
    ActivityManagersDoneDefectsBinding manBind;
    SharedPreferences pref;
    Call<List<DoneDefect>> call;
    ApiCalls calls;
    List<DoneDefect> allDoneDefects;
    ManagerDoneDefectsAdapter adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manBind = ActivityManagersDoneDefectsBinding.inflate(getLayoutInflater());
        setContentView(manBind.getRoot());

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        manBind.managerName.setText("Hey, "+ pref.getString(Constants.NAME, "Charles"));

        calls = RetrofitClient.getClient();
        call = calls.managerDoneDefects(pref.getString(Constants.NAME, ""));
        allDoneDefects = new ArrayList<>();

        call.clone().enqueue(new Callback<List<DoneDefect>>() {
            @Override
            public void onResponse(Call<List<DoneDefect>> call, Response<List<DoneDefect>> response) {
                if (response.isSuccessful()){
                    allDoneDefects = response.body();
                    adp = new ManagerDoneDefectsAdapter(allDoneDefects,ManagersDoneDefects.this);
                    manBind.myRec.setAdapter(adp);
                    manBind.myRec.setLayoutManager(new LinearLayoutManager(ManagersDoneDefects.this));
                    manBind.myRec.setHasFixedSize(true);
                }else{
                    manBind.title.setText("There are no done defects Currently");

                }

            }

            @Override
            public void onFailure(Call<List<DoneDefect>> call, Throwable t) {

            }
        });

        bottomClick(manBind.bottom);
    }

    private void bottomClick( BottomNavigationView bottom) {
        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(ManagersDoneDefects.this, PropertiesActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.tenants:
                        startActivity(new Intent(ManagersDoneDefects.this, MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.requests:
                        return true;
                    case R.id.back:
                        Intent intent = new Intent(ManagersDoneDefects.this, SplashActivity.class);
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

}