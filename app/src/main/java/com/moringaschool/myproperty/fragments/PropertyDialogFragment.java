package com.moringaschool.myproperty.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.moringaschool.myproperty.api.ApiCalls;
import com.moringaschool.myproperty.api.RetrofitClient;
import com.moringaschool.myproperty.databinding.AddPropertyDialogBinding;
import com.moringaschool.myproperty.models.Constants;
import com.moringaschool.myproperty.models.Property;
import com.moringaschool.myproperty.models.Tenant;
import com.moringaschool.myproperty.ui.PropertiesActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertyDialogFragment extends DialogFragment {
    String name,location ;
    AddPropertyDialogBinding propertyBind;
    Call<Property> call1;
    SharedPreferences pref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        propertyBind = AddPropertyDialogBinding.inflate(inflater);
        View view = propertyBind.getRoot();
        getDialog().setTitle("Property Dialog");

        pref = PreferenceManager.getDefaultSharedPreferences(getContext());

       propertyBind.submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               name = propertyBind.name.getEditText().getText().toString().trim();
               location  = propertyBind.location.getEditText().getText().toString();

               Property property = new Property(name,pref.getString(Constants.NAME, "Charles"),location );
               ApiCalls calls = RetrofitClient.getClient();

               call1 = calls.addProperty(property);
               call1.enqueue(new Callback<Property>() {
                   @Override
                   public void onResponse(Call<Property> call, Response<Property> response) {
                       if (response.isSuccessful()){
                           Toast.makeText(getContext(), "Property successfully added", Toast.LENGTH_SHORT).show();
                       }
                   }

                   @Override
                   public void onFailure(Call<Property> call, Throwable t) {
                       String error = t.getMessage();
                       Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                   }
               });
           }
       });

        return view;
    }
}
