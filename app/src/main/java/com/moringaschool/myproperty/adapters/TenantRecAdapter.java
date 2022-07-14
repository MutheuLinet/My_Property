package com.moringaschool.myproperty.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moringaschool.myproperty.R;
import com.moringaschool.myproperty.models.Tenant;

import java.text.DateFormat;
import java.util.List;

public class TenantRecAdapter extends RecyclerView.Adapter<TenantRecAdapter.myHolder> {
    List<Tenant> allTenants;
    Context context;

    public TenantRecAdapter(List<Tenant> allTenants, Context context) {
        this.allTenants = allTenants;
        this.context = context;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_defect, parent, false);
        return new myHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        holder.setData(allTenants.get(position));
    }

    @Override
    public int getItemCount() {
        return allTenants.size();
    }

    public class myHolder extends RecyclerView.ViewHolder {
        TextView name, description, occupied, joined, propertyName, unit_name;

        public myHolder(@NonNull View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.propertyName);
            description =  itemView.findViewById(R.id.propertyDescription);
            occupied = itemView.findViewById(R.id.occupied);
            propertyName = itemView.findViewById(R.id.name);
            unit_name = itemView.findViewById(R.id.unit_name);
            joined = itemView.findViewById(R.id.joined);
        }

        public void setData(Tenant tenant){
            name.setText("Tenant name: "+tenant.getTenant_name());
            description.setText("Found in: "+tenant.getProperty_name());
            String date = DateFormat.getDateTimeInstance().format(tenant.getJoined());
            occupied.setText("Lives in house: "+tenant.getUnit_name());
            propertyName.setText("Tenant phone number: "+tenant.getTenant_phone());
            unit_name.setText("Tenant Email: "+tenant.getTenant_email());
            joined.setText("Joined in: "+date);
        }
    }
}
