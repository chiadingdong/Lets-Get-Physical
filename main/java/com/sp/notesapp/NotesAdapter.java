package com.sp.notesapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private final ArrayList<Note> noteList;
    private final Context mContext;

    //constructor
    public NotesAdapter(ArrayList<Note> notesList, Context mContext) {
        this.noteList = notesList;
        this.mContext = mContext;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitle,noteContent,editNote,deleteNote;
        ImageView cardImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.note_title_TV);
            noteContent = itemView.findViewById(R.id.note_content_TV);
            editNote = itemView.findViewById(R.id.editTV);
            deleteNote=itemView.findViewById(R.id.deleteTV);
            cardImageView= itemView.findViewById(R.id.cardImage);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View noteView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card,parent,false);
        ViewHolder viewHolder = new ViewHolder(noteView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //TODO : We will set the data here.


        //position starts from zero. So initially it is zero.
        final Note note =noteList.get(position);
        holder.noteTitle.setText(note.getNoteTitle());
        holder.noteContent.setText(note.getNoteContent());
        Glide.with(mContext).load(noteList.get(position).getImageUri()).into(holder.cardImageView);


        holder.editNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //we will open Edit note activity here.
                Intent editNoteIntent = new Intent(mContext,EditNote.class);

                editNoteIntent.putExtra("noteTitle",note.getNoteTitle());
                editNoteIntent.putExtra("noteContent",note.getNoteContent());
                editNoteIntent.putExtra("noteID",note.getNoteID());

                mContext.startActivity(editNoteIntent);
            }
        });

        holder.deleteNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteNoteDialog(note.getNoteID());
            }
        });

    }

    @Override
    public int getItemCount() {
        //TODO : put the dataset size here.
        return noteList.size();
    }


    private void showDeleteNoteDialog(final String noteID) {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mContext);
        builder.setMessage("Are you sure you want to delete the note?")
                .setPositiveButton("Yes.", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //delete the note here.
                        ((Home)mContext).deletNoteFromFirebase(noteID); //HomeActivity.deleteNoteFromFirebase();

                    }
                })
                .setNegativeButton("No!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
