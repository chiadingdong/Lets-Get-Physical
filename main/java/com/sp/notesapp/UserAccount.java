package com.sp.notesapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserAccount extends AppCompatActivity implements View.OnClickListener {

    private TextView welcomeMessageTV;
    private Button updatePasswordBtn, updateEmailBtn;

    FirebaseAuth mAuth;
    FirebaseUser user;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        context = this;

        welcomeMessageTV = findViewById(R.id.welcomeMessageTV);

        welcomeMessageTV.setText("Email Address: " + user.getEmail());

        updatePasswordBtn = findViewById(R.id.update_password_btn);
        updatePasswordBtn.setOnClickListener(this);

        updateEmailBtn = findViewById(R.id.update_email_btn);
        updateEmailBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {

            case R.id.update_password_btn:
                showUpdatePasswordActivity();
                break;

            case R.id.update_email_btn:
                showUpdateEmailActivity();
                break;

        }
    }
    /*
    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to logout?").setPositiveButton("No.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
            }
        }).setNegativeButton("Yes.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                mAuth.signOut();
                ((Activity) context).finish();
                Intent loginActivity = new Intent(UserAccount.this, Login.class);
                startActivity(loginActivity);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    } */

    private void showUpdatePasswordActivity() {
        Toast.makeText(context, "Update Password here!", Toast.LENGTH_SHORT).show();

        Intent updatePasswordActivity = new Intent(UserAccount.this, UpdatePassword.class);
        startActivity(updatePasswordActivity);
    }

    private void showUpdateEmailActivity() {
        Toast.makeText(context, "Update Email here!", Toast.LENGTH_SHORT).show();

        Intent updateEmailActivity = new Intent(UserAccount.this, UpdateEmail.class);
        startActivity(updateEmailActivity);
    }
}