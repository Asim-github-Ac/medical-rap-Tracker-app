package com.example.medical_rap_tracker.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medical_rap_tracker.Details_User_Activity;
import com.example.medical_rap_tracker.Model.AdminAuth;
import com.example.medical_rap_tracker.R;

import java.util.List;

public class AdminView extends RecyclerView.Adapter<AdminView.myHolder> {
    Context context;
    List<AdminAuth> auths;

    public AdminView(Context context, List<AdminAuth> auths) {
        this.context = context;
        this.auths = auths;
    }

    @NonNull
    @Override
    public AdminView.myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.userlayout,parent,false);
        return new AdminView.myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminView.myHolder holder, int position) {

        AdminAuth adminAuth=auths.get(position);

            holder.tvfname.setText("Full Name : "+adminAuth.getFullnamre());
            holder.tvemail.setText("Email : "+adminAuth.getEmail());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, Details_User_Activity.class);
                intent.putExtra("email",adminAuth.getEmail());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return auths.size();
    }

    public class myHolder extends RecyclerView.ViewHolder {
        TextView tvfname,tvemail;
        CardView cardView;
        public myHolder(@NonNull View itemView) {
            super(itemView);
            tvemail=itemView.findViewById(R.id.emaillay);
            tvfname=itemView.findViewById(R.id.fname);
            cardView=itemView.findViewById(R.id.next);
        }
    }
}
