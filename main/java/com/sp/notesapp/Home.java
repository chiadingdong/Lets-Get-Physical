package com.sp.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class Home extends AppCompatActivity {
    //change this to like NoteActivity/NoteDisplay
    private TextView welcomeMessageTV;
    private Button createNoteBtn;

    private RecyclerView recyclerView;
    ArrayList<Note> noteArrayList = new ArrayList<>();
    NotesAdapter notesAdapter;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext =this;
        welcomeMessageTV = findViewById(R.id.welcomeMessageTV);
        createNoteBtn = findViewById(R.id.createNoteBtn);

        //Display out user's name
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootReference = firebaseDatabase.getReference(); //app root in firebase database.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference nameReference = rootReference.child("Users").child(currentUser.getUid()).child("name");

        nameReference.addListenerForSingleValueEvent(new ValueEventListener() { //this will get triggered at least once.
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //dataSnapshot will have = {name: "Aman"} key =name, value = Aman.
                welcomeMessageTV.setText("Welcome, "+dataSnapshot.getValue().toString()+"!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        createNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createNoteIntent = new Intent(Home.this,CreateNote.class);
                startActivity(createNoteIntent);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

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

                    //Toast.makeText(HomeActivity.this, "note : title : "+note.getNoteTitle() + " content "+note.getNoteContent() + " note key/ID "+note.getNoteID(), Toast.LENGTH_SHORT).show();
                    // Log.i("mynote", "note : title : "+note.getNoteTitle() + " content "+note.getNoteContent() + " note key/ID "+note.getNoteID());

                    //add note the arraylist of Notes.
                    noteArrayList.add(note);
                }

                //TODO : SetUp Layout.
                notesAdapter = new NotesAdapter(noteArrayList,mContext);
                recyclerView.setAdapter(notesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Home.this, "Some error occurred.", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void deletNoteFromFirebase(String noteID) {
        //delete the note in Firebase;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.userAccount:
                Intent userAccountIntent = new Intent(Home.this,UserAccount.class);
                startActivity(userAccountIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}