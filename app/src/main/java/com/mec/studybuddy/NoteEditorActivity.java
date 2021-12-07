package com.mec.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;


public class NoteEditorActivity extends AppCompatActivity {
        int noteId;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_note_editor);
            EditText editText = findViewById(R.id.editText);

            // Fetch data that is passed from MainActivity
            Intent intent = getIntent();

            // Accessing the data using key and value
            noteId = intent.getIntExtra("noteId", -1);
            if (noteId != -1) {
                editText.setText(note.notes.get(noteId));
            } else {

                note.notes.add("");
                noteId = note.notes.size() - 1;
                note.arrayAdapter.notifyDataSetChanged();

            }

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // add your code here
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    note.notes.set(noteId, String.valueOf(charSequence));
                    note.arrayAdapter.notifyDataSetChanged();
                    // Creating Object of SharedPreferences to store data in the phone
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                    HashSet<String> set = new HashSet(note.notes);
                    sharedPreferences.edit().putStringSet("notes", set).apply();
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // add your code here
                }
            });
        }
    }


