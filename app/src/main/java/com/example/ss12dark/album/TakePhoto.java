package com.example.ss12dark.album;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class TakePhoto extends AppCompatActivity {
//
//    ImageView imageV;
//    EditText des;
//    int REQ_CAMERA_IMAGE = 1;
//    int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1763;
//    Activity a = this;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_take_photo);
//        imageV = (ImageView) findViewById(R.id.photo);
//        des = (EditText) findViewById(R.id.des);
//
//        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (shouldShowRequestPermissionRationale(
//                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                // Explain to the user why we need to read the contacts
//            }
//
//            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//
//            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
//            // app-defined int constant that should be quite unique
//
//        }
//
//        imageV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                startActivityForResult(intent, 0);
//            }
//        });
//
//    }
//
//    protected void onActivityResult(int requestCode, int resultCode,
//                                    Intent resultData) {
//        super.onActivityResult(requestCode, resultCode, resultData);
//
//        if (resultData != null) {
//
//            String[] projection = {MediaStore.Images.Media.DATA};
//            Cursor cursor = managedQuery(
//                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                    projection, null, null, null);
//            int column_index_data = cursor
//                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToLast();
//
//            String imagePath = cursor.getString(column_index_data);
//            Bitmap bitmapImage = BitmapFactory.decodeFile(imagePath);
//            imageV.setImageBitmap(bitmapImage);
//            Toast.makeText(this,imagePath,Toast.LENGTH_SHORT).show();
//        }
//
//    }
//}
ImageView imageV;
    EditText des;
    int REQ_CAMERA_IMAGE = 1;
    int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1763;
    Activity a = this;


    private static final int REQUEST_IMAGE_GALLERY = 1;
    private static final int SELECT_PHOTO = 2;
    Bitmap imageBitmap;
    String fileName = "example.jpg";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_take_photo);
        imageV = (ImageView) findViewById(R.id.photo);
        des = (EditText) findViewById(R.id.des);

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique

        }

        imageV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
//                startActivityForResult(intent, 0);

            }
        });

    }

    public void save (View view)
    {
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    imageBitmap, fileName, "More data if needed");
            Toast.makeText(TakePhoto.this, "photo saved !", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(TakePhoto.this, "Fail to save image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    public void load (View view)
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }


    protected void onActivityResult( int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_IMAGE_GALLERY && resultCode== RESULT_OK)
        {
            imageBitmap = (Bitmap) data.getExtras().get("data");
            imageV.setBackgroundColor(Color.alpha(Color.WHITE));
            imageV.setImageBitmap(imageBitmap);

        }

        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            Uri Selected_Image_Uri = data.getData();
            imageV.setBackgroundColor(Color.alpha(Color.WHITE));
            imageV.setImageURI(Selected_Image_Uri);
        }

    }

}