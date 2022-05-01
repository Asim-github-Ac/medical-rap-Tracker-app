package com.example.medical_rap_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    ImageView imgpicurl;
    TextView tvname,tvlike,tvsample,tvabout,tvspcieal,tvcnic,medicine_name;
    List<UserModel_Data> data=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_user);
        tvname=findViewById(R.id.dr_name);
        tvcnic=findViewById(R.id.dr_cnic);
        medicine_name=findViewById(R.id.medicine_name);
        tvspcieal=findViewById(R.id.special_name);
        tvabout=findViewById(R.id.dr_about_name);
        imgpicurl=findViewById(R.id.imgurl);
        tvspcieal=findViewById(R.id.sample_decision);

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
                    String name= data.get(0).getDr_name();
                    String medicinename=data.get(0).getMedicne_name();
                    String cnice= data.get(0).getCinicname();
                    String special_name=data.get(0).getSpecial_name();
                    String likedecision=data.get(0).getLike_decion();
                    String picurl=data.get(0).getPicurl();
                    String sampledecion= data.get(0).getSample_decision();
                    String loc= data.get(0).getLocation();
                    String about_doc=data.get(0).getDr_about_name();


                    tvname.setText("Name : "+name);
                    tvcnic.setText("CNIC : "+cnice);
                    tvspcieal.setText("Special : "+special_name);
                    tvabout.setText("About Dr : "+about_doc);
                    medicine_name.setText("Medicine Name : "+medicinename);
                    tvsample.setText("Sample : "+sampledecion);

                    Picasso.get().load(picurl).into(imgpicurl);
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