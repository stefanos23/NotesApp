package com.example.spt516.notesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class NoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Intent asd = getIntent();
        if (asd != null) {
            if ( asd.getAction().equals("edit")) {
                Bundle as = asd.getExtras();
                String temp = as.get("name").toString();
                EditText mEdit;
                mEdit = (EditText) findViewById(R.id.email_address);
                mEdit.setText(temp);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.info_icon:{ Intent intent = new Intent();
                EditText mEdit;
                mEdit   = (EditText)findViewById(R.id.email_address);


                Intent asd = getIntent();
                if (asd != null) {
                    if (asd.getAction().equals("edit")) {
                        Bundle as = asd.getExtras();
                        String temp = as.get("position").toString();
                        intent.putExtra("position",temp);
                    }
                }

                intent.putExtra("note",mEdit.getText().toString());
                setResult(RESULT_OK,intent);finish(); break;}
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
