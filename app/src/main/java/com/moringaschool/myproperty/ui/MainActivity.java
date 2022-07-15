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
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moringaschool.myproperty.R;
import com.moringaschool.myproperty.adapters.TenantRecAdapter;
import com.moringaschool.myproperty.api.ApiCalls;
import com.moringaschool.myproperty.api.RetrofitClient;
import com.moringaschool.myproperty.databinding.ActivityDefectPostBinding;
import com.moringaschool.myproperty.databinding.ActivityMainBinding;
import com.moringaschool.myproperty.models.Constants;
import com.moringaschool.myproperty.models.Tenant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBind;
    Call<List<Tenant>> call1;
    List<Tenant> allTenants;
    ApiCalls calls;
    String managerName;
    SharedPreferences pref;
    TenantRecAdapter adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBind = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBind.getRoot());

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        managerName = pref.getString(Constants.NAME, "");
        mainBind.managerName.setText("Hey "+managerName);

        allTenants = new ArrayList<>();
        calls = RetrofitClient.getClient();

        call1 = calls.getManagerTenants(managerName);
        call1.clone().enqueue(new Callback<List<Tenant>>() {
            @Override
            public void onResponse(Call<List<Tenant>> call, Response<List<Tenant>> response) {
                if (response.isSuccessful()){
                    allTenants = response.body();
                    adp = new TenantRecAdapter(allTenants, MainActivity.this);
                    mainBind.recView.setAdapter(adp);
                    mainBind.recView.setHasFixedSize(true);
                    mainBind.recView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                    successful();
                }else{
                    unSuccessful();
                    Toast.makeText(MainActivity.this, "Something happened try again later", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tenant>> call, Throwable t) {
                unSuccessful();
                String error = t.getMessage();
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
        bottomClick(mainBind.bottom);
    }

    private void bottomClick(BottomNavigationView bottom) {
        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tenants:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(MainActivity.this, PropertiesActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.requests:
                        startActivity(new Intent(MainActivity.this, ManagersDoneDefects.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.back:
                        Intent intent = new Intent(MainActivity.this, SplashActivity.class);
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

    public void successful(){
        mainBind.cont1.setVisibility(View.VISIBLE);
        mainBind.title.setVisibility(View.VISIBLE);
        mainBind.recView.setVisibility(View.VISIBLE);
        mainBind.progress.setVisibility(View.GONE);
    }

    public void unSuccessful(){
        mainBind.cont1.setVisibility(View.GONE);
        mainBind.title.setVisibility(View.GONE);
        mainBind.recView.setVisibility(View.GONE);
        mainBind.progress.setVisibility(View.VISIBLE);
    }


}