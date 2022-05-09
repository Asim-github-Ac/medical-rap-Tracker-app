package com.example.medical_rap_tracker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.medical_rap_tracker.Model.UserModel_Data;
import com.example.medical_rap_tracker.SharedPrefrence.PrefManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DataUploadActivity extends AppCompatActivity implements LocationListener {
    ImageView clickImage;
    RadioGroup radioGroup_like,group_sample;
    String like_decision;
    String sample_decion;
    Bitmap photo;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude, longitude;
    Button submit;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;
    TextInputLayout textInputLayoutSetLocation;
    TextInputEditText dr_name,cinic_name,special_name,medicne_name,dr_about_name,locationtxt;
    private static final int CAMERA_REQUEST = 1;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_upload);
        clickImage = findViewById(R.id.imageView);
        textInputLayoutSetLocation=findViewById(R.id.cur_location);
        dr_name=findViewById(R.id.dr_name);
        cinic_name=findViewById(R.id.cnic_name);
        special_name=findViewById(R.id.special_name);
        medicne_name=findViewById(R.id.medicine_name);
        dr_about_name=findViewById(R.id.dr_about_name);
        radioGroup_like=findViewById(R.id.radiogroup_like);
        group_sample=findViewById(R.id.radio_group_sample);
        submit=findViewById(R.id.submit_now);
        locationtxt=findViewById(R.id.location);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }
        ActivityCompat.requestPermissions( this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        clickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
                    takePicture();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(DataUploadActivity.this, "Wait a Minute", Toast.LENGTH_SHORT).show();
                System.out.println("clicked____________");
                uploadFile(mImageUri);

            }
        });

        radioGroup_like.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_yes_like:
                        like_decision= "yes";
                        Toast.makeText(DataUploadActivity.this, "Yes", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.no_radia_like:
                        Toast.makeText(DataUploadActivity.this, "No", Toast.LENGTH_SHORT).show();
                        like_decision= "No";
                        break;

                }
            }
        });
        group_sample.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_no_sample:
                        sample_decion="No";
                        Toast.makeText(DataUploadActivity.this, "No", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radio_yes_sample:
                        sample_decion= "Yes";
                        Toast.makeText(DataUploadActivity.this, "Yes", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void takePicture() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select File"), CAMERA_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CAMERA_REQUEST && resultCode==RESULT_OK) {
//            Bundle extras = data.getExtras();
//            photo = (Bitmap) extras.get("data");
         //   takepic.setImageBitmap(imageBitmap);
// Actually this uri is null, im confuse in this

             mImageUri = data.getData();
            clickImage.setImageURI(mImageUri);
        }
    }

    public void SendData(String email,String url){
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        UserModel_Data userModel_data=new UserModel_Data(dr_name.getText().toString().trim(),cinic_name.getText().toString().trim(),special_name.getText().toString(),medicne_name.getText().toString().trim(),dr_about_name.getText().toString().trim(),textInputLayoutSetLocation.getEditText().toString(),url,like_decision,sample_decion);
        firestore.collection("User Data").document("data").collection(email).add(userModel_data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    public void uploadFile(Uri imgurl) {

        mStorageRef = FirebaseStorage.getInstance().getReference("Candidate");

        if (mImageUri != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            final StorageReference ref =
                    mStorageRef.child("Products" + System.currentTimeMillis() + "." + getFileExtension(mImageUri));


            // adding listeners on upload
            // or failure of image
            ref.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Image uploaded successfully
                    // Dismiss dialog
                    progressDialog.dismiss();

                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            PrefManager prefManager=new PrefManager(getApplicationContext());
                            SendData(prefManager.getUserEmail(),uri.toString());
                        }
                    });
                }
            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(DataUploadActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        } else {

            Toast.makeText(this, "Url is Null", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        double lat = location.getLatitude();
        double longi = location.getLongitude();
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        locationtxt.setText(address);
        locationtxt.setEnabled(false);
    }
    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(DataUploadActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                DataUploadActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                DataUploadActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            getLocations();
        }
    }
    public  void getLocations() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }
}