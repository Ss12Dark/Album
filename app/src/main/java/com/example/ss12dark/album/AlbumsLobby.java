package com.example.ss12dark.album;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class AlbumsLobby extends AppCompatActivity {
    LinearLayout ll;
    int numberCounter=0;
    MyDBHandler db;
    String m_Text = "";
    LinearLayout pageColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums_lobby);

        backgroundColor();
        db = new MyDBHandler(this);
        ll = (LinearLayout) findViewById(R.id.List);

        final Button menu = (Button) findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(AlbumsLobby.this, menu);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())   {

                            case R.id.add:
                                final Button album = new Button(AlbumsLobby.this);//new album object

                                AlertDialog.Builder builder = new AlertDialog.Builder(AlbumsLobby.this);
                                builder.setTitle("Please enter Album Name:");

                                final EditText input = new EditText(AlbumsLobby.this);
                                input.setInputType(InputType.TYPE_CLASS_TEXT);
                                builder.setView(input);

                                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        m_Text = input.getText().toString();
                                        String albumname = m_Text;
                                        makeButton(album,numberCounter,albumname);
                                        album.setBackground(getDrawable(R.drawable.albumstyle));
                                        ll.addView(album);
                                    }
                                });
                                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                builder.show();
                                break;

                            case R.id.search:
                                Intent i = new Intent(AlbumsLobby.this,Search.class);
                                startActivity(i);
                                break;

                            case R.id.setting:
                                Intent j = new Intent(AlbumsLobby.this,Setting.class);
                                startActivity(j);
                                finish();
                                break;

                            default:
                                break;
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });

        createAlbums();

    }


    public void backgroundColor(){
        pageColor = (LinearLayout) findViewById(R.id.pagecolor);
        SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(this);
        int background =myPref.getInt("pageColor",1);
        switch (background){
            case 1:{ pageColor.setBackgroundColor(getColor(R.color.blu));break;}
            case 2:{ pageColor.setBackgroundColor(getColor(R.color.pinki));break;}
            case 3:{ pageColor.setBackgroundColor(getColor(R.color.pur));break;}
        }
    }

    public void createAlbums(){
        db = new MyDBHandler(this);
        List<Photo> all = db.getAllAlbumList();
        List<Integer> checks = new ArrayList<>();
        int i = 0;

        if (all.size()==i){
//if the list is empty do nothing
        }else{
            while(i<all.size()){
                boolean notGood = true;
                boolean next = true;
                int album = all.get(i).getAlbumNum();
                if(i==0){
//                    checks.add(album);
                    notGood=false;
                }
                //after i take a photo i check if the album of the photo is already exist
                while(notGood){
                    int j = 0;
                    while(j<checks.size()){
                        if(album==checks.get(j)){
                            next =false;//if the album exist so do not make a buttom with his name
                        }
                        j++;
                    }
                    notGood=false;
                }
                if(next) {
                    checks.add(album);
                    String AlNa = all.get(i).getAlna();
                    Button newAlbum = new Button(AlbumsLobby.this);
                    makeButton(newAlbum, album,AlNa);
                    newAlbum.setBackground(getDrawable(R.drawable.albumstyle));
                    ll.addView(newAlbum);
                }
                i++;

            }
        }
        db.close();
    }

    public void deleteAlbum(int albumN){
        db = new MyDBHandler(this);
        db.deleteAlbum(albumN);
        db.close();
    }

    public void makeButton(Button newAlbum, final int albumN,final String name){
        LinearLayout.LayoutParams positionRules = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        newAlbum.setLayoutParams(positionRules);
        newAlbum.setText(name);
        newAlbum.setTextSize(19);
        positionRules.setMargins(10, 10, 10, 10);
        numberCounter=albumN;
        newAlbum.setTag(numberCounter);
        numberCounter++;
        final int place =Integer.parseInt(newAlbum.getTag().toString());
        newAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(AlbumsLobby.this  ,  MainActivity.class);

                next.putExtra("num",place);
                next.putExtra("alna",name);
                startActivity(next);
            }
        });
        newAlbum.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Vibrator vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vi.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    vi.vibrate(500);
                }
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(AlbumsLobby.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(AlbumsLobby.this);
                }
                builder.setTitle("Delete Album")
                        .setMessage("Are you sure you want to delete this album?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAlbum(albumN);
                                AlbumsLobby.this.recreate();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return false;
            }
        });
    }

}
