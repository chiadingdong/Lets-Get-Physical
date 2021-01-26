package com.sp.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

public class CreateExc extends AppCompatActivity {

    private EditText excTitle, excTimeNCal, excDescrip, videoURL;
    private Button galleryBtn, saveBtn;
    private ImageView displayImage;

    private static final int GALLERY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exc);

        excTitle = findViewById(R.id.excTitle);
        excTimeNCal = findViewById(R.id.excTimeCal);
        excDescrip = findViewById(R.id.excDescrip);
        videoURL = findViewById(R.id.excVideoURl);

        displayImage = findViewById(R.id.excImageUpload);

        galleryBtn = findViewById(R.id.excGalleryBtn);
        galleryBtn.setOnClickListener(ReadImage);

        saveBtn = findViewById(R.id.excSaveBtn);
        saveBtn.setOnClickListener(uploadToFirebase);
    }

    View.OnClickListener uploadToFirebase = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String title = excTitle.getText().toString();
            String timeNCal = excTimeNCal.getText().toString();
            String descrip = excDescrip.getText().toString();
            String vidUrl = videoURL.getText().toString();

            BitmapDrawable drawable = (BitmapDrawable) displayImage.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            String imageStr = bitmapToString(bitmap);

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference rootReference = firebaseDatabase.getReference(); //reference to database-root here.
            DatabaseReference excReference = rootReference.child("Exercises");

            DatabaseReference newExcRef = excReference.push();

            ExcModel exercise = new ExcModel(imageStr, title, timeNCal, descrip, vidUrl);

            newExcRef.setValue(exercise).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(CreateExc.this, "Note submitted in Firebase.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CreateExc.this, "Some error occurred :  " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    };


    View.OnClickListener ReadImage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image"), GALLERY_REQUEST_CODE);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE) {
            Uri selectedImage = data.getData();
            displayImage.setImageURI(selectedImage);
        }
    }

    public String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
}