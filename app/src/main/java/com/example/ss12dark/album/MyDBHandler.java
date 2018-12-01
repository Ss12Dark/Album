package com.example.ss12dark.album;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;


//this class handles the all database tasks and extends the SQLiteOpenHelper (a build class)
//in this class we must to implement the methods "onCreate" and "onUpgrade" from the SQLiteOpenHelper class
public class MyDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "photoLab";

    public static final String TABLE_PHOTOSS = "Photos";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "photoName";
    public static final String KEY_URL = "photoURL";
    public static final String KEY_WATCH = "albumNum";
    public static final String KEY_ALNA = "albumName";

    //We need to pass database information along to superclass because the super class doesn't have any default constructor

    public MyDBHandler(Context contex) {
        super(contex, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //in the first time that we run this app we want to create our table in order to store data
    @Override
    public void onCreate(SQLiteDatabase db) {


        String query = "CREATE TABLE " + TABLE_PHOTOSS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_URL + " TEXT, "
                + KEY_ALNA + " TEXT, "
                + KEY_WATCH + " INTEGER"+")";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOSS);

        // Create tables again
        onCreate(db);
    }


    public void clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PHOTOSS,null,null);
        db.execSQL("delete from "+ TABLE_PHOTOSS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOSS);

        onCreate(db);
    }


    public void addPhoto(Photo photo){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_WATCH, photo.getAlbumNum());
        values.put(KEY_ALNA, photo.getAlna());
        values.put(KEY_URL, photo.getUrl());
        values.put(KEY_NAME, photo.getName());

        db.insert(TABLE_PHOTOSS, null, values);
        db.close();
    }


    public boolean deleteAlbum(int album) {

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_PHOTOSS, KEY_WATCH + "=" + album, null) > 0;

    }

    public boolean deletePhoto(int ID) {

        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_PHOTOSS, KEY_ID + "=" + ID, null) > 0;

    }

    public List<Photo> getAllPhotoList(int album) {

        List<Photo> PhotoList = new ArrayList<Photo>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PHOTOSS + " WHERE "+KEY_WATCH+" = "+ album;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Photo photo = new Photo();
                photo.setID(Integer.parseInt(cursor.getString(0)));
                photo.setName(cursor.getString(1));
                photo.setUrl(cursor.getString(2));
                photo.setAlna(cursor.getString(3));
                photo.setAlbumNum(Integer.parseInt(cursor.getString(4)));

                // Adding contact to list
                PhotoList.add(photo);

            } while (cursor.moveToNext());
        }

        // return contact list
        return PhotoList;
    }

    public List<Photo> getAllAlbumList() {

        List<Photo> PhotoList = new ArrayList<Photo>();
//TODO: make it better whitout taking all the photos and just the album numbers
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PHOTOSS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                Photo photo = new Photo();
                photo.setID(Integer.parseInt(cursor.getString(0)));
                photo.setName(cursor.getString(1));
                photo.setUrl(cursor.getString(2));
                photo.setAlna(cursor.getString(3));
                photo.setAlbumNum(Integer.parseInt(cursor.getString(4)));

                // Adding contact to list

                    PhotoList.add(photo);



            } while (cursor.moveToNext());
        }

        // return contact list
        return PhotoList;
    }

}