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
    ImageView imageV;
    EditText title;
    int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1763;
    Activity a = this;
    MyDBHandler db;

    private static final int REQUEST_IMAGE_GALLERY = 1;
    private static final int SELECT_PHOTO = 2;
    Bitmap imageBitmap;
    String fileName ;
    int albomNo =0;
    String alna;
    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new MyDBHandler(this);
        Intent thisphoto = getIntent();
        albomNo = thisphoto.getIntExtra("album",666);
        alna = thisphoto.getStringExtra("alna");
        setContentView(R.layout.activity_take_photo);
        imageV = (ImageView) findViewById(R.id.photo);
        title = (EditText) findViewById(R.id.title);

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

    public void save (View view) {
            if(title.getText().toString().equals("")) {
                Toast.makeText(TakePhoto.this, "please fill the title text" , Toast.LENGTH_SHORT).show();

            }else{
                try {
                    fileName = title.getText().toString();
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    File finalFile = new File(getRealPathFromURI(tempUri));
                    filePath = finalFile.toString();
                    Photo photo = new Photo();
                    photo.setName(fileName);
                    photo.setAlbumNum(albomNo);
                    photo.setUrl(filePath);
                    photo.setAlna(alna);
                    db.addPhoto(photo);
                    db.close();
                    finish();
                    Toast.makeText(TakePhoto.this, "photo saved !", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(TakePhoto.this, "Fail to save image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
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
        Uri image;
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


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, fileName, null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }
}