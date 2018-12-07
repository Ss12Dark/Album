package com.example.ss12dark.album;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class TakePhoto extends AppCompatActivity {
    ImageView imageV;
    EditText title;
    int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1763;
    Activity a = this;
    MyDBHandler db;

    int loadOrPicture=0;
    //load = 1; picture = 2;
    private static final int OPEN_CAMERA = 1;
    private static final int SELECT_PHOTO = 2;
    Bitmap imageBitmap;
    Uri Selected_Image_Uri;
    String fileName ;
    int albomNo =0;
    String alna;
    String filePath;
    LinearLayout pageColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        backgroundColor();
        db = new MyDBHandler(this);
        Intent thisphoto = getIntent();
        albomNo = thisphoto.getIntExtra("album",666); //album = album number
        alna = thisphoto.getStringExtra("alna"); //alna = album name
        imageV = (ImageView) findViewById(R.id.photo);
        title = (EditText) findViewById(R.id.title);

        checkPermission();

        imageV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadOrPicture = 2;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent, OPEN_CAMERA);

            }
        });

    }

    public void checkPermission(){
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);


        }
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

    public void save (View view) {
            if(title.getText().toString().equals("")) {
                Toast.makeText(TakePhoto.this, "Please fill the title text" , Toast.LENGTH_SHORT).show();

            }else{
                if(title.getText().toString().length()>50){
                    Toast.makeText(TakePhoto.this, "Text is too long" , Toast.LENGTH_SHORT).show();
                }else {
                    setAndSavePhoto();
                }
            }

    }

    public void setAndSavePhoto(){
        try {
            fileName = title.getText().toString();


            File finalFile;
            // CALL THIS METHOD TO GET THE ACTUAL PATH
            if (loadOrPicture == 2) {
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);
                finalFile = new File(getRealPathFromURI(tempUri));
            }else{
                int sdkVersion = Build.VERSION.SDK_INT;
                if(sdkVersion<24){
                    filePath = Selected_Image_Uri+"";

                }else{
                    finalFile = new File(getRealPathFromURI(Selected_Image_Uri));
                    filePath = finalFile.toString();
                }

            }


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

    public void load (View view){
        loadOrPicture = 1;
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    protected void onActivityResult( int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==OPEN_CAMERA && resultCode== RESULT_OK)
        {
            imageBitmap = (Bitmap) data.getExtras().get("data");
            imageV.setBackgroundColor(Color.alpha(Color.WHITE));
            imageV.setImageBitmap(imageBitmap);

        }

        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            Selected_Image_Uri = data.getData();
//            imageV.setBackgroundColor(Color.alpha(Color.WHITE));
//            imageV.setImageURI(Selected_Image_Uri);


            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = this.getContentResolver().query(Selected_Image_Uri, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap loadedBitmap = BitmapFactory.decodeFile(picturePath);

            ExifInterface exif = null;
            try {
                File pictureFile = new File(picturePath);
                exif = new ExifInterface(pictureFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            int orientation = ExifInterface.ORIENTATION_NORMAL;

            if (exif != null)
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    imageV.setBackgroundColor(Color.alpha(Color.WHITE));
                    imageBitmap = rotateBitmap(loadedBitmap, 90);
                    imageV.setImageBitmap(imageBitmap);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    imageV.setBackgroundColor(Color.alpha(Color.WHITE));
                    imageBitmap = rotateBitmap(loadedBitmap, 180);
                    imageV.setImageBitmap(imageBitmap);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    imageV.setBackgroundColor(Color.alpha(Color.WHITE));
                    imageBitmap = rotateBitmap(loadedBitmap, 270);
                    imageV.setImageBitmap(imageBitmap);
                    break;
            }
        }

    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
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