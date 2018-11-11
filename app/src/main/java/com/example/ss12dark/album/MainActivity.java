package com.example.ss12dark.album;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
TextView numberOfAlbum;
Button newMe,refresh;
MyDBHandler db;
List <Photo> all;
LinearLayout upperPage,bottomPage;

    boolean isImageFitToScreen = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        upperPage = (LinearLayout) findViewById(R.id.upperPage);
        bottomPage = (LinearLayout) findViewById(R.id.bottomPage);
        numberOfAlbum = (TextView) findViewById(R.id.numberofplace);
        Intent lobby = getIntent();
        final int num = lobby.getIntExtra("num",666);
        final String alna = lobby.getStringExtra("alna");
        numberOfAlbum.setText(alna);

        newMe = (Button) findViewById(R.id.newM);
        refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });
        newMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photo = new Intent(MainActivity.this,TakePhoto.class);
                photo.putExtra("album",num);
                photo.putExtra("alna",alna);
                startActivity(photo);
            }
        });

        db = new MyDBHandler(this);
        all = db.getAllPhotoList(num);
        int i = 0;
        if (all.size()==i){
            Toast.makeText(this,"this album is empty",Toast.LENGTH_SHORT).show();
        }else{
            while(i<all.size()){
                int album = all.get(i).getAlbumNum();
                if(album==num){
                    String name = all.get(i).getName();
                    String filePath = all.get(i).getUrl();
                    createImage(name,filePath);
                }
                i++;
            }
        }
        db.close();
    }

    public void createImage(String name, String filePath){
        ImageView image = new ImageView(this);
        resizeImage(image,filePath);

        File imgFile = new  File(filePath);
        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(filePath);
            image.setImageBitmap(myBitmap);

        }
        upperPage.addView(image);

        TextView text = new TextView(this);
        resizeText(text);
        text.setText("\""+name+"\"");

        bottomPage.addView(text);
    }

    public void resizeImage(final ImageView sv,final String filePath){
        LinearLayout.LayoutParams positionRules = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        sv.setLayoutParams(positionRules);
        sv.getLayoutParams().height=800;
        sv.getLayoutParams().width=575;
        positionRules.setMargins(10, 10, 10, 10);
        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,FullScreenPhoto.class);
                i.putExtra("imagePath",filePath);
                startActivity(i);
            }
        });
    }

    public void resizeText(TextView sv){
        LinearLayout.LayoutParams positionRules = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        sv.setLayoutParams(positionRules);
        sv.getLayoutParams().height=200;
        sv.getLayoutParams().width=575;
        sv.setTextSize(20);
        sv.setTextColor(Color.BLACK);
        positionRules.setMargins(10, 1, 10, 1);
    }
}
