package com.example.ss12dark.album;

import java.text.DateFormat;
import java.util.Date;

public class Photo {//in my app im using only one obgect class for photos and albums because albums got only number and name and we can just ask the photo wich album he belongs

    private int ID; //for database use
    private int albumNum;//album number
    private String name;//name of the photo (discription)
    private String url;//place in the device
    private String alna;//album name
    private String date;//date of the photo

    public Photo(){} //using this for empty initial (- no other constructors in needed)

    //standard "get and set" for each factor------------------so i can edit them or get the data from the in the application code------------------------------------------
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAlna() {
        return alna;
    }

    public void setAlna(String alna) {
        this.alna = alna;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getAlbumNum() {
        return albumNum;
    }

    public void setAlbumNum(int albumNum) {
        this.albumNum = albumNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
