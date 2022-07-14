package com.moringaschool.myproperty.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moringaschool.myproperty.R;
import com.moringaschool.myproperty.adapters.DefectRecAdapter;
import com.moringaschool.myproperty.databinding.ActivityPropertiesBinding;
import com.moringaschool.myproperty.adapters.PropertyRecAdapter;
import com.moringaschool.myproperty.api.ApiCalls;
import com.moringaschool.myproperty.api.RetrofitClient;
import com.moringaschool.myproperty.fragments.PropertyDialogFragment;
import com.moringaschool.myproperty.fragments.TenantDialogFragment;
import com.moringaschool.myproperty.models.Constants;
import com.moringaschool.myproperty.models.Defect;
import com.moringaschool.myproperty.models.Property;
import com.moringaschool.myproperty.models.PropertyManager;
import com.moringaschool.myproperty.models.Validator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertiesActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityPropertiesBinding properBind;
    List<Property> allProperties;
    PropertyRecAdapter adapter;
    Call<List<Property>> call1;
    Call<Property> call2;
    Call<List<Defect>> call3;
    SharedPreferences pref;
    DefectRecAdapter adp;
    DatabaseReference ref;
    List<Defect> allDefects;
    PropertyManager manager;
    BottomSheetDialog dialog;
    ApiCalls calls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        properBind = ActivityPropertiesBinding.inflate(getLayoutInflater());
        setContentView(properBind.getRoot());


        ref = FirebaseDatabase.getInstance().getReference();
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        dialog = new BottomSheetDialog(this);
        createDialog();

        properBind.managerName.setText("Hey, "+ pref.getString(Constants.NAME, "Charles"));



        properBind.add.setOnClickListener(this);
        properBind.add2.setOnClickListener(this);
        bottomClick(properBind.bottom);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

//        manager = (PropertyManager) getIntent().getSerializableExtra("manager");


        allProperties = new ArrayList<>();
        allDefects = new ArrayList<>();
        calls = RetrofitClient.getClient();
        String name1 = pref.getString(Constants.NAME, "");

        call1 = calls.getManagerProperties(pref.getString(Constants.NAME, "Charles"));
        call3 = calls.managerDefects(pref.getString(Constants.NAME, "Charles"));



    }

    private void bottomClick( BottomNavigationView bottom) {
        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.tenants:
                        startActivity(new Intent(PropertiesActivity.this, MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.requests:
                        startActivity(new Intent(PropertiesActivity.this, ManagersDoneDefects.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.back:
                        Intent intent = new Intent(PropertiesActivity.this, SplashActivity.class);
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

    private void createDialog() {
        View v = getLayoutInflater().inflate(R.layout.add_property_dialog, null, false);
        TextInputLayout name = v.findViewById(R.id.name);
        TextInputLayout location = v.findViewById(R.id.location);
        MaterialButton submit = v.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String propertyName = name.getEditText().getText().toString().trim();
                String propertyLocation = location.getEditText().getText().toString().trim();

                if (!Validator.validateName(name) || !Validator.validateName(location)){
                    return;
                }

                Property property = new Property(propertyName, pref.getString(Constants.NAME, ""), propertyLocation);
                call2 = calls.addProperty(property);

                call2.clone().enqueue(new Callback<Property>() {
                    @Override
                    public void onResponse(Call<Property> call, Response<Property> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(PropertiesActivity.this, "Property successfully added", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Property> call, Throwable t) {
                        String error = t.getMessage();
                        Toast.makeText(PropertiesActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        dialog.setContentView(v);

    }

    @Override
    public void onClick(View v) {
        if (v == properBind.add){
//            FragmentManager fm = getSupportFragmentManager();
//            PropertyDialogFragment fragment = new PropertyDialogFragment();
//
//            fragment.show(fm, "Property Dialog");

            dialog.show();

        }else
        if (v == properBind.add2){
            Intent intent = new Intent(PropertiesActivity.this, TenantDefectActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        call1.clone().enqueue(new Callback<List<Property>>() {
            @Override
            public void onResponse(Call<List<Property>> call, Response<List<Property>> response) {
                if (response.isSuccessful()){
                    allProperties = response.body();
                    adapter = new PropertyRecAdapter(allProperties, PropertiesActivity.this);
                    properBind.myRec.setAdapter(adapter);
                    properBind.myRec.setLayoutManager(new LinearLayoutManager(PropertiesActivity.this, RecyclerView.HORIZONTAL, false));
                    properBind.myRec.setHasFixedSize(true);

                    successfull();
                }else{
                    unSuccessfull();
                }
            }
            @Override
            public void onFailure(Call<List<Property>> call, Throwable t) {
                unSuccessfull();

                String error = t.getMessage();
                Toast.makeText(PropertiesActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        call3.clone().enqueue(new Callback<List<Defect>>() {
            @Override
            public void onResponse(Call<List<Defect>> call, Response<List<Defect>> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        allDefects = response.body();
                        adp = new DefectRecAdapter(allDefects, PropertiesActivity.this);
                        properBind.recView.setAdapter(adp);
                        properBind.recView.setHasFixedSize(true);
                        properBind.recView.setLayoutManager(new LinearLayoutManager(PropertiesActivity.this));
                    }else{
                        properBind.title2.setText("No requests available");
                    }

                }else{
                    Toast.makeText(PropertiesActivity.this,"Something happened", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<List<Defect>> call, Throwable t) {
                String error = t.getMessage();
                Toast.makeText(PropertiesActivity.this, error, Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void successfull(){
        properBind.cont1.setVisibility(View.VISIBLE);
        properBind.title.setVisibility(View.VISIBLE);
        properBind.add.setVisibility(View.VISIBLE);
        properBind.myRec.setVisibility(View.VISIBLE);
        properBind.title2.setVisibility(View.VISIBLE);
        properBind.add2.setVisibility(View.VISIBLE);
        properBind.recView.setVisibility(View.VISIBLE);
        properBind.progress.setVisibility(View.GONE);
    }

    public void unSuccessfull(){
        properBind.cont1.setVisibility(View.GONE);
        properBind.title.setVisibility(View.VISIBLE);
        properBind.title.setText("Please wait for some few minutes.");
        properBind.add.setVisibility(View.GONE);
        properBind.myRec.setVisibility(View.GONE);
        properBind.title2.setVisibility(View.GONE);
        properBind.add2.setVisibility(View.VISIBLE);
        properBind.recView.setVisibility(View.GONE);
        properBind.progress.setVisibility(View.VISIBLE);
    }

}