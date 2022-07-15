package com.moringaschool.myproperty.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;
import java.util.UUID;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.moringaschool.myproperty.R;
import com.moringaschool.myproperty.api.ApiCalls;
import com.moringaschool.myproperty.api.RetrofitClient;
import com.moringaschool.myproperty.databinding.ActivityDefectPostBinding;
import com.moringaschool.myproperty.databinding.ActivityTenantDefectBinding;
import com.moringaschool.myproperty.models.Constants;
import com.moringaschool.myproperty.models.Defect;
import com.moringaschool.myproperty.models.Validator;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TenantDefectActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private final int PICK_IMAGE_REQUEST = 22;
    ActivityTenantDefectBinding mainBind;
    String downloadUri;
    // Uri indicates, where the image will be picked from local phone storage
    private Uri filePath;
    Defect defect;
    FirebaseStorage storage;
    StorageReference storageReference;

    SharedPreferences pref;
    Call<Defect> call;
    ApiCalls calls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBind = ActivityTenantDefectBinding.inflate(getLayoutInflater());
        setContentView(mainBind.getRoot());
        FirebaseApp.initializeApp(this);

        pref = PreferenceManager.getDefaultSharedPreferences(this);


        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        calls = RetrofitClient.getClient();

        mainBind.buildingNameET.getEditText().setText(pref.getString(Constants.PROPERTY_NAME, ""));
        mainBind.houseNumberET.getEditText().setText(pref.getString(Constants.UNIT_NAME, ""));


        mainBind.captureImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if system os is >= marshmallow, request runtime permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //Permission not enabled, request it
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //SHOW POPUP TO REQUEST PERMISSIONS
                        requestPermissions(permission, PERMISSION_CODE);
                    } else { //permission already granted
                        imageOptionPickDialog();
                        mainBind.submitDefectBtn.setEnabled(true);
                    }
                } else {  //system os < marshmallow
                    imageOptionPickDialog();
                    mainBind.submitDefectBtn.setEnabled(true);
                }
            }
        });

        mainBind.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
                mainBind.imageView.setEnabled(false);
            }
        });
        mainBind.submitDefectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate input fields
                if (!Validator.validateName(mainBind.defectDescriptionEditText)) {
                    return;
                } else {
                    uploadImage();
                    mainBind.submitDefectBtn.setEnabled(false);
                }
            }
        });
    }
    private void imageOptionPickDialog(){
        String[]options={"Camera", "Gallery"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Take image from")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (i==0){
                            //camera clicked
                            openCamera();

                        } else  if (i==1){
                            //gallery clicked
                            SelectImage();
                        }
                    }
                })
                .show();
    }
    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        filePath = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Camera Intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }


    //handling permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //this method is called, when user presses Allow or Deny from permission Request Popup
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Permission from popup was granted
                    openCamera();

                } else {
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void SelectImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image from here..."),PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        //called when image was captured from camera
        if (resultCode == RESULT_OK){
            //set image captured to our image view
            mainBind.imageView.setImageURI(filePath);
        }
        // checking request code and result code, if request code is PICK_IMAGE_REQUEST and resultCode is RESULT_OK, then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) { // Get the Uri of data
            filePath = data.getData();
            try {// Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                mainBind.imageView.setImageBitmap(bitmap);
            }
            catch (IOException e) { // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if (filePath != null) {
            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            // Defining the child of storageReference
            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            // adding listeners on upload or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess( UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!uriTask.isSuccessful()) ;
                                    // get the link of image
                                    downloadUri = uriTask.getResult().toString();
                                    // Image uploaded successfully, Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast.makeText(TenantDefectActivity.this,"Image Uploaded!!",Toast.LENGTH_SHORT).show();
                                    saveDefect();
                                    startActivity(new Intent(TenantDefectActivity.this, TenantMainActivity.class));

                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(TenantDefectActivity.this,"Failed " + e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                // Progress Listener for loading percentage on the dialog box
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                }
                            });
        }
    }

    private void saveDefect() {
        String defectDescription = mainBind.defectDescriptionEditText.getEditText().getText().toString().trim();
        String tenant_id = pref.getString(Constants.TENANT_ID, "");
        String manager_name = pref.getString(Constants.DEFECT_MANAGER_NAME, "");

        defect = new Defect(defectDescription, pref.getString(Constants.PROPERTY_NAME, ""), pref.getString(Constants.UNIT_NAME, ""),downloadUri,tenant_id,manager_name);
        defect.setString_uri(downloadUri);
        defect.setManager_name(manager_name);

        call = calls.addDefect(defect);
        call.enqueue(new Callback<Defect>() {
            @Override
            public void onResponse(Call<Defect> call, Response<Defect> response) {
                if (response.isSuccessful()){
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("defects");
                    databaseReference.child(pref.getString(Constants.DEFECT_MANAGER_NAME,"")).child(defect.getDescription()).setValue(defect);
                    Toast.makeText(TenantDefectActivity.this, "Defect submitted successfully", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<Defect> call, Throwable t) {
                String error = t.getMessage();
                Toast.makeText(TenantDefectActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });



    }

}