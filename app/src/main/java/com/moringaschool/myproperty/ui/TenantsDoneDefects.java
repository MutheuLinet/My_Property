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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.FirebaseDatabase;
import com.moringaschool.myproperty.R;
import com.moringaschool.myproperty.adapters.TenantDoneDefectsAdapter;
import com.moringaschool.myproperty.api.ApiCalls;
import com.moringaschool.myproperty.api.RetrofitClient;
import com.moringaschool.myproperty.databinding.ActivityTenantsDoneDefectsBinding;
import com.moringaschool.myproperty.models.Constants;
import com.moringaschool.myproperty.models.DoneDefect;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TenantsDoneDefects extends AppCompatActivity implements View.OnClickListener {
    ActivityTenantsDoneDefectsBinding tenBind;
    SharedPreferences pref;
    Call<List<DoneDefect>> call;
    ApiCalls calls;
    List<DoneDefect> allDoneDefects;
    TenantDoneDefectsAdapter adp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tenBind = ActivityTenantsDoneDefectsBinding.inflate(getLayoutInflater());
        setContentView(tenBind.getRoot());

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        tenBind.managerName.setText("Hey, "+ pref.getString(Constants.TENANT_NAME, "Charles"));

        tenBind.add.setOnClickListener(this);

        calls = RetrofitClient.getClient();
        call = calls.tenantDoneDefects(pref.getString(Constants.TENANT_ID,""));
        allDoneDefects = new ArrayList<>();

        call.clone().enqueue(new Callback<List<DoneDefect>>() {
            @Override
            public void onResponse(Call<List<DoneDefect>> call, Response<List<DoneDefect>> response) {
                if (response.isSuccessful()){
                    allDoneDefects = response.body();
                    adp = new TenantDoneDefectsAdapter(allDoneDefects,TenantsDoneDefects.this);
                    tenBind.myRec.setAdapter(adp);
                    tenBind.myRec.setLayoutManager(new LinearLayoutManager(TenantsDoneDefects.this));
                    tenBind.myRec.setHasFixedSize(true);
                    successful();
                }else{
                    unSuccessful();
                    tenBind.title.setText("There are no done defects Currently");
                }

            }

            @Override
            public void onFailure(Call<List<DoneDefect>> call, Throwable t) {
                unSuccessful();
                String error = t.getMessage();
                Toast.makeText(TenantsDoneDefects.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        bottomClick(tenBind.bottom);
    }

    private void bottomClick( BottomNavigationView bottom) {
        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(TenantsDoneDefects.this, TenantMainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.move:
                        return true;
                    case R.id.favourites:
                        startActivity(new Intent(TenantsDoneDefects.this, TenantsDefectsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.back:
                        Intent intent = new Intent(TenantsDoneDefects.this, SplashActivity.class);
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
        if (v == tenBind.add){
            startActivity(new Intent(this, TenantDefectActivity.class));
        }

    }

    public void successful(){
        tenBind.progress.setVisibility(View.GONE);
        tenBind.animationView.setVisibility(View.VISIBLE);
        tenBind.profileImage.setVisibility(View.VISIBLE);
        tenBind.logo.setVisibility(View.VISIBLE);
        tenBind.cont1.setVisibility(View.VISIBLE);
        tenBind.myRec.setVisibility(View.VISIBLE);
        tenBind.add.setVisibility(View.VISIBLE);
        tenBind.title.setVisibility(View.VISIBLE);
    }

    public void unSuccessful(){
        tenBind.progress.setVisibility(View.VISIBLE);
        tenBind.animationView.setVisibility(View.GONE);
        tenBind.profileImage.setVisibility(View.GONE);
        tenBind.logo.setVisibility(View.GONE);
        tenBind.cont1.setVisibility(View.GONE);
        tenBind.myRec.setVisibility(View.GONE);
        tenBind.add.setVisibility(View.GONE);
        tenBind.title.setVisibility(View.GONE);
    }
}