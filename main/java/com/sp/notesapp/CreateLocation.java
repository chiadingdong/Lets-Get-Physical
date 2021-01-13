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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

public class CreateLocation extends AppCompatActivity {

    private EditText locationTitle, locationMrt, locationDescrip;
    private EditText lat, lon;
    private Button galleryBtn, saveBtn;
    private ImageView displayImage;
    private CreateLocation mContext;

    private static final int GALLERY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_location);

        mContext = CreateLocation.this;
        locationTitle = findViewById(R.id.locationTitle);
        locationMrt = findViewById(R.id.locationMrt);
        locationDescrip = findViewById(R.id.locationDescrip);
        lat = findViewById(R.id.locationLat);
        lon = findViewById(R.id.locationLon);

        displayImage = findViewById(R.id.locationImageUpload);

        galleryBtn = findViewById(R.id.locationGalleryBtn);
        galleryBtn.setOnClickListener(ReadImage);

        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(uploadToFirebase);

    }

    View.OnClickListener uploadToFirebase = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String title = locationTitle.getText().toString();
            String mrt = locationMrt.getText().toString();
            String descrip = locationDescrip.getText().toString();

            double latDouble = Double.parseDouble(lat.getText().toString());
            double longDouble = Double.parseDouble(lon.getText().toString());

            BitmapDrawable drawable = (BitmapDrawable) displayImage.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            String imageStr = bitmapToString(bitmap);

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference rootReference = firebaseDatabase.getReference(); //reference to database-root here.
            DatabaseReference locationReference = rootReference.child("Location");

            DatabaseReference newLocationRef = locationReference.push();

            LocationModel location = new LocationModel(title, descrip , mrt, imageStr, latDouble, longDouble);

            newLocationRef.setValue(location).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(CreateLocation.this, "Note submitted in Firebase.", Toast.LENGTH_SHORT).show();
                        mContext.finish(); //finish this activity.
                    } else {
                        Toast.makeText(CreateLocation.this, "Some error occurred :  " + task.getException(), Toast.LENGTH_SHORT).show();
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