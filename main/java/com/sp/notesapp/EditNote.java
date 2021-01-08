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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditNote extends AppCompatActivity {

    private EditText noteTitleET,noteContentET;
    private Button editNoteButton;
    String noteTitle,noteContent,noteID;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        context = this;
        noteTitleET=findViewById(R.id.noteTitle_ET);
        noteContentET=findViewById(R.id.noteContent_ET);


        if(getIntent().getExtras()!=null)
        {
            noteTitle = getIntent().getStringExtra("noteTitle");
            noteContent = getIntent().getStringExtra("noteContent");
            noteID = getIntent().getStringExtra("noteID");

            noteTitleET.setText(noteTitle);
            noteContentET.setText(noteContent);
        }



        editNoteButton = findViewById(R.id.editNote_btn);

        editNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNoteInFirebase();
                ((Activity) context).finish();
            }
        });

    }

    private void editNoteInFirebase() {
        //TODO : put edit note method here.
        //edit the note whose ID is noteID.

        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase firebaseDatabase =  FirebaseDatabase.getInstance();

        DatabaseReference rootReference =firebaseDatabase.getReference(); //root reference

        DatabaseReference notesReference = rootReference.child("Users").child(currentUser.getUid()).child("Notes");

        DatabaseReference particularNoteReference= notesReference.child(noteID);

        particularNoteReference.child("noteTitle").setValue(noteTitleET.getText().toString());

        particularNoteReference.child("noteContent").setValue(noteContentET.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    //note updates successfully.
                    Toast.makeText(EditNote.this, "Note updated successfully!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(EditNote.this, "Some error occurred!", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}
