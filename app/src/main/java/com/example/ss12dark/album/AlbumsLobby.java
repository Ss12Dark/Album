package com.example.ss12dark.album;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class AlbumsLobby extends AppCompatActivity {
    LinearLayout ll;
    Button add,setting;
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
        final Button search = (Button) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AlbumsLobby.this,Search.class);
                startActivity(i);
            }
        });

        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Button album = new Button(AlbumsLobby.this);

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



            }
        });
        setting = (Button) findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AlbumsLobby.this,Setting.class);
                startActivity(i);
                finish();
            }
        });
        createAlbums();

    }

    public void backgroundColor(){
        pageColor = (LinearLayout) findViewById(R.id.pagecolor);
        SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(this);
        int background =myPref.getInt("pageColor",1);
        switch (background){
            case 1:{ pageColor.setBackground(getDrawable(R.drawable.albumlobby));break;}
            case 2:{ pageColor.setBackground(getDrawable(R.drawable.albumlobbyblack));break;}
            case 3:{ pageColor.setBackground(getDrawable(R.drawable.albumlobbypink));break;}
        }
    }

    public void createAlbums(){
        db = new MyDBHandler(this);
        List<Photo> all = db.getAllAlbumList();
        List<Integer> checks = new ArrayList<>();
        int i = 0;

        if (all.size()==i){

        }else{
            while(i<all.size()){
                boolean notGood = true;
                boolean next = true;
                int album = all.get(i).getAlbumNum();
                if(i==0){
//                    checks.add(album);
                    notGood=false;
                }
                while(notGood){
                    int j = 0;
                    while(j<checks.size()){
                        if(album==checks.get(j)){
                            next =false;
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
