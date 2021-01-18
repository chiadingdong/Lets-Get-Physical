package com.sp.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private EditText emailET, passwordET;
    private Button loginBtn;
    private TextView signUpText;

    ValidateInput validateInput;
    private String email,password;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        validateInput= new ValidateInput(this);
        emailET = findViewById(R.id.email_ET);
        passwordET = findViewById(R.id.oldPassword_ET);
        loginBtn = findViewById(R.id.login_btn);
        signUpText = findViewById(R.id.signUp_TV);
        loginBtn.setOnClickListener(this);
        signUpText.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        int id =view.getId();

        switch (id)
        {
            case R.id.login_btn:
                handleLoginBtnClick();
                break;

            case R.id.signUp_TV:
                handleSignUpClick();
                break;
        }
    }

    private void handleLoginBtnClick() {

        email = emailET.getText().toString();
        password = passwordET.getText().toString();

        if(validateInput.checkIfEmailIsValid(email) && validateInput.checkIfPasswordIsValid(password)) {
            //Toast.makeText(this, "Valid Inputs", Toast.LENGTH_SHORT).show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        FirebaseUser user=mAuth.getCurrentUser();
                        Toast.makeText(Login.this, "Signed In : "+user.getEmail(), Toast.LENGTH_SHORT).show();

                        Intent userAccountActivity = new Intent(Login.this, Home.class);
                        startActivity(userAccountActivity);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(Login.this, "Incorrect Email or Password.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    private void handleSignUpClick() {
        Toast.makeText(this, "SignUp Now!", Toast.LENGTH_SHORT).show();
        Intent signupIntent = new Intent(Login.this,SignUp.class);
        startActivity(signupIntent);
    }


}