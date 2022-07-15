package com.moringaschool.myproperty.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moringaschool.myproperty.databinding.ActivityDefectPostBinding;

import java.util.UUID;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.moringaschool.myproperty.models.Defect;

public class DefectPostActivity extends AppCompatActivity {
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private final int PICK_IMAGE_REQUEST = 22;
    ActivityDefectPostBinding mainBind;
    Uri image_uri;
    String downloadUri;

    Defect defect;
    FirebaseStorage storage;
    StorageReference storageReference;
    AutoCompleteTextView buildingNameET;
    AutoCompleteTextView tenantNameET;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBind = ActivityDefectPostBinding.inflate(getLayoutInflater());
        setContentView(mainBind.getRoot());
        FirebaseApp.initializeApp(this);


        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        //button click
        mainBind.captureImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if system os is >= marshmallow, requestruntime permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //Permission not enabled, request it
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //SHOW POPUP TO REQUEST PERMISSIONS
                        requestPermissions(permission, PERMISSION_CODE);
                    } else {
                        //permission already granted
                        openCamera();
//                        SelectImage();
                    }
                } else {
                    //system os < marshmallow
                    openCamera();
//                    SelectImage();
                }
            }
        });

        mainBind.submitDefectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate input fields
                if (mainBind.buildingNameET.getText().toString().isEmpty() || mainBind.houseNumberET.getText().toString().isEmpty()
                        || mainBind.buildingNameET.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please insert all input fields", Toast.LENGTH_SHORT).show();
                } else {
                    saveDefect();
                }
            }
        });
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri= getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Camera Intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

//    // Select Image method
//    private void SelectImage()
//    {
//        // Defining Implicit Intent to mobile gallery
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(
//                Intent.createChooser(
//                        intent,
//                        "Select Image from here..."),
//                PICK_IMAGE_REQUEST);
//    }


//    // Override onActivityResult method
//    @Override
//    protected void onActivityResult(int requestCode,int resultCode,Intent data){
//        super.onActivityResult(requestCode,resultCode,data);
//        // checking request code and result code
//        // if request code is PICK_IMAGE_REQUEST and
//        // resultCode is RESULT_OK
//        // then set image in the image view
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            // Get the Uri of data
//            image_uri = data.getData();
//            try {
//                // Setting image on image view using Bitmap
//                Bitmap bitmap = MediaStore
//                        .Images
//                        .Media
//                        .getBitmap(
//                                getContentResolver(),
//                                image_uri);
//                mainBind.imageView.setImageBitmap(bitmap);
//            }
//            catch (IOException e) {
//                // Log the exception
//                e.printStackTrace();
//            }
//        }
//    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //called when image was captured from camera
        if (resultCode == RESULT_OK){
            //set image captured to our image view
            mainBind.imageView.setImageURI(image_uri);
        }
    }
    private void uploadImage() {
        if (image_uri != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            // adding listeners on upload
            // or failure of image
            ref.putFile(image_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!uriTask.isSuccessful()) ;
                                    // get the link of image
                                    downloadUri = uriTask.getResult().toString();
//                                    Toast
//                                            .makeText(DefectPostActivity.this,
//                                                    downloadUri,
//                                                    Toast.LENGTH_SHORT)
//                                            .show();

                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(DefectPostActivity.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(DefectPostActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
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
        }
    }


    private void saveDefect() {
        String defectDescription = mainBind.defectDescriptionEditText.getText().toString().trim();
        String buildingName = mainBind.buildingNameET.getText().toString().trim();
        String houseNumber = mainBind.houseNumberET.getText().toString().trim();

        uploadImage();
//        defect = new Defect(defectDescription, buildingName, houseNumber,downloadUri);
//        defect.setStingUri(downloadUri);
//
//        Toast
//                .makeText(DefectPostActivity.this,
//                        downloadUri,
//                        Toast.LENGTH_SHORT)
//                .show();
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference databaseReference = firebaseDatabase.getInstance().getReference("defects");
//        databaseReference.setValue(defect);



    }

}