package com.example.ss12dark.album;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class Search extends AppCompatActivity {

    LinearLayout pageColor,upperPage,bottomPage;
    List<Photo> all;
    MyDBHandler db;
    Button search;
    EditText nameSearched;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        upperPage = (LinearLayout) findViewById(R.id.upperPage);
        bottomPage = (LinearLayout) findViewById(R.id.bottomPage);
        search = (Button) findViewById(R.id.search);
        nameSearched = (EditText) findViewById(R.id.name);

        db = new MyDBHandler(this);

        backgroundColor();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameSearched.getText().toString().equals("")){
                    Toast.makeText(Search.this,"Please enter text :)",Toast.LENGTH_SHORT).show();
                }else{
                    startAlbum(nameSearched.getText().toString());
                }
            }
        });

    }

    private void startAlbum(String title) {
        upperPage.removeAllViews();
        bottomPage.removeAllViews();
        int i = 0;
        all = db.getAllPhotoListByName(title);

        if (all.size()==0){
            Toast.makeText(this,"There is no such photo... sorry",Toast.LENGTH_SHORT).show();
        }else {
            while (i < all.size()) {
                String name = all.get(i).getName();
                String date = all.get(i).getDate();
                String filePath = all.get(i).getUrl();
                int ID = all.get(i).getID();
                createImage(name, filePath, ID, date);

                i++;
            }
        }

    }

    private void createImage(String name, String filePath,int ID,String date){
        final ImageView image = new ImageView(Search.this);
        resizeImage(image,filePath,ID);

        File imgFile = new  File(filePath);
        if(imgFile.exists()){

            Bitmap myBitmap =decodeSampledBitmapFromURL(filePath);
            image.setImageBitmap(myBitmap);

        }

        final TextView text = new TextView(Search.this);
        resizeText(text);
        text.setText(date+"\n"+"\""+name+"\"");

        Search.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                upperPage.addView(image);
                bottomPage.addView(text);
            }
        });

    }

    private  Bitmap decodeSampledBitmapFromURL(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();

        options.inSampleSize = 2;
        return BitmapFactory.decodeFile(filePath, options);
    }

    private void resizeImage(final ImageView sv,final String filePath,final int ID){
        LinearLayout.LayoutParams positionRules = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        sv.setLayoutParams(positionRules);
        sv.getLayoutParams().height=700;
        sv.getLayoutParams().width=575;
        positionRules.setMargins(10, 10, 10, 10);
        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Search.this,FullScreenPhoto.class);
                i.putExtra("imagePath",filePath);
                startActivity(i);
            }
        });
        sv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Search.this);
                builder.setTitle("Are you sure you want to delete this photo?");

                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deletePhoto(ID);
                        recreate();

                    }
                });
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                return false;
            }
        });
    }

    private void resizeText(TextView sv){
        LinearLayout.LayoutParams positionRules = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        sv.setLayoutParams(positionRules);
        sv.getLayoutParams().height=255;
        sv.getLayoutParams().width=575;
        sv.setTextSize(20);
        sv.setTextColor(Color.BLACK);
        positionRules.setMargins(10, 1, 10, 1);
        sv.setPadding(5,5,5,5);
        sv.setBackground(getDrawable(R.drawable.textmain));
    }


    public void backgroundColor(){
        pageColor = (LinearLayout) findViewById(R.id.pagecolor2);
        SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(this);
        int background =myPref.getInt("pageColor",1);
        switch (background){
            case 1:{ pageColor.setBackgroundColor(getColor(R.color.blu));break;}
            case 2:{ pageColor.setBackgroundColor(getColor(R.color.pinki));break;}
            case 3:{ pageColor.setBackgroundColor(getColor(R.color.pur));break;}
        }
    }
}
