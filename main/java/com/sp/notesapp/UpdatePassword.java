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

public class UpdatePassword extends AppCompatActivity {

    private EditText passwordET, passwordAgainET;
    private Button updatePasswordBtn;

    private ValidateInput validateInput;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        passwordET = findViewById(R.id.password_ET);
        passwordAgainET = findViewById(R.id.password_Again);
        updatePasswordBtn = findViewById(R.id.update_password_Btn);

        validateInput = new ValidateInput(this);

        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePasswordInFirebase();
            }
        });

        mAuth= FirebaseAuth.getInstance();
        user= mAuth.getCurrentUser();
        context = this;
    }

    private void updatePasswordInFirebase() {

        String password = passwordET.getText().toString();
        final String passwordAgain = passwordAgainET.getText().toString();

        if(validateInput.checkIfPasswordIsValid(passwordAgain))
        {
            if(password.equals(passwordAgain))
            {
                //password are same.
                Toast.makeText(this, "Updating the new password...", Toast.LENGTH_SHORT).show();

                user.updatePassword(passwordAgain).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(UpdatePassword.this, "Password updated to : "+passwordAgain, Toast.LENGTH_SHORT).show();
                            ((Activity) context).finish();
                        }
                        else
                        {
                            Toast.makeText(UpdatePassword.this, "Error occurred : "+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else
            {
                Toast.makeText(this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}