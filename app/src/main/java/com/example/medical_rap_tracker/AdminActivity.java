package com.example.medical_rap_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.medical_rap_tracker.Adapter.AdminView;
import com.example.medical_rap_tracker.Adapter.DoctorAdapter;
import com.example.medical_rap_tracker.Model.AdminAuth;
import com.example.medical_rap_tracker.Model.DoctorModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;

    AdminView adminView;
    List<AdminAuth> authList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        toolbar=findViewById(R.id.toolbar);
        recyclerView=findViewById(R.id.rvadmin);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));

        GetData();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_optional_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.emp:
                startActivity(new Intent(getApplicationContext(),AddNewEmpActivity.class));

                break;
            case R.id.signout:
                Toast.makeText(getApplicationContext(), "Sign Out", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getApplicationContext(), "Item not found", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void GetData(){
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        firestore.collection("All Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (queryDocumentSnapshots.isEmpty()){
                    Toast.makeText(AdminActivity.this, "user Not Found", Toast.LENGTH_SHORT).show();
                }else {
                    List<AdminAuth> auths=queryDocumentSnapshots.toObjects(AdminAuth.class);
                    authList.addAll(auths);
                    adminView=new AdminView(getApplicationContext(),authList);
                    recyclerView.setAdapter(adminView);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(AdminActivity.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}