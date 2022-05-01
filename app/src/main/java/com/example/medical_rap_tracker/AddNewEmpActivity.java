package com.example.medical_rap_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.medical_rap_tracker.Model.AdminAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddNewEmpActivity extends AppCompatActivity {

    TextInputEditText  uname,uemail,upass,uconfirmpass;
    Button btncreat;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_emp);
        uname=findViewById(R.id.username);
        uemail=findViewById(R.id.useremail);
        upass=findViewById(R.id.userpass);
        uconfirmpass=findViewById(R.id.userconfirm);
        btncreat=findViewById(R.id.creatand_account);
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Adding");
        progressDialog.setMessage("Loading......");

        btncreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                if (uname.getText().toString().isEmpty()){
                    uname.setError("Please Add User Name");
                }else if (uemail.getText().toString().isEmpty()){
                    uemail.setError("Please Add email");
                }else if (upass.getText().toString().isEmpty()){
                    upass.setError("Please Add Pass");
                }else if (uconfirmpass.getText().toString().isEmpty()){
                    uconfirmpass.setError("Please Add Confirm");
                }else {
                    CreateAccount(uemail.getText().toString(),upass.getText().toString());
                }

            }
        });
    }
    public void CreateAccount(String email,String pass){
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    AdminRegisterData(uemail.getText().toString(),uname.getText().toString(),"Users");
                }else {

                    Toast.makeText(AddNewEmpActivity.this, "SomeThing Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(AddNewEmpActivity.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void AdminRegisterData(String email,String fullname,String type){

        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        AdminAuth auth=new AdminAuth(fullname,type,email);

        firestore.collection("Admins").document("data").collection(email).add(auth).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "Account created", Toast.LENGTH_SHORT).show();
                firestore.collection("All Users").add(auth).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddNewEmpActivity.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
              progressDialog.dismiss();
            }
        });

    }
}