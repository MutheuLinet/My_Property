package com.moringaschool.myproperty.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moringaschool.myproperty.R;
import com.moringaschool.myproperty.api.ApiCalls;
import com.moringaschool.myproperty.api.RetrofitClient;
import com.moringaschool.myproperty.databinding.FragmentDefectBinding;
import com.moringaschool.myproperty.models.Constants;
import com.moringaschool.myproperty.models.Defect;
import com.moringaschool.myproperty.models.DoneDefect;
import com.moringaschool.myproperty.models.Tenant;
import com.moringaschool.myproperty.models.Validator;
import com.moringaschool.myproperty.ui.PropertiesActivity;
import com.moringaschool.myproperty.ui.TenantDashboardActivity;
import com.moringaschool.myproperty.ui.TenantLoginActivity;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DefectFragment extends Fragment {
    FragmentDefectBinding defBind;
    Defect defect;
    DatabaseReference ref;
    String tenantId;
    BottomSheetDialog dialog;
    Call<DoneDefect> call;
    Call<List<Defect>> defectDeleteCall;
    ApiCalls calls;

    public DefectFragment() {
        // Required empty public constructor
    }


    public static DefectFragment newInstance(Defect defect) {
        DefectFragment fragment = new DefectFragment();
        Bundle args = new Bundle();
        args.putSerializable("defect", (Serializable) defect);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            defect = (Defect) getArguments().getSerializable("defect");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        defBind = FragmentDefectBinding.inflate(inflater, container, false);
        View v = defBind.getRoot();
        dialog = new BottomSheetDialog(getContext());

        calls = RetrofitClient.getClient();

        createDialog();

        Picasso.get().load(defect.getString_uri()).into(defBind.animationView);
        defBind.catName.setText("Defect: " + defect.getDescription());
        defBind.questType.setText("Unit Name: "+defect.getUnit_name());
        defBind.contractorName.setText(defect.getDescription());
        defBind.phone.setText("Please assign a relevant contractor to fix the problem before it damages your property.");
        defBind.locat.setText("Use the plus button above to assign a contractor to the given defect.");
        String date = DateFormat.getDateTimeInstance().format(defect.getCreated_at());
        defBind.tenantName.setText("Found in: "+ defect.getProperty_name());
        defBind.tenantDes.setText("Posted in: "+ date);

        ref = FirebaseDatabase.getInstance().getReference();

        Query query = ref.child("Tenants").orderByChild("tenant_id").equalTo(defect.getTenant_id());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    String tenantName = snapshot.child(defect.getTenant_id()).child("tenant_name").getValue(String.class);
                    String tenantPhone = snapshot.child(defect.getTenant_id()).child("tenant_phone").getValue(String.class);
                    String tenantEmail = snapshot.child(defect.getTenant_id()).child("tenant_email").getValue(String.class);
                    tenantId = snapshot.child(defect.getTenant_id()).child("tenant_id").getValue(String.class);

                    defBind.questAns.setText("Unit Tenant: "+tenantName);
                    defBind.questMode.setText(tenantPhone);
                    defBind.location.setText(tenantEmail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError t) {
                String error = t.getMessage();
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        defBind.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        return v;
    }

    private void createDialog() {
        View v = getLayoutInflater().inflate(R.layout.add_contractor, null, false);
        TextInputLayout name, phone, location;
        MaterialButton btn;

        name = v.findViewById(R.id.name);
        phone = v.findViewById(R.id.phone);
        location = v.findViewById(R.id.location);
        btn = v.findViewById(R.id.submit);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactorName = name.getEditText().getText().toString().trim();
                String contactorPhone = phone.getEditText().getText().toString().trim();
                String contactorLocation = location.getEditText().getText().toString().trim();

                if(!Validator.validateName(name) || !Validator.validatePhone(phone) || !Validator.validateName(location)){
                    return;
                }

                DoneDefect doneDefect = new DoneDefect(defect.getDescription(),defect.getString_uri(),contactorName,contactorPhone,contactorLocation,defect.getManager_name(),defect.getTenant_id());
                call = calls.addDefect(doneDefect);

                call.clone().enqueue(new Callback<DoneDefect>() {
                    @Override
                    public void onResponse(Call<DoneDefect> call, Response<DoneDefect> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(getContext(), "Assigned a contractor", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                            defectDeleteCall = calls.deleteDefect(defect.getId());
                            defectDeleteCall.enqueue(new Callback<List<Defect>>() {
                                        @Override
                                        public void onResponse(Call<List<Defect>> call, Response<List<Defect>> response) {
                                            if (response.body() != null){
                                                List<Defect> remDefs = new ArrayList<>();
                                                remDefs = response.body();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<List<Defect>> call, Throwable t) {
                                            String error = t.getMessage();
                                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                                        }
                                    });

                            startActivity(new Intent(getContext(), PropertiesActivity.class));

                        }
                    }

                    @Override
                    public void onFailure(Call<DoneDefect> call, Throwable t) {
                        String error = t.getMessage();
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    }
                });

//                ref.child("DoneDefects").child(doneDefect.getName()).setValue(doneDefect).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()){
//                            defBind.contractorName.setText(doneDefect.getName());
//                            defBind.phone.setText(contactorPhone);
//                            defBind.locat.setText(contactorLocation);
//                            defBind.tenantName.setText(doneDefect.getManager_name());
//
//                        }
//                    }
//                });
//
            }
        });

        dialog.setContentView(v);
    }

    @Override
    public void onResume() {
        super.onResume();



        Query q = ref.child("DoneDefects").orderByChild("name").equalTo(defect.getDescription());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String contactorName= snapshot.child(defect.getDescription()).child("contactorName").getValue(String.class);
                    String contactorPhone = snapshot.child(defect.getDescription()).child("contactorPhone").getValue(String.class);
                    String contactorLocation = snapshot.child(defect.getDescription()).child("contactorLocation").getValue(String.class);
                    String managerName = snapshot.child(defect.getDescription()).child("managerName").getValue(String.class);


                    defBind.contractorName.setText(defect.getDescription());
                    defBind.phone.setText(contactorPhone);
                    defBind.locat.setText(contactorLocation);
                    defBind.tenantName.setText(managerName);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError t) {
                String error = t.getMessage();
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

            }
        });
    }
}