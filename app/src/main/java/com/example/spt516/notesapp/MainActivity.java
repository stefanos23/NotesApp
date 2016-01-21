package com.example.spt516.notesapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected final int ADD_NEW_NOTE_REQUEST = 2;
    protected final int EDIT_NOTE_REQUEST = 3;
    protected List<String> notes;
    protected ListView notesListView;
    protected ArrayAdapter<String> notesListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesListView = (ListView) findViewById(R.id.fruitsListView);
        notes = new ArrayList<String>();
        notesListViewAdapter = new ArrayAdapter<String>(
                this, // Context
                android.R.layout.simple_list_item_1, // Style of the view
                notes // Data
        );
        notesListView.setAdapter(notesListViewAdapter);

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){ @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
            Intent intent = new Intent(MainActivity.this, NoteActivity.class);
            intent.putExtra("name", notes.get(position));
            intent.setAction("edit");
            intent.putExtra("position",position);
            startActivityForResult(intent, EDIT_NOTE_REQUEST);
        }});

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action_menu bar if it is present.
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action_menu bar item clicks here. The action_menu bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.plus_icon: Intent intent = new Intent(this, NoteActivity.class);
                intent.setAction("new");startActivityForResult(intent,ADD_NEW_NOTE_REQUEST); break;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_NEW_NOTE_REQUEST &&
                resultCode == Activity.RESULT_OK) {
            notes.add(data.getStringExtra("note"));
            notesListViewAdapter.notifyDataSetChanged();
        }
        else if (requestCode == EDIT_NOTE_REQUEST &&
                resultCode == Activity.RESULT_OK) {
            notes.set(Integer.parseInt(data.getStringExtra("position")),data.getStringExtra("note"));
            notesListViewAdapter.notifyDataSetChanged();
        }
    }




}
