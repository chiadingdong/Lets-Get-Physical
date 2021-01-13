package com.sp.notesapp;

import com.google.firebase.database.Exclude;

public class Note {
    String imageUri;
    String noteTitle, noteContent;

    @Exclude
    String noteID;

    public Note() {
        //this is required for firebase
    }

    public Note(String noteTitle, String noteContent, String imageUri) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.imageUri = imageUri;
    }



    @Exclude
    public String getNoteID() {
        return noteID;
    }

    @Exclude
    public void setNoteID(String noteID) {
        this.noteID = noteID;
    }



    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

}
