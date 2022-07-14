package com.moringaschool.myproperty.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.moringaschool.myproperty.R;
import com.moringaschool.myproperty.databinding.ActivitySelectBinding;


public class SelectActivity extends AppCompatActivity {

    ActivitySelectBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.managerRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectActivity.this, ManagerLoginActivity.class);
                startActivity(intent);
            }
        });
        binding.tenantLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectActivity.this, TenantLoginActivity.class);
                startActivity(intent);
            }
        });
    }
}