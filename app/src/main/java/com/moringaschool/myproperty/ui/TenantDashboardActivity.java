package com.moringaschool.myproperty.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.moringaschool.myproperty.R;
import com.moringaschool.myproperty.databinding.ActivityTenantDashboardBinding;
import com.moringaschool.myproperty.databinding.ActivityTenantDefectBinding;

public class TenantDashboardActivity extends AppCompatActivity {

    ActivityTenantDashboardBinding mainBind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBind = ActivityTenantDashboardBinding.inflate(getLayoutInflater());
        setContentView(mainBind.getRoot());

        mainBind.maintenanceRequestCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TenantDashboardActivity.this, TenantDefectActivity.class);
                startActivity(intent);
            }
        });

        mainBind.historyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TenantDashboardActivity.this, TenantsDefectsActivity.class);
                startActivity(intent);
            }
        });
    }
}