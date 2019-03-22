package com.example.ss12dark.album;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
    SharedPreferences myPref;
    LinearLayout pageColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        myPref = PreferenceManager.getDefaultSharedPreferences(this);
        backgroundColor();
        back = (Button) findViewById(R.id.back);
        reset = (Button) findViewById(R.id.reset);
        db = new MyDBHandler(this);
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
                builder.setTitle(R.string.deletos_allos);

                final EditText input = new EditText(Setting.this);


                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = myPref.edit();
                        editor.putInt("photoAddedNumberInLine", 0);
                        editor.apply();
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
        SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(this);
        int background =myPref.getInt("pageColor",1);
        switch (background){


//            case 1:{ pageColor.setBackground(getDrawable(R.drawable.albumlobby));break;}
//            case 2:{ pageColor.setBackground(getDrawable(R.drawable.albumlobbyblack));break;}
//            case 3:{ pageColor.setBackground(getDrawable(R.drawable.albumlobbypink));break;}
            case 1:{ pageColor.setBackgroundColor(getColor(R.color.blu));break;}
            case 2:{ pageColor.setBackgroundColor(getColor(R.color.pinki));break;}
            case 3:{ pageColor.setBackgroundColor(getColor(R.color.pur));break;}

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

