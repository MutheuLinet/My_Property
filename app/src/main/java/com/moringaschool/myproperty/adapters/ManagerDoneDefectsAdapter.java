package com.moringaschool.myproperty.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.moringaschool.myproperty.R;
import com.moringaschool.myproperty.models.DoneDefect;

import java.text.DateFormat;
import java.util.List;

public class ManagerDoneDefectsAdapter extends RecyclerView.Adapter<ManagerDoneDefectsAdapter.myHolder> {
    List<DoneDefect> allDoneDefects;
    Context cont;

    public ManagerDoneDefectsAdapter(List<DoneDefect> allDoneDefects, Context cont) {
        this.allDoneDefects = allDoneDefects;
        this.cont = cont;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(cont).inflate(R.layout.property, parent, false);
        return new myHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        holder.setData(allDoneDefects.get(position));

    }

    @Override
    public int getItemCount() {
        return allDoneDefects.size();
    }

    public class myHolder extends RecyclerView.ViewHolder {
        TextView name, description, managerName, phone, date;
        ShapeableImageView img;

        public myHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.propertyName);
            description = itemView.findViewById(R.id.propertyDescription);
            img = itemView.findViewById(R.id.image);
            managerName = itemView.findViewById(R.id.occupied);
            phone = itemView.findViewById(R.id.manager_name);
            date = itemView.findViewById(R.id.joined);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getLayoutPosition();
//                    Intent intent = new Intent(cont, DefectsActivity.class);
//                    intent.putExtra("position", position);
//                    intent.putExtra("allDefects", (Serializable) allDefects);
//                    cont.startActivity(intent);
//                    Toast.makeText(cont, "Coming Wait and Relax ", Toast.LENGTH_LONG).show();
//                }
//            });

        }

        public void setData(DoneDefect defect) {
            name.setText(defect.getName());
            description.setText("Assigned: " + defect.getContactor_name());
            Glide.with(cont)
                    .asBitmap()
                    .load(defect.getUri())
                    .into(img);
            managerName.setText("Assigned by: " + defect.getManager_name());
            phone.setText("Contractor phone: " + defect.getContractor_phone() + " Found at: "+defect.getContractor_location());
            String newDate = DateFormat.getDateTimeInstance().format(defect.getCreated_at());
            date.setText("Posted in: " + newDate);
        }
    }
}
