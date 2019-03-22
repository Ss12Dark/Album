package com.example.ss12dark.album;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;


//this class handles the all database tasks and extends the SQLiteOpenHelper (a build class)
//in this class we must to implement the methods "onCreate" and "onUpgrade" from the SQLiteOpenHelper class - - - but not have to use onUpgrade (myabe for updates or other features)
public class MyDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1; //using standard version of sqlite
//we will use final strings to set the sqlite table and to help in future commands here
    private static final String DATABASE_NAME = "photoLab";//name of the whole db

    public static final String TABLE_PHOTOSS = "Photos";//the photo table
//---------------------now i will add the photo class factors ---------------------------
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "photoName";
    public static final String KEY_URL = "photoURL";
    public static final String KEY_WATCH = "albumNum";
    public static final String KEY_ALNA = "albumName";
    public static final String KEY_DATE = "date";

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
                + KEY_WATCH + " INTEGER, "
                + KEY_DATE + " TEXT"+")";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {//"we must to implement the method "onUpgrade" from the SQLiteOpenHelper class"

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOSS);

        // Create tables again
        onCreate(db);
    }


    public void clear() {//i use this method to delete all the albums and photos in the app at the setting menu
        SQLiteDatabase db = this.getWritableDatabase();
        //-----------using sql quarry to execute commands----------
        db.delete(TABLE_PHOTOSS,null,null);
        db.execSQL("delete from "+ TABLE_PHOTOSS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOSS);

        onCreate(db);
    }


    public void addPhoto(Photo photo){//this is the add photo part in the database
        SQLiteDatabase db = this.getWritableDatabase();//we are getting the option to write into the db

        ContentValues values = new ContentValues();//we set values variable to easy add all the photo factors to the table in wanted order
//geting the values for the photo that was sended and add them to db values
        values.put(KEY_WATCH, photo.getAlbumNum());
        values.put(KEY_ALNA, photo.getAlna());
        values.put(KEY_URL, photo.getUrl());
        values.put(KEY_NAME, photo.getName());
        values.put(KEY_DATE, photo.getDate());

        db.insert(TABLE_PHOTOSS, null, values);//insert all of them
        db.close();//close the db
    }


    public void deleteAlbum(int album) {//method for deleting a specific album and all the photo he have inside
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PHOTOSS, KEY_WATCH + "=" + album, null);
    }

    public void deletePhoto(int ID) {//method for deleting a single photo
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PHOTOSS, KEY_ID + "=" + ID, null);
    }

    public List<Photo> getAllPhotoList(int album) {//im using this method to get all the photo in the same album to display in main activity

        List<Photo> PhotoList = new ArrayList<Photo>();//making alist to put all the photos inside anddd i will return in back after done
        // Select Query with the requested album number
        String selectQuery = "SELECT  * FROM " + TABLE_PHOTOSS + " WHERE "+KEY_WATCH+" = "+ album;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//mouse like that going through rows and column

        // looping through all columns and adding to list
        if (cursor.moveToFirst()) {
            do {

                Photo photo = new Photo();//i make a new object of photo and put data values in it
                photo.setID(Integer.parseInt(cursor.getString(0)));
                photo.setName(cursor.getString(1));
                photo.setUrl(cursor.getString(2));
                photo.setAlna(cursor.getString(3));
                photo.setAlbumNum(Integer.parseInt(cursor.getString(4)));
                photo.setDate(cursor.getString(5));

                // Adding photo to list
                PhotoList.add(photo);

            } while (cursor.moveToNext());//got to the next row
        }

        // return photo list
        return PhotoList;
    }

    public List<Photo> getAllAlbumList() {//using this method for the lobby album menu and gets all the photos

        List<Photo> PhotoList = new ArrayList<Photo>();//making alist to put all the photos inside anddd i will return in back after done
//TODO: make it better whitout taking all the photos and just the album numbers
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PHOTOSS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all columns and adding to list
        if (cursor.moveToFirst()) {
            do {

                Photo photo = new Photo();
                photo.setID(Integer.parseInt(cursor.getString(0)));
                photo.setName(cursor.getString(1));
                photo.setUrl(cursor.getString(2));
                photo.setAlna(cursor.getString(3));
                photo.setAlbumNum(Integer.parseInt(cursor.getString(4)));

                // Adding photo to list

                    PhotoList.add(photo);



            } while (cursor.moveToNext());//got to the next row
        }

        // return photo list
        return PhotoList;
    }

    public List<Photo> getAllPhotoListByName(String name) {//im using this method for the search feature

        List<Photo> PhotoList = new ArrayList<Photo>();//making alist to put all the photos inside anddd i will return in back after done

        // Select a specific photos from the table by searching the name
        String selectQuery = "SELECT  * FROM " + TABLE_PHOTOSS + " WHERE "+KEY_NAME+" like '%"+ name+"%'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all columns and adding to list
        if (cursor.moveToFirst()) {
            do {

                Photo photo = new Photo();
                photo.setID(Integer.parseInt(cursor.getString(0)));
                photo.setName(cursor.getString(1));
                photo.setUrl(cursor.getString(2));
                photo.setAlna(cursor.getString(3));
                photo.setAlbumNum(Integer.parseInt(cursor.getString(4)));
                photo.setDate(cursor.getString(5));

                // Adding photo to list
                PhotoList.add(photo);

            } while (cursor.moveToNext());//go to the next row
        }

        // return photo list list
        return PhotoList;
    }

}