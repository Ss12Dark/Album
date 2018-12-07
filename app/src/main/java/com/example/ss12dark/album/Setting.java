package com.example.ss12dark.album;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class Setting extends AppCompatActivity {

    Button reset,back;
    MyDBHandler db;
    LinearLayout pageColor;
    SharedPreferences myPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        backgroundColor();

        back = (Button) findViewById(R.id.back);
        reset = (Button) findViewById(R.id.reset);
        db = new MyDBHandler(this);
        setButtonsClicks();
    }

    public void setButtonsClicks(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Setting.this,AlbumsLobby.class);
                startActivity(i);
                finish();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
                builder.setTitle("Are you sure you want to DELETE ALL ALBUMS in the app?");

                final EditText input = new EditText(Setting.this);


                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.clear();
                        db.close();
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
    }

    public void backgroundColor(){
        pageColor = (LinearLayout) findViewById(R.id.pagecolor);
        myPref = PreferenceManager.getDefaultSharedPreferences(this);
        int background =myPref.getInt("pageColor",1);
        switch (background){
            case 1:{ pageColor.setBackground(getDrawable(R.drawable.albumlobby));break;}
            case 2:{ pageColor.setBackground(getDrawable(R.drawable.albumlobbyblack));break;}
            case 3:{ pageColor.setBackground(getDrawable(R.drawable.albumlobbypink));break;}
        }
    }

    public void changeBackground(View v){
        switch (v.getTag().toString()){
            case "1":{SharedPreferences.Editor editor = myPref.edit();
                editor.putInt("pageColor",1);
                editor.apply();break;}
            case "2":{SharedPreferences.Editor editor = myPref.edit();
                editor.putInt("pageColor",2);
                editor.apply();break;}
            case "3":{SharedPreferences.Editor editor = myPref.edit();
                editor.putInt("pageColor",3);
                editor.apply();break;}
        }
        recreate();
    }
}
