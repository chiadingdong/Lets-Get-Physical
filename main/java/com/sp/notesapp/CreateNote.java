package com.sp.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class CreateNote extends AppCompatActivity {

    private EditText noteTitle,noteContent;
    private Button createNewNoteBtn;
    private CreateNote mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        noteTitle  = findViewById(R.id.noteTitle_ET);
        noteContent  = findViewById(R.id.noteContent_ET);

        createNewNoteBtn = findViewById(R.id.createNote_btn);

        createNewNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putNoteInFirebase();
            }
        });

        mContext = CreateNote.this;
    }
    private void putNoteInFirebase() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference rootReference= firebaseDatabase.getReference(); //reference to database-root here.

        DatabaseReference notesReference = rootReference.child("Users").child(currentUser.getUid()).child("Notes"); // root/Users/{currentUserID}/Notes

        DatabaseReference newNoteReference = notesReference.push();                           // root/Users/{currentUserID}/Notes/someRandomIdGeneratedByFirebase

        //someID  : {noteTitle : "noteTitle", noteContent : "noteContent"} Note class will have these two properties, noteTitle and noteContent.

        Note newNote = new Note(noteTitle.getText().toString(),noteContent.getText().toString());

        newNoteReference.setValue(newNote).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(CreateNote.this, "Note submitted in Firebase.", Toast.LENGTH_SHORT).show();
                    mContext.finish(); //finish this activity.
                }
                else
                {
                    Toast.makeText(CreateNote.this, "Some error occurred :  "+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}