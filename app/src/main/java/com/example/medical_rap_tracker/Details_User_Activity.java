package com.example.medical_rap_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medical_rap_tracker.Adapter.Details_Adapter;
import com.example.medical_rap_tracker.Model.UserModel_Data;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Details_User_Activity extends AppCompatActivity {

    String path;
   RecyclerView recyclerView;

    List<UserModel_Data> data=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_user);
        recyclerView=findViewById(R.id.recy_details);


        Intent intent=getIntent();
        path=intent.getStringExtra("email");

        GetDetails();
    }
    public void GetDetails(){
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        firestore.collection("User Data").document("data").collection(path).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()){
                    Toast.makeText(Details_User_Activity.this, "Record Not Found", Toast.LENGTH_SHORT).show();
                }else {
                    List<UserModel_Data> model_data=queryDocumentSnapshots.toObjects(UserModel_Data.class);
                    data.addAll(model_data);

                  recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                  recyclerView.setAdapter(new Details_Adapter(getApplicationContext(),data));

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Details_User_Activity.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}