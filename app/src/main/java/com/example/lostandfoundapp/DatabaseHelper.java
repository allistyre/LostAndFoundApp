package com.example.lostandfoundapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Constants
    public static final String DATABASE_NAME = "LostAndFoundDatabase.db";
    public static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "Items";
    public static final String ID = "id";
    public static final String POST_TYPE = "post_type";
    public static final String NAME = "name";
    public static final String PHONE = "phone";
    public static final String DESCRIPTION = "description";
    public static final String LOCATION = "location";
    public static final String DATE = "date";
    public static final String CATEGORY = "category";
    public static final String IMAGE = "image";
    public static final String TIMESTAMP = "timestamp";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                POST_TYPE + " TEXT NOT NULL, " +
                NAME + " TEXT NOT NULL, " +
                PHONE + " TEXT NOT NULL, " +
                DESCRIPTION + " TEXT NOT NULL, " +
                LOCATION + " TEXT NOT NULL, " +
                DATE + " TEXT NOT NULL, " +
                CATEGORY + " TEXT NOT NULL, " +
                IMAGE + " TEXT NOT NULL, " +
                TIMESTAMP + " TEXT NOT NULL, " +
                LATITUDE + " REAL NOT NULL, " +
                LONGITUDE + " REAL NOT NULL " +
                ")";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // CRUD Operations
    // No update operation as there was no need to update anything in the task.

    public long insertItem(String postType, String name, String phone,
                           String description, String location, String date, String category,
                           String image, String latitude, String longitude)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        // For obtaining the current date and time for the timestamp
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = now.format(formatter);

        ContentValues values = new ContentValues();
        values.put(POST_TYPE, postType);
        values.put(NAME, name);
        values.put(PHONE, phone);
        values.put(DESCRIPTION, description);
        values.put(LOCATION, location);
        values.put(DATE, date);
        values.put(CATEGORY, category);
        values.put(IMAGE, image);
        values.put(TIMESTAMP, formatDateTime);
        values.put(LATITUDE, latitude);
        values.put(LONGITUDE, longitude);

        return database.insert(TABLE_NAME, null, values);
    }

    public Cursor getAllItems()
    {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public Cursor getItemsByCategory(String CATEGORY)
    {
        SQLiteDatabase database = this.getReadableDatabase();

        return database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE category = '" +
                CATEGORY + "'", null);
    }

    public int deleteItem(int id)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(TABLE_NAME, ID + " = ?", new String[]{String.valueOf(id)});
    }

    public Cursor getItemById(int id) {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ID + " = ?",
                new String[]{String.valueOf(id)});
    }

}
