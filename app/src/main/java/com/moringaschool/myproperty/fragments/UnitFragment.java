package com.moringaschool.myproperty.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.moringaschool.myproperty.R;
import com.moringaschool.myproperty.api.ApiCalls;
import com.moringaschool.myproperty.api.RetrofitClient;
import com.moringaschool.myproperty.databinding.FragmentUnitBinding;
import com.moringaschool.myproperty.models.Constants;
import com.moringaschool.myproperty.models.Tenant;
import com.moringaschool.myproperty.models.Unit;
import com.moringaschool.myproperty.models.Validator;
import com.moringaschool.myproperty.ui.PropertiesActivity;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UnitFragment extends Fragment {
    Unit unit;
    FragmentUnitBinding fragBind;
    Call<Tenant> call, call1;
    Call<List<Unit>> deleteCall;
    ApiCalls calls;
    BottomSheetDialog bottom;
    String tName, tPhone, tEmail, tId;
    int idTenant;
    SharedPreferences pref;




    public UnitFragment() {
        // Required empty public constructor
    }

    public static UnitFragment newInstance(Unit unit) {
        UnitFragment fragment = new UnitFragment();
        Bundle args = new Bundle();
        args.putSerializable("unit", (Serializable) unit);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            unit = (Unit) getArguments().getSerializable("unit");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragBind = FragmentUnitBinding.inflate(inflater);
        View v = fragBind.getRoot();

        fragBind.catName.setText(unit.getUnitName());
        bottom = new BottomSheetDialog(getContext());
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());

        calls = RetrofitClient.getClient();
        call = calls.getTenant(unit.getUnitName());
        call.clone().enqueue(new Callback<Tenant>() {
            @Override
            public void onResponse(Call<Tenant> call, Response<Tenant> response) {
                if (response.body() != null){
                    Tenant tenant = response.body();
                    tName = tenant.getTenant_name();
                    tPhone = tenant.getTenant_phone();
                    tEmail = tenant.getTenant_email();
                    tId = tenant.getTenant_id();
                    idTenant = tenant.getId();

                    fragBind.questType.setText("Unit Tenant: "+ tenant.getTenant_name());
                    String date = DateFormat.getDateTimeInstance().format(tenant.getJoined());
                    fragBind.questAns.setText("Joined in: "+ date);
                    fragBind.questMode.setText(tenant.getTenant_phone());
                    fragBind.location.setText(tenant.getTenant_email());
                }
            }

            @Override
            public void onFailure(Call<Tenant> call, Throwable t) {

            }
        });

        createDialog();

        fragBind.contractorName.setText(unit.getUnitName());
        fragBind.phone.setText("This unit is found in " + unit.getProperty_name() + " and it contains "+ unit.getUnit_rooms() + " rooms." );
        fragBind.locat.setText("You can use the update button above to replace the tenant in case he/she wants to shift from one room to the other. You can also use the delete button below to erase the unit");
        fragBind.tenantName.setText("Manager in charge: "+pref.getString(Constants.NAME,""));
        fragBind.tenantDes.setText("Contains "+ unit.getUnit_rooms() + " rooms");

        fragBind.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom.show();
            }
        });

        fragBind.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCall = calls.deleteUnit(unit.getId());

                deleteCall.clone().enqueue(new Callback<List<Unit>>() {
                    @Override
                    public void onResponse(Call<List<Unit>> call, Response<List<Unit>> response) {
                        if (response.isSuccessful()){
                            List<Unit> allUnits = new ArrayList<>();
                            allUnits = response.body();
                            startActivity(new Intent(getContext(), PropertiesActivity.class));
                            Toast.makeText(getContext(), "Successfully deleted", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(), "Something happened", Toast.LENGTH_SHORT).show();

                        }
                    }
                    @Override
                    public void onFailure(Call<List<Unit>> call, Throwable t) {

                    }
                });
            }
        });
        return v;
    }

    private void createDialog() {
        View v = getLayoutInflater().inflate(R.layout.add_tenant, null, false);
        TextInputLayout name, phone, id, email;
        Button btn;

        name = v.findViewById(R.id.tenantUsername);
        phone = v.findViewById(R.id.editTextPhone);
        id = v.findViewById(R.id.tenantId);
        email = v.findViewById(R.id.editTextEmail);
        btn = v.findViewById(R.id.addTenant);

        Objects.requireNonNull(name.getEditText()).setText(tName);
        Objects.requireNonNull(phone.getEditText()).setText(tPhone);
        Objects.requireNonNull(id.getEditText()).setText(tId);
        Objects.requireNonNull(email.getEditText()).setText(tEmail);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTenantName = name.getEditText().getText().toString().trim();
                String newTenantPhone = phone.getEditText().getText().toString().trim();
                String newTenantEmail = email.getEditText().getText().toString().trim();
                String newTenantId = id.getEditText().getText().toString().trim();

                if(!Validator.validateName(name) || !Validator.validatePhone(phone) || !Validator.validateEmail(email) || !Validator.validatePass(id)){
                    return;
                }

                Tenant newTenant = new Tenant(newTenantName,newTenantEmail,newTenantPhone,newTenantId, unit.getProperty_name(), unit.getUnit_name(),pref.getString(Constants.NAME, ""));
                call1 = calls.updateTenant(idTenant, newTenant);

                call1.enqueue(new Callback<Tenant>() {
                    @Override
                    public void onResponse(Call<Tenant> call, Response<Tenant> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(getContext(), "Successfully updated", Toast.LENGTH_SHORT).show();
                            bottom.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Tenant> call, Throwable t) {
                        String error = t.getMessage();
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });

        bottom.setContentView(v);
    }
}