package com.moringaschool.myproperty.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moringaschool.myproperty.R;
import com.moringaschool.myproperty.api.ApiCalls;
import com.moringaschool.myproperty.api.RetrofitClient;
import com.moringaschool.myproperty.databinding.ActivityUnitDetailsBinding;
import com.moringaschool.myproperty.models.Constants;
import com.moringaschool.myproperty.models.Tenant;
import com.moringaschool.myproperty.models.Unit;
import com.moringaschool.myproperty.models.Validator;
import com.moringaschool.myproperty.ui.UnitDetailsActivity;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnitRecAdapter extends RecyclerView.Adapter<UnitRecAdapter.myHolder> {
    List<Unit> allUnits;
    Context cont;
    Call<Tenant> call;
    ApiCalls calls;
    Tenant tenant;

    public UnitRecAdapter(List<Unit> allUnits, Context cont) {
        this.allUnits = allUnits;
        this.cont = cont;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(cont).inflate(R.layout.activity_unit, parent, false);
        return new myHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        holder.setData(allUnits.get(position));

        calls = RetrofitClient.getClient();
        call = calls.getTenant(allUnits.get(position).getUnitName());

        call.enqueue(new Callback<Tenant>() {
            @Override
            public void onResponse(Call<Tenant> call, Response<Tenant> response) {
                if (response.body() != null){
                    tenant = response.body();
                    holder.tenantName1.setText("Occupied by: "+tenant.getTenant_name());
                    holder.tenantPhone2.setText("Mobile number: "+tenant.getTenant_phone());
                    String date = DateFormat.getDateTimeInstance().format(tenant.getJoined());
                    holder.date.setText(date);
                    holder.add.setVisibility(View.GONE);
                }else{
                    holder.tenantName1.setText("Vacant");
                    holder.tenantPhone2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Tenant> call, Throwable t) {
                String error = t.getMessage();
            }
        });

    }

    @Override
    public int getItemCount() {

        return allUnits.size();
    }

    public class myHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, tenantName1, tenantPhone2, unitRooms, date;
        ImageView add, remove;
        BottomSheetDialog dialog;
        Unit unit;
        Call<Tenant> call, call1;
        ApiCalls calls;
        Tenant tenant, tenant1;
        SharedPreferences pref;


        public myHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.quest_type);
            tenantName1 = itemView.findViewById(R.id.quest_ans);
            tenantPhone2 = itemView.findViewById(R.id.quest_mode);
            unitRooms = itemView.findViewById(R.id.location);
            remove = itemView.findViewById(R.id.remove);
            date = itemView.findViewById(R.id.joined);

            add = itemView.findViewById(R.id.add);

            pref = PreferenceManager.getDefaultSharedPreferences(cont);
            calls = RetrofitClient.getClient();

            dialog = new BottomSheetDialog(cont);
            createDialog();
            add.setOnClickListener(this);
            remove.setOnClickListener(this);
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    Intent intent = new Intent(cont, UnitDetailsActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("allUnits", (Serializable) allUnits);
                    cont.startActivity(intent);
                }
            });
        }

        public void setData(Unit unit){
            name.setText(unit.getUnit_name());
            unitRooms.setText("This unit contains "+unit.getUnit_rooms() + " rooms");
            this.unit = unit;
        }



        @Override
        public void onClick(View v) {
            if (v == add){
                dialog.show();
            }
        }

        private void createDialog() {
            View v = LayoutInflater.from(cont).inflate(R.layout.add_tenant, null, false);



            TextInputLayout name = v.findViewById(R.id.tenantUsername);
            TextInputLayout phone = v.findViewById(R.id.editTextPhone);
            TextInputLayout email = v.findViewById(R.id.editTextEmail);
            TextInputLayout id = v.findViewById(R.id.tenantId);
            MaterialButton btn = v.findViewById(R.id.addTenant);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String tenantName = name.getEditText().getText().toString().trim();
                    String tenantPhone = phone.getEditText().getText().toString().trim();
                    String tenantEmail = email.getEditText().getText().toString().trim();
                    String tenantId = id.getEditText().getText().toString().trim();

                    if(!Validator.validateName(name) || !Validator.validatePhone(phone) || !Validator.validateEmail(email) || !Validator.validatePass(id)){
                        return;
                    }

                    tenant = new Tenant(tenantName,tenantEmail,tenantPhone,tenantId, unit.getProperty_name(), unit.getUnit_name(),pref.getString(Constants.NAME, "") );

                    call = calls.addTenant(tenant);
                    call.enqueue(new Callback<Tenant>() {
                        @Override
                        public void onResponse(Call<Tenant> call, Response<Tenant> response) {
                            if (response.isSuccessful()){
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Tenants");
                                ref.child(tenantId).setValue(tenant);
                                tenantName1.setText(tenantName);
                                tenantPhone2.setText(tenantPhone);
                                Toast.makeText(cont, "Tenant successfully onboarded", Toast.LENGTH_SHORT).show();

                                call1 = calls.getTenant(unit.getUnitName());
                                call1.enqueue(new Callback<Tenant>() {
                                    @Override
                                    public void onResponse(Call<Tenant> call, Response<Tenant> response) {
                                        if (response.isSuccessful()){
                                            tenant1 = response.body();
                                            tenantName1.setText(tenant1.getTenant_name());
                                            tenantPhone2.setText(tenant1.getTenant_phone());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Tenant> call, Throwable t) {
                                        String error = t.getMessage();
                                        Toast.makeText(cont, error, Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }else{
                                Toast.makeText(cont, "please try again", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<Tenant> call, Throwable t) {
                            String error = t.getMessage();
                            Toast.makeText(cont, error, Toast.LENGTH_SHORT).show();


                        }
                    });
                }

            });

            dialog.setContentView(v);
        }
    }
}
