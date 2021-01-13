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
        setContentView(R.layout.activity_display_note);

        mContext =this;
        welcomeMessageTV = findViewById(R.id.welcomeMessageTV);
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


    //menu stuff
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
                Intent userAccountIntent = new Intent(DisplayNote.this,UserAccount.class);
                startActivity(userAccountIntent);
                break;
            case R.id.calendarEvent:
                //Create event in calendar
                Calendar cal = Calendar.getInstance();
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setData(CalendarContract.Events.CONTENT_URI);
                intent.putExtra("beginTime", cal.getTimeInMillis());
                intent.putExtra("allDay", true);
                intent.putExtra("rrule", "FREQ=WEEKLY;WKST=SU;BYDAY=TU,TH");
                intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
                intent.putExtra("title", "Workout using Lets Get Physical App!");
                startActivity(intent);
                break;
        }


        return super.onOptionsItemSelected(item);
    }


}