package com.example.medical_rap_tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medical_rap_tracker.Model.AdminAuth;
import com.example.medical_rap_tracker.Model.AdminModel;
import com.example.medical_rap_tracker.SharedPrefrence.PrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mRef;
    Button btnLogin;
    TextInputLayout inputLayoutEmailAddress, inputLayoutPassword;
    TextView tvCreateNewAccount;
    RadioGroup radioGroup;
    ProgressDialog progressDialog;
    List<AdminAuth> authList=new ArrayList<>();
    String accountType = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activty);
        radioGroup = findViewById(R.id.radioGroup2);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Verification");
        progressDialog.setMessage("Loading.......");

        initView();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButton4:
                        accountType = "Admin";
                        Toast.makeText(getApplicationContext(), "Clicked on Admin", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButton3:
                        accountType = "Users";
                        Toast.makeText(getApplicationContext(), "clicked on user", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Please Select Account Type", Toast.LENGTH_SHORT).show();
                        break;


                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String emailAddress = inputLayoutEmailAddress.getEditText().getText().toString().trim();
                String password = inputLayoutPassword.getEditText().getText().toString().trim();
                if (accountType == "Admin") {
                    if (!emailAddress.isEmpty() && !password.isEmpty()) {

                        mAuth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String UID = mAuth.getCurrentUser().getUid();
                                    System.out.println("u id ---------------"+UID);

                                    GetVerFication(emailAddress);

                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                System.out.println("error is ________________"+e.getMessage());
                            }
                        });
                    } else {
                        if (emailAddress.isEmpty()) {
                            inputLayoutEmailAddress.setError("Enter Email Address");
                        } else if (password.isEmpty()) {
                            inputLayoutPassword.setError("Please Enter Password");
                        } else {

                        }

                    }


                } else if (accountType == "Users") {

                    GetVerFication(emailAddress);
                    PrefManager prefManager=new PrefManager(getApplicationContext());
                    prefManager.setUserEmail(emailAddress);


                }
            }
        });
        tvCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setCancelable(false);
                builder.setTitle("Important");
                builder.setMessage("User's just create Admin account. Then Add number of empolyee's");
                builder.setPositiveButton("Understand", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        startActivity(new Intent(getApplicationContext(), SignupActivity.class));

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    private void initView() {
        inputLayoutEmailAddress = findViewById(R.id.textInputLayout);
        inputLayoutPassword = findViewById(R.id.textInputLayout2);
        btnLogin = findViewById(R.id.button);
        tvCreateNewAccount = findViewById(R.id.textView);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("admins");
    }
    public void GetVerFication(String email){
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        firestore.collection("Admins").document("data").collection(email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()){
                    Toast.makeText(LoginActivity.this, "You Need to Register", Toast.LENGTH_SHORT).show();
                }else {
                    List<AdminAuth> auths=queryDocumentSnapshots.toObjects(AdminAuth.class);
                    authList.addAll(auths);
                    if (authList.get(0).getType().equals("Admin")){
                        if (accountType.equals(authList.get(0).getType())){
                            Intent intent=new Intent(getApplicationContext(),AdminActivity.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                            System.out.println("admin_______________"+authList.get(0).getType());
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Please Select Admin", Toast.LENGTH_SHORT).show();
                        }

                    }else if (authList.get(0).getType().equals("Users")) {
                        if (accountType.equals(authList.get(0).getType())) {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            progressDialog.dismiss();

                            System.out.println("users_______________" + authList.get(0).getType());
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Account Not Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}