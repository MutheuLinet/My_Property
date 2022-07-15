package com.moringaschool.myproperty.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.moringaschool.myproperty.databinding.ActivityLoginBinding;
import com.moringaschool.myproperty.models.Constants;
import com.moringaschool.myproperty.models.Validator;

public class ManagerLoginActivity extends AppCompatActivity implements View.OnClickListener{
    ActivityLoginBinding logBind;
    FirebaseAuth myAuth;
    SharedPreferences myData;
    SharedPreferences.Editor myDataEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        logBind = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(logBind.getRoot());

        myAuth = FirebaseAuth.getInstance();
        myData = PreferenceManager.getDefaultSharedPreferences(this);
        myDataEditor = myData.edit();

        logBind.addManager.setOnClickListener(this);
        logBind.login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == logBind.addManager){
            Intent intent = new Intent(ManagerLoginActivity.this, AddManagerActivity.class);
            startActivity(intent);
        }else if(v == logBind.login){
            login();
        }
    }

    private void login() {

        String email = logBind.userEmail.getEditText().getText().toString().trim();
        String password = logBind.password.getEditText().getText().toString().trim();

        if(!Validator.validateEmail(logBind.userEmail) || !Validator.validatePass(logBind.password)){
            return;
        }

        myDataEditor.putString(Constants.EMAIL, email).apply();

        myAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                logBind.password.setErrorEnabled(false);
                Intent intent = new Intent(ManagerLoginActivity.this, PropertiesActivity.class);
                startActivity(intent);
            }else{
                logBind.password.setError("Check credentials and try again");
                logBind.password.setErrorEnabled(true);
            }
        });
    }
}