package com.moringaschool.myproperty.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.moringaschool.myproperty.R;
import com.moringaschool.myproperty.adapters.DefectRecAdapter;
import com.moringaschool.myproperty.adapters.TenantDefectRecAdapter;
import com.moringaschool.myproperty.api.ApiCalls;
import com.moringaschool.myproperty.api.RetrofitClient;
import com.moringaschool.myproperty.databinding.ActivityTenantsDefectsBinding;
import com.moringaschool.myproperty.models.Constants;
import com.moringaschool.myproperty.models.Defect;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TenantsDefectsActivity extends AppCompatActivity {
    ActivityTenantsDefectsBinding tenBind;
    SharedPreferences pref;
    String tenantName, tenantId;
    Call<List<Defect>> call;
    ApiCalls calls;
    List<Defect> allDefects;
    TenantDefectRecAdapter adp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tenBind = ActivityTenantsDefectsBinding.inflate(getLayoutInflater());
        setContentView(tenBind.getRoot());

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        tenantName = pref.getString(Constants.TENANT_NAME, "");
        tenantId = pref.getString(Constants.TENANT_ID, "");

        allDefects = new ArrayList<>();

        tenBind.managerName.setText("Hey, "+ tenantName);

        calls = RetrofitClient.getClient();
        call = calls.tenantDefects(tenantId);

        call.clone().enqueue(new Callback<List<Defect>>() {
            @Override
            public void onResponse(Call<List<Defect>> call, Response<List<Defect>> response) {
                if (response.isSuccessful()){
                    allDefects = response.body();
                    adp = new TenantDefectRecAdapter(allDefects, TenantsDefectsActivity.this);
                    tenBind.myRec.setAdapter(adp);
                    tenBind.myRec.setHasFixedSize(true);
                    tenBind.myRec.setLayoutManager(new LinearLayoutManager(TenantsDefectsActivity.this));
                }
            }

            @Override
            public void onFailure(Call<List<Defect>> call, Throwable t) {
                String error = t.getMessage();
                Toast.makeText(TenantsDefectsActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        navigation(tenBind.bottom);
    }

    private void navigation(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(TenantsDefectsActivity.this, TenantMainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.move:
                        startActivity(new Intent(TenantsDefectsActivity.this, TenantsDoneDefects.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.favourites:
                        return true;
                    case R.id.back:
                        Intent intent = new Intent(TenantsDefectsActivity.this, SplashActivity.class);
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