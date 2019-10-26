package com.ebookfrenzy.contactapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Diana Arita on 10/12/2019.
 */

public class DbHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 4;
    private static final String DB_NAME = "WEEK6_CONTACTS.db";
    private static final String TABLE_NAME= "contacts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE= "phone";
    private static final String KEY_EMAIL= "email";
    private static final String KEY_ADDRESS= "address";

    //creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_NAME + " TEXT,  " + KEY_PHONE + " TEXT, " + KEY_EMAIL + " TEXT, " + KEY_ADDRESS + " TEXT);";

    //Constructor
    public DbHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    
    // **** CRUD (Create, Read, Update, Delete) Operations ***** //

    // Adding new Contact Details
    void insertContactDetails(Contact contact){
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_NAME, contact.getName());
        cValues.put(KEY_PHONE, contact.getPhoneNumber());
        cValues.put(KEY_EMAIL, contact.getEmailAddress());
        cValues.put(KEY_ADDRESS, contact.getStreetAddress());
        // Insert the new row, returning the primary key value of the new row
        db.insert(TABLE_NAME,null, cValues);
        db.close();
    }
    // Get Contact Details for contact list
    public ArrayList<Contact> GetContacts() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Contact> contacts = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount(); i++) {
            Contact tmpContact = new Contact();
            tmpContact.setId(cursor.getInt(0));
            tmpContact.setName(cursor.getString(1));
            tmpContact.setPhoneNumber(cursor.getString(2));
            tmpContact.setEmailAddress(cursor.getString(3));
            tmpContact.setStreetAddress(cursor.getString(4));
            contacts.add(tmpContact);
            cursor.moveToNext();
        }

        db.close();
        return contacts;

    }
    // Delete User Details
    public void DeleteContact(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID+" = ?",new String[]{String.valueOf(id)});
        db.close();
    }
    //remove everything from the table
    public void removeAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
    // Update User Details
    public void UpdateUserDetails(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PHONE, contact.getPhoneNumber());
        values.put(KEY_EMAIL, contact.getEmailAddress());
        values.put(KEY_ADDRESS, contact.getStreetAddress());
        db.update(TABLE_NAME, values, " id=" + contact.getId(), null);
    }
}
