package com.moringaschool.myproperty.ui;

import androidx.appcompat.app.AppCompatActivity;
import com.moringaschool.myproperty.R;
import com.moringaschool.myproperty.databinding.ActivityMainBinding;
import com.moringaschool.myproperty.databinding.ActivityManagerDashboardBinding;
import com.moringaschool.myproperty.models.Constants;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

public class ManagerDashboardActivity extends AppCompatActivity implements View.OnClickListener{
    ActivityManagerDashboardBinding mainBind;
    String  managerName;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBind = ActivityManagerDashboardBinding.inflate(getLayoutInflater());
        setContentView(mainBind.getRoot());

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        mainBind.subTitle.setText(pref.getString(Constants.NAME, "Property Overviews"));

        managerName = getIntent().getStringExtra("managerName");
        Toast.makeText(this, managerName, Toast.LENGTH_SHORT).show();

        mainBind.listOfPropertiesCard.setOnClickListener(this);
        mainBind.maintenanceRequestCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mainBind.listOfPropertiesCard){
            Intent intent =  new Intent(ManagerDashboardActivity.this, DefectActivity.class);
            intent.putExtra("managerName", managerName);
            startActivity(intent);
        }else if (v == mainBind.maintenanceRequestCard){
            startActivity(new Intent(ManagerDashboardActivity.this, DefectActivity.class));
        }

    }
}