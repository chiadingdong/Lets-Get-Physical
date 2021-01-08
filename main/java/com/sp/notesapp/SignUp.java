package com.sp.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private EditText nameET,emailET, passwordET, passwordAgainET;
    private Button signUpBtn;

    ValidateInput validateInput;
    private String name,email, password, passwordAgain;

    private Context context;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        validateInput = new ValidateInput(this);
        nameET =findViewById(R.id.name_ET);
        emailET = findViewById(R.id.email_ET);
        passwordET = findViewById(R.id.oldPassword_ET);
        passwordAgainET = findViewById(R.id.newPassword_ET);
        context = this;
        signUpBtn = findViewById(R.id.signUp_btn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSignUpBtnClick();
            }
        });

        mAuth = FirebaseAuth.getInstance();

    }
    private void handleSignUpBtnClick() {
        //Fetching the string values
        name = nameET.getText().toString();
        email = emailET.getText().toString();
        password = passwordET.getText().toString();
        passwordAgain = passwordAgainET.getText().toString();

        if (!name.isEmpty()) {
            //name is not empty, signup the user.
            if (validateInput.checkIfEmailIsValid(email) && validateInput.checkIfPasswordIsValid(password)) //validate the input
            {
                if (password.equals(passwordAgain)) // we are checking if both the password entered are same.
                {
                    //signUp the user with this email and password.
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //this is triggered, when the user is created in the firebase auth.
                            if (task.isSuccessful()) {
                                //firebase user object has email, password and other profile info inside this object.
                                FirebaseUser user = mAuth.getCurrentUser();  //this method returns the current firebase user; who is logged in.
                                Toast.makeText(SignUp.this, "SignUp is successful for : " + user.getEmail(), Toast.LENGTH_SHORT).show();

                                saveNameInFirebaseRealtimeDatabase(user);
                            } else {
                                Toast.makeText(SignUp.this, "Error occurred : " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                        Toast.makeText(this, "Passwords don't match. Please enter again!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else //name is empty.
        {
            Toast.makeText(this, "Please enter a name!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveNameInFirebaseRealtimeDatabase(FirebaseUser user) {

        //firebase method here.
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference rootReference = firebaseDatabase.getReference();

        DatabaseReference nameReference = rootReference.child("Users").child(user.getUid()).child("name");

        nameReference.setValue(name);
    }
}