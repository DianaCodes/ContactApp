package com.ebookfrenzy.contactapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    private DbHandler db;
    private static final int NEW_CONTACT = 1234;
    private static final int UPDATE_CONTACT = 0000;
    private ArrayAdapter<Contact> adapter = null;
    private ArrayList<Contact> contactList = null;
    private ListView list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DbHandler(this);

        //get data from database and add to list
        contactList = db.GetContacts();

        //setup the adapter and link it to the listView on the layout
        list = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, contactList);
        list.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddContact.class);
                startActivityForResult(i, NEW_CONTACT);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //extract the items from the Contact object and add to the intent
                Contact tmpContact = contactList.get(position);
                Intent i = new Intent(MainActivity.this, ContactDescription.class);
                i.putExtra("ID", tmpContact.getId());
                i.putExtra("NAME", tmpContact.getName());
                i.putExtra("PHONE", tmpContact.getPhoneNumber());
                i.putExtra("ADDRESS", tmpContact.getStreetAddress());
                i.putExtra("EMAIL", tmpContact.getEmailAddress());
                i.putExtra("NOTES", tmpContact.getNotes());

                //start the activity
                startActivityForResult(i, UPDATE_CONTACT);
            }
        });
    }

    //method that is called when an activity returns
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //make sure that the right code fires when activity returns
        if (requestCode == NEW_CONTACT && resultCode == RESULT_OK) {
            Contact newContact = new Contact();
            //parse through the results
            if (data.hasExtra("NAME")) {
                newContact.setName(data.getExtras().getString("NAME"));
            }

            if (data.hasExtra("PHONE")) {
                newContact.setPhoneNumber(data.getExtras().getString("PHONE"));
            }

            if (data.hasExtra("EMAIL")) {
                newContact.setEmailAddress(data.getExtras().getString("EMAIL"));
            }

            if (data.hasExtra("ADDRESS")) {
                newContact.setStreetAddress(data.getExtras().getString("ADDRESS"));
            }

            if (data.hasExtra("NOTES")) {
                newContact.setNotes(data.getExtras().getString("NOTES"));
            }

            //now to save the stuff to the DB first and then the ArrayList
            db.insertContactDetails(newContact);
            contactList.add(newContact);

            //notify the ListView that it has been updated
            adapter.notifyDataSetChanged();
        } else if (requestCode == UPDATE_CONTACT && resultCode == RESULT_OK) {
            Contact updContact = new Contact();

            //parse through the returning intent
            if (data.hasExtra("ID")) {
                updContact.setId(data.getExtras().getInt("ID"));
            }

            if (data.hasExtra("NAME")) {
                updContact.setName(data.getExtras().getString("NAME"));
            }

            if (data.hasExtra("PHONE")) {
                updContact.setPhoneNumber(data.getExtras().getString("PHONE"));
            }

            if (data.hasExtra("EMAIL")) {
                updContact.setEmailAddress(data.getExtras().getString("EMAIL"));
            }

            if (data.hasExtra("ADDRESS")) {
                updContact.setStreetAddress(data.getExtras().getString("ADDRESS"));
            }

            if (data.hasExtra("NOTES")) {
                updContact.setNotes(data.getExtras().getString("NOTES"));
            }

            //push the changes to the DB
            db.UpdateUserDetails(updContact);

            //replace the contents of the item in items
            Contact tmpContact = new Contact();
            for (Contact c : contactList) {
                if (c.getId() == updContact.getId()) {
                    tmpContact = c;
                }
            }

            //remove the old Contact and add the new one
            contactList.remove(tmpContact);
            contactList.add(updContact);

            //notify the ListView that it has been updated
            adapter.notifyDataSetChanged();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}