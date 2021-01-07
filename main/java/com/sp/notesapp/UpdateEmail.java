package com.sp.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateEmail extends AppCompatActivity {

    private EditText oldEmailET,newEmailET;
    private Button updateEmailBtn;

    private ValidateInput validateInput;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);

        newEmailET = findViewById(R.id.newEmail_ET);
        oldEmailET = findViewById(R.id.oldEmail_ET);
        updateEmailBtn = findViewById(R.id.updateEmail_btn);

        updateEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEmailInFirebase();
            }
        });

        validateInput = new ValidateInput(this);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        context = this;
    }

    private void updateEmailInFirebase()
    {
        final String newEmail = newEmailET.getText().toString();
        String oldEmail = oldEmailET.getText().toString();

        if(validateInput.checkIfEmailIsValid(newEmail))
        {
            if(newEmail.equals(oldEmail))
            {
                Toast.makeText(this, "New email can't be same as old one. Please enter a new email!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                firebaseUser.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(UpdateEmail.this, "Email updated successfully to "+newEmail, Toast.LENGTH_SHORT).show();
                            ((Activity) context).finish();
                        }
                        else
                        {
                            Toast.makeText(UpdateEmail.this, "Some error occured : "+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

}