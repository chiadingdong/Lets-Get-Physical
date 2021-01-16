package com.sp.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class DisplayNote extends AppCompatActivity {

    private Button createNoteBtn;

    private RecyclerView recyclerView;
    ArrayList<Note> noteArrayList = new ArrayList<>();
    NotesAdapter notesAdapter;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_note);

        mContext =this;
        createNoteBtn = findViewById(R.id.createNoteBtn);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //if want reverse format
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL, true);
        recyclerView.setLayoutManager(linearLayoutManager);


        createNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createNoteIntent = new Intent(DisplayNote.this,CreateNote.class);
                startActivity(createNoteIntent);
            }
        });


        //get the notes from Firebase
        readNotesFromFirebase();
    }


    private void readNotesFromFirebase() {
        //read the notes in firebase database
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference notesReference = firebaseDatabase.getReference().child("Users").child(currentUser.getUid()).child("Notes");

        notesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //for this example, this datasnapshot will contain. datasnapshot : {NoteID1 : {noteTitle :"some title", noteContent:"some content"}, NoteID2 :{noteTitle :"some title", noteContent:"some content"}}
                noteArrayList.clear();
                Note note;

                for(DataSnapshot noteSnapshot : dataSnapshot.getChildren())
                {
                    note = noteSnapshot.getValue(Note.class);
                    note.setNoteID(noteSnapshot.getKey());

                    noteArrayList.add(note);  //add note to the arraylist of Notes.
                }
                //TODO : SetUp Layout.
                notesAdapter = new NotesAdapter(noteArrayList,mContext);
                recyclerView.setAdapter(notesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DisplayNote.this, "Some error occurred.", Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void deletNoteFromFirebase(String noteID) {        //delete the note in Firebase;

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference notesReference = firebaseDatabase.getReference().child("Users").child(currentUser.getUid()).child("Notes");

        DatabaseReference particularNote = notesReference.child(noteID);

        particularNote.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    Toast.makeText(mContext, "Note is deleted, successfully!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(mContext, "Some error occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}