package com.example.medical_rap_tracker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medical_rap_tracker.Details_User_Activity;
import com.example.medical_rap_tracker.Model.UserModel_Data;
import com.example.medical_rap_tracker.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Details_Adapter extends RecyclerView.Adapter<Details_Adapter.myHolder> {
    Context context;
    List<UserModel_Data> userModel_data;

    public Details_Adapter(Context context, List<UserModel_Data> userModel_data) {
        this.context = context;
        this.userModel_data = userModel_data;
    }

    @NonNull
    @Override
    public Details_Adapter.myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.details_items,parent,false);
        return new Details_Adapter.myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Details_Adapter.myHolder holder, int position) {
        UserModel_Data userModel_data1=userModel_data.get(position);
        holder.tvname.setText("Name : "+userModel_data1.getDr_name());
        holder.tvcnic.setText("CNIC : "+userModel_data1.getCinicname());
        holder.tvspcieal.setText("Special : "+userModel_data1.getSpecial_name());
        holder.tvabout.setText("About Dr : "+userModel_data1.getDr_about_name());
        holder.medicine_name.setText("Medicine Name : "+userModel_data1.getMedicne_name());
        holder.tvsample.setText("Sample : "+userModel_data1.getSample_decision());

        Picasso.get().load(userModel_data1.getPicurl()).into(holder.imgpicurl);
    }

    @Override
    public int getItemCount() {
        return userModel_data.size();
    }

    public class myHolder extends RecyclerView.ViewHolder {
        TextView tvname,tvlike,tvsample,tvabout,tvspcieal,tvcnic,medicine_name;
        ImageView imgpicurl;
        public myHolder(@NonNull View itemView) {
            super(itemView);
            tvname=itemView.findViewById(R.id.dr_name);
            tvcnic=itemView.findViewById(R.id.dr_cnic);
            medicine_name=itemView.findViewById(R.id.medicine_name);
            tvspcieal=itemView.findViewById(R.id.special_name);
            tvabout=itemView.findViewById(R.id.dr_about_name);
            imgpicurl=itemView.findViewById(R.id.imgurl);
            tvspcieal=itemView.findViewById(R.id.sample_decision);
        }
    }
}
