package com.example.ss12dark.album;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

public class FullScreenPhoto extends AppCompatActivity {
ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_photo);
        iv= (ImageView) findViewById(R.id.image);
        Intent i = getIntent();
        String filePath =i.getStringExtra("imagePath");
        File imgFile = new  File(filePath);
        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(filePath);
            iv.setImageBitmap(myBitmap);

        }
    }
}
