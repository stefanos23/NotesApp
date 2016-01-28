package com.example.spt516.notesapp;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected final int ADD_NEW_NOTE_REQUEST = 2;
    protected final int EDIT_NOTE_REQUEST = 3;
    protected List<String> notes;
    protected ListView notesListView;
    protected ArrayAdapter<String> notesListViewAdapter;

    protected ServiceConnection connection = null;
    protected WordCountService service = null;


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

        registerForContextMenu(notesListView);

        notesListView.setAdapter(notesListViewAdapter);

        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtra("name", notes.get(position));
                intent.setAction("edit");
                intent.putExtra("position", position);
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });


        connection = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) { service = null; }
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                service = ((WordCountService.LocalBinder) binder).getService();

                int count = service.count(notes.toArray(new String[]{}));
                toast(Integer.toString(count));
            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action_menu bar if it is present.
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        // R.menu.context_menu -> menu/context_menu.xml
        inflater.inflate(R.menu.context_menu, menu);
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
        if (id == R.id.info_icon) {
            Intent intent = new Intent(this, WordCountService.class);
            intent.setAction("stop");
            intent.putExtra("text", notes.toArray(new String[]{}));
            startService(intent);

            bindService(new Intent(this, WordCountService.class), connection, Context.BIND_AUTO_CREATE);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String action = "";
        switch (item.getItemId()) {
            case R.id.action_duplicate: action = "Duplicate";
                AdapterView.AdapterContextMenuInfo info2 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int index2 = info2.position;
                String temp = notes.get(index2);
                notes.add(index2,temp);
                notesListViewAdapter.notifyDataSetChanged();
                break;

            case R.id.action_edit: action = "Edit";
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int index = info.position;
                edit_intent(index);
                break;

            case R.id.action_delete: action = "Delete";
                AdapterView.AdapterContextMenuInfo info3 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int index4 = info3.position;
                notes.remove(index4);
                break;

            case R.id.create_notification: action = "Create Notification";
                AdapterView.AdapterContextMenuInfo info4 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int index5 = info4.position;
                String temp3 = notes.get(index5);
                notification("Your Notes",temp3);
                break;
        }
        // Get the selected fruit using the item's menu info
        // Note: fruitListView.getSelectedItem() will return null here
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String selectedFruit = (String) notesListView.getItemAtPosition((int) info.position);



        return super.onContextItemSelected(item);
    }

    public void edit_intent(int position){
        Intent intent = new Intent(MainActivity.this, NoteActivity.class);
        intent.putExtra("name", notes.get(position));
        intent.setAction("edit");
        intent.putExtra("position",position);
        startActivityForResult(intent, EDIT_NOTE_REQUEST);
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


    private void toast(String sentence){
        Toast toast = Toast.makeText(
                MainActivity.this,
                sentence,
                Toast.LENGTH_LONG ); // Toast.LENGTH_SHORT
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }


    private void notification(String title, String body){

        // Create the notification
        NotificationCompat.Builder builder = new
                NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.info);
        // Get hold of the Android notification manager
        NotificationManager m = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        // Send the notification to the manager
        // Sending a notification with the same id (0) will
        // replace the existing notification in the drawer
        m.notify(0, builder.build());

    }

}
