package com.example.ss12dark.album;

import java.text.DateFormat;
import java.util.Date;

public class Photo {

    private int ID;
    private int albumNum;
    private String name;
    private String url;
    private String alna;
    private String date;

    public Photo(){}

    public  Photo(int albumNum, String url , String name ){
        this.albumNum = albumNum;
        this.name=name;
        this.url = url;
    }

    public  Photo(int ID, int albumNum, String url , String name ){
        this.ID = ID;
        this.albumNum = albumNum;
        this.name=name;
        this.url = url;
    }

    public  Photo(int albumNum, String url ,String alna, String name ){
        this.alna = alna;
        this.albumNum = albumNum;
        this.name=name;
        this.url = url;
    }

    public Photo(int ID, int albumNum, String name, String url, String alna, String date) {
        this.ID = ID;
        this.albumNum = albumNum;
        this.name = name;
        this.url = url;
        this.alna = alna;
        this.date = date;
    }

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
