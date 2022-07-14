package com.moringaschool.myproperty.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moringaschool.myproperty.databinding.AddPropertyManagerBinding;
import com.moringaschool.myproperty.api.ApiCalls;
import com.moringaschool.myproperty.api.RetrofitClient;
import com.moringaschool.myproperty.models.Constants;
import com.moringaschool.myproperty.models.Property;
import com.moringaschool.myproperty.models.PropertyManager;
import com.moringaschool.myproperty.models.Validator;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddManagerActivity extends AppCompatActivity implements View.OnClickListener{
    AddPropertyManagerBinding addBind;
    Call<PropertyManager> call1;
    Call<Property> call2;
    FirebaseAuth myAuth;
    FirebaseAuth.AuthStateListener myAuthListener;
    DatabaseReference ref;
    SharedPreferences myData;
    SharedPreferences.Editor myDataEditor;
    Property property;
    PropertyManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addBind = AddPropertyManagerBinding.inflate(getLayoutInflater());
        setContentView(addBind.getRoot());

        myData = PreferenceManager.getDefaultSharedPreferences(this);
        myDataEditor = myData.edit();

        addBind.submit.setOnClickListener(this);
        myAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onClick(View v) {

        ApiCalls calls = RetrofitClient.getClient();

        String name = addBind.managerName.getEditText().getText().toString().trim();
        String number = addBind.managerPhone.getEditText().getText().toString().trim();
        String email = addBind.managerEmail.getEditText().getText().toString().trim();
        String propertyName = addBind.managerHouseName.getEditText().getText().toString().trim();
        String propertyLocation = addBind.propertyDescription.getEditText().getText().toString().trim();
        String password = addBind.propertyManagerPassword.getEditText().getText().toString().trim();

        if(!Validator.validateName(addBind.managerName) || !Validator.validatePhone(addBind.managerPhone) || !Validator.validateEmail(addBind.managerEmail) || !Validator.validateName(addBind.managerHouseName) || !Validator.validateName(addBind.propertyDescription) || !Validator.validatePass(addBind.propertyManagerPassword) || !Validator.validatePassword(addBind.propertyManagerPassword,addBind.propertyConfirmPassword)){
            return;
        }

        myDataEditor.putString(Constants.NAME, name).apply();
        myDataEditor.putString(Constants.PHONE_NUMBER, number).apply();
        myDataEditor.putString(Constants.EMAIL, email).apply();

        manager = new PropertyManager(name, number, email);
        property = new Property(propertyName, name, propertyLocation);

        call1 = calls.addManager(manager);
        call2 = calls.addProperty(property);
        call1.enqueue(new Callback<PropertyManager>() {
            @Override
            public void onResponse(Call<PropertyManager> call, Response<PropertyManager> response) {
                if (response.isSuccessful()){

                    call2.enqueue(new Callback<Property>() {
                        @Override
                        public void onResponse(Call<Property> call, Response<Property> response) {
                            if (response.isSuccessful()){

                                myAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()){

                                        Intent intent = new Intent(AddManagerActivity.this, PropertiesActivity.class);
                                        intent.putExtra("managerName", name);
                                        Toast.makeText(AddManagerActivity.this, "User created successfully "+name, Toast.LENGTH_SHORT).show();

                                        startActivity(intent);
                                    }
                                });


                            }else{
                                Toast.makeText(AddManagerActivity.this, "User not created successfully", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Property> call, Throwable t) {
                            String error = t.getMessage();
                            Toast.makeText(AddManagerActivity.this, error, Toast.LENGTH_SHORT).show();

                        }
                    });

                    Toast.makeText(AddManagerActivity.this, "Manager Added", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AddManagerActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<PropertyManager> call, Throwable t) {
                String error = t.getMessage();
                Toast.makeText(AddManagerActivity.this, error, Toast.LENGTH_SHORT).show();

            }
        });


    }
}