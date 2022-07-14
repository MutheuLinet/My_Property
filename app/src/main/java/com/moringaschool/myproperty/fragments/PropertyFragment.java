package com.moringaschool.myproperty.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.moringaschool.myproperty.R;
import com.moringaschool.myproperty.adapters.UnitRecAdapter;
import com.moringaschool.myproperty.api.ApiCalls;
import com.moringaschool.myproperty.api.RetrofitClient;
import com.moringaschool.myproperty.databinding.FragmentPropertyBinding;
import com.moringaschool.myproperty.models.Constants;
import com.moringaschool.myproperty.models.Property;
import com.moringaschool.myproperty.models.Unit;
import com.moringaschool.myproperty.models.Validator;
import com.moringaschool.myproperty.ui.PropertiesActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PropertyFragment extends Fragment {
    Property property;
    FragmentPropertyBinding fragBind;
    SharedPreferences pref;
    BottomSheetDialog dialog;
    Call<Unit> call;
    Call<List<Unit>> call1;
    ApiCalls calls;
    List<Unit> allUnits;
    UnitRecAdapter adp;




    public PropertyFragment() {
        // Required empty public constructor
    }


    public static PropertyFragment newInstance(Property property) {
        PropertyFragment fragment = new PropertyFragment();
        Bundle args = new Bundle();
        args.putSerializable("property", (Serializable) property);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            property = (Property) getArguments().getSerializable("property");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragBind = FragmentPropertyBinding.inflate(inflater, container ,false);
        View v  = fragBind.getRoot();

        calls = RetrofitClient.getClient();


        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        dialog = new BottomSheetDialog(getContext());
        createDialog();

        allUnits = new ArrayList<>();


        fragBind.catName.setText("Property name: " +property.getProperty_name());
        fragBind.questType.setText("Property owner: "+pref.getString(Constants.NAME, ""));
        fragBind.questAns.setText(pref.getString(Constants.PHONE_NUMBER, ""));
        fragBind.questMode.setText(pref.getString(Constants.EMAIL, ""));
        fragBind.location.setText(property.getProperty_location());



        fragBind.add.setOnClickListener(v1 -> {
//            FragmentManager fm = getParentFragmentManager();
//            TenantDialogFragment fragment = new TenantDialogFragment();
//
//            Bundle args = new Bundle();
//            args.putSerializable("property" , (Serializable) property);
//            fragment.setArguments(args);
//
//            fragment.show(getParentFragmentManager(),"Tenant Dialog");

            dialog.show();
        });
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        return v;
    }

    private void createDialog() {
        View v = getActivity().getLayoutInflater().inflate(R.layout.add_unit, null, false);

        TextInputLayout name = v.findViewById(R.id.unit_name);
        TextInputLayout rooms = v.findViewById(R.id.unit_rooms);
        MaterialButton btn = v.findViewById(R.id.submit);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unitName = name.getEditText().getText().toString().trim();
                String unitRooms = rooms.getEditText().getText().toString().trim();

                if (!Validator.validateName(name) || !Validator.validateName(rooms)){
                    return;
                }

                Unit unit = new Unit(unitName, unitRooms, property.getProperty_name());

                call = calls.addUnit(unit);

                call.enqueue(new Callback<Unit>() {
                    @Override
                    public void onResponse(Call<Unit> call, Response<Unit> response) {
                        Toast.makeText(getContext(), "Successfully added", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    @Override
                    public void onFailure(Call<Unit> call, Throwable t) {
                        String error = t.getMessage();
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.setContentView(v);
    }

    @Override
    public void onResume() {
        super.onResume();
        call1 = calls.propertyUnits(property.getProperty_name());
        call1.enqueue(new Callback<List<Unit>>() {
            @Override
            public void onResponse(Call<List<Unit>> call, Response<List<Unit>> response) {
                allUnits = response.body();
                adp = new UnitRecAdapter(allUnits, getContext());
                fragBind.cont2.setAdapter(adp);
                fragBind.cont2.setLayoutManager(new LinearLayoutManager(getContext()));
                fragBind.cont2.setHasFixedSize(true);
            }

            @Override
            public void onFailure(Call<List<Unit>> call, Throwable t) {
                String error = t.getMessage();
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}