package com.moringaschool.myproperty.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.moringaschool.myproperty.databinding.AddTenantBinding;
import com.moringaschool.myproperty.api.ApiCalls;
import com.moringaschool.myproperty.api.RetrofitClient;
import com.moringaschool.myproperty.models.Property;
import com.moringaschool.myproperty.models.Tenant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TenantDialogFragment extends DialogFragment {
    String name, phone, email, tenantId ;
    AddTenantBinding tenantBind;
    Call<Tenant> call1;
    Property property;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tenantBind = AddTenantBinding.inflate( inflater);
        View view = tenantBind.getRoot();
        getDialog().setTitle("Tenant Dialog");

        if (getArguments() !=null){
            property = (Property) getArguments().getSerializable("property");
        }



        ApiCalls calls = RetrofitClient.getClient();


        tenantBind.addTenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = tenantBind.tenantUsername.getEditText().getText().toString().trim();
                phone =  tenantBind.editTextPhone.getEditText().getText().toString().trim();
                email = tenantBind.editTextEmail.getEditText().getText().toString().trim();
                tenantId = tenantBind.tenantId.getEditText().getText().toString().trim();

//                Tenant newTenant = new Tenant(name, email, phone, tenantId, property.getId(), property.getManager_id());
//                call1 = calls.addTenant(newTenant);
//                call1.enqueue(new Callback<Tenant>() {
//                    @Override
//                    public void onResponse(Call<Tenant> call, Response<Tenant> response) {
//                        if (response.isSuccessful()){
//                            Log.d("TAG", "onSuccess: Successfully" );
//                        }else{
//                            Log.d("TAG", "onFailure: failed");
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<Tenant> call, Throwable t) {
//                        String error = t.getMessage();
//                        Log.d("TAG", "onFailure: "+ error);
//                    }
//                });

                dismiss();
            }

        });

        return view;
    }
}
