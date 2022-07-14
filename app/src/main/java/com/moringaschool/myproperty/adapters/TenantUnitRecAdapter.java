package com.moringaschool.myproperty.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.moringaschool.myproperty.R;
import com.moringaschool.myproperty.api.ApiCalls;
import com.moringaschool.myproperty.api.RetrofitClient;
import com.moringaschool.myproperty.models.Tenant;
import com.moringaschool.myproperty.models.Unit;
import com.moringaschool.myproperty.ui.TenantsDoneDefects;

import java.text.DateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TenantUnitRecAdapter extends RecyclerView.Adapter<TenantUnitRecAdapter.myHolder> {
    List<Unit> allUnits;
    Context cont;
    Call<Tenant> call;
    ApiCalls calls;
    Tenant tenant;

    public TenantUnitRecAdapter(List<Unit> allUnits, Context cont) {
        this.allUnits = allUnits;
        this.cont = cont;
    }

    @NonNull
    @Override
    public TenantUnitRecAdapter.myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(cont).inflate(R.layout.activity_unit, parent, false);
        return new myHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TenantUnitRecAdapter.myHolder holder, int position) {
        holder.setData(allUnits.get(position));

        calls = RetrofitClient.getClient();
        call = calls.getTenant(allUnits.get(position).getUnitName());

        call.clone().enqueue(new Callback<Tenant>() {
            @Override
            public void onResponse(Call<Tenant> call, Response<Tenant> response) {
                if (response.isSuccessful()){
                    tenant = response.body();
                    holder.tenantName1.setText("Occupied by: "+tenant.getTenant_name());
                    holder.tenantPhone2.setText("Mobile number"+tenant.getTenant_phone());
                    String date = DateFormat.getDateTimeInstance().format(tenant.getJoined());
                    holder.joined.setText("Joined in: " +date);

                }else{
                    holder.tenantName1.setText("Vacant");
                    holder.tenantPhone2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Tenant> call, Throwable t) {
                String error = t.getMessage();
                Toast.makeText(cont, error, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {

        return allUnits.size();
    }

    public class myHolder extends RecyclerView.ViewHolder{
        TextView name, tenantName1, tenantPhone2, unitRooms, joined;
        ImageView add, remove;

        Unit unit;



        public myHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.quest_type);
            tenantName1 = itemView.findViewById(R.id.quest_ans);
            tenantPhone2 = itemView.findViewById(R.id.quest_mode);
            unitRooms = itemView.findViewById(R.id.location);
            remove = itemView.findViewById(R.id.remove);
            add = itemView.findViewById(R.id.add);
            joined = itemView.findViewById(R.id.joined);

            add.setVisibility(View.GONE);
        }

        public void setData(Unit unit) {
            this.unit = unit;
            name.setText(unit.getUnit_name());
            tenantName1.setText("Vacant");
            unitRooms.setText("This unit contains " + unit.getUnit_rooms() + " rooms");

        }
    }
}
