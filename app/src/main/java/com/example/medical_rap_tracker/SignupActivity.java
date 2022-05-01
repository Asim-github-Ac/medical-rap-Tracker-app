package com.example.medical_rap_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medical_rap_tracker.Model.AdminAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mRef;
    TextView tvAlreadyHaveAccount;
    Button btnSignUp;
    TextInputLayout inputLayoutFullName, inputLayoutUserEmail, inputLayoutPassword, inputLayoutConfrmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();
        dialog = new ProgressDialog(SignupActivity.this);
        dialog.setMessage("Please Wait");
        // dialog.create();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userFullName = inputLayoutFullName.getEditText().getText().toString();
                String userEmailAddress = inputLayoutUserEmail.getEditText().getText().toString().trim();
                String userPassword = inputLayoutPassword.getEditText().getText().toString().trim();
                String userCnfrmPassword = inputLayoutConfrmPassword.getEditText().getText().toString().trim();
                if (!userFullName.isEmpty() && !userEmailAddress.isEmpty() && !userPassword.isEmpty() && !userCnfrmPassword.isEmpty()) {
                    if (userPassword.length() < 6) {
                        inputLayoutPassword.setError("Weak Password");
                    } else {
                        if (userPassword.equals(userCnfrmPassword)) {
                            dialog.show();
                            mAuth.createUserWithEmailAndPassword(userEmailAddress, userPassword)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                String UID = mAuth.getCurrentUser().getUid();
                                               AdminRegisterData(userEmailAddress,userFullName,"Admin");
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();

                                }
                            });

                        } else {
                            inputLayoutConfrmPassword.setError("Not same");
                        }
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        tvAlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    private void initView() {
        tvAlreadyHaveAccount = findViewById(R.id.textView2);
        inputLayoutFullName = findViewById(R.id.textInputLayout4);
        inputLayoutUserEmail = findViewById(R.id.textInputLayout5);
        inputLayoutPassword = findViewById(R.id.textInputLayout6);
        inputLayoutConfrmPassword = findViewById(R.id.textInputLayout7);
        btnSignUp = findViewById(R.id.button2);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("admins");
    }
    public void AdminRegisterData(String email,String fullname,String type){

        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        AdminAuth auth=new AdminAuth(fullname,type,email);
        firestore.collection("Admins").document("data").collection(email).add(auth).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(), "Account created", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignupActivity.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }
}