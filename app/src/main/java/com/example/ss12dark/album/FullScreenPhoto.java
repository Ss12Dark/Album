package com.example.ss12dark.album;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import static com.example.ss12dark.album.TakePhoto.rotateBitmap;

public class FullScreenPhoto extends AppCompatActivity {
    ImageView iv;
    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_photo);
        iv = (ImageView) findViewById(R.id.image);
        Intent i = getIntent();
        filePath = i.getStringExtra("imagePath");
        File imgFile = new File(filePath);
        if (imgFile.exists()) {

            Bitmap loadedBitmap = BitmapFactory.decodeFile(filePath);
            iv.setImageBitmap(loadedBitmap);

            ExifInterface exif = null;
            try {
                File pictureFile = new File(filePath);
                exif = new ExifInterface(pictureFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            int orientation = ExifInterface.ORIENTATION_NORMAL;

            if (exif != null)
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    loadedBitmap = rotateBitmap(loadedBitmap, 90);
                    iv.setImageBitmap(loadedBitmap);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    loadedBitmap = rotateBitmap(loadedBitmap, 180);
                    iv.setImageBitmap(loadedBitmap);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    loadedBitmap = rotateBitmap(loadedBitmap, 270);
                    iv.setImageBitmap(loadedBitmap);
                    break;
            }
        }
    }
}
