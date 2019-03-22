package com.example.ss12dark.album;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SharedElementCallback;
import android.content.ContentValues;
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
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class TakePhoto extends AppCompatActivity {
    ImageView imageV;
    EditText title;
    int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1763;
    Activity a = this;
    MyDBHandler db;
    Date date;

    int loadOrPicture=0;
    //load = 1; picture = 2;
    private static final int CAMERA_ON = 2;
    private static final int SELECT_PHOTO = 1;
    Bitmap imageBitmap;
    Uri Selected_Image_Uri;
    String fileName ;
    int albomNo =0;
    String alna;
    String filePath;
    LinearLayout pageColor;
    ContentValues values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        pageColor = (LinearLayout) findViewById(R.id.pagecolor);
        backgroundColor();
        db = new MyDBHandler(this);
        Intent thisphoto = getIntent();
        albomNo = thisphoto.getIntExtra("album",666); //album = album number
        alna = thisphoto.getStringExtra("alna"); //alna = album name
        imageV = (ImageView) findViewById(R.id.photo);
        title = (EditText) findViewById(R.id.title);

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);


        }

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            }

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1788);


        }

        imageV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadOrPicture = 2;
                values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                Selected_Image_Uri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Selected_Image_Uri);
                startActivityForResult(intent, CAMERA_ON);

            }
        });

    }


    public void backgroundColor(){
        pageColor = (LinearLayout) findViewById(R.id.pagecolor);
        SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(this);
        int background =myPref.getInt("pageColor",1);
        switch (background){
            case 1:{ pageColor.setBackgroundColor(getColor(R.color.blu));break;}
            case 2:{ pageColor.setBackgroundColor(getColor(R.color.pinki));break;}
            case 3:{ pageColor.setBackgroundColor(getColor(R.color.pur));break;}
        }
    }

    public void save (View view) {
            if(title.getText().toString().equals("")) {
                title.setText(alna);
                setAndSavePhoto();

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

                finalFile = new File(getRealPathFromURI(Selected_Image_Uri));

                //finaFile ====== path
            date = new Date(finalFile.lastModified());
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String reportDate = df.format(date);
            filePath = finalFile.toString();
            Photo photo = new Photo();
            photo.setName(fileName);
            photo.setAlbumNum(albomNo);
            photo.setUrl(filePath);
            photo.setAlna(alna);
            photo.setDate(reportDate);
            db.addPhoto(photo);
            db.close();

            notifyNumber();


            finish();

        } catch (Exception e) {
            Toast.makeText(TakePhoto.this, getString(R.string.fail) + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void load (View view)
    {
        loadOrPicture = 1;
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }



    protected void onActivityResult( int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CAMERA_ON && resultCode== RESULT_OK)
        {
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), Selected_Image_Uri);
                imageV.setBackgroundColor(Color.alpha(Color.WHITE));
                imageV.setImageBitmap(imageBitmap);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            Selected_Image_Uri = data.getData();
            imageV.setBackgroundColor(Color.alpha(Color.WHITE));
            imageV.setImageURI(Selected_Image_Uri);
        }

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

    public void notifyNumber() {
        SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = myPref.edit();
        int N = (myPref.getInt("photoAddedNumberInLine", 0)) + 1;
        editor.putInt("photoAddedNumberInLine", N);
        editor.apply();
        String Title = getString(R.string.complete);
        String message="";
        switch (N){
            case 1:{message = getString(R.string.ach1); showNotification(Title,message); break;}
            case 10:{message = getString(R.string.ach2);showNotification(Title,message); break;}
            case 25:{message = getString(R.string.ach3);showNotification(Title,message); break;}
            case 50:{message = getString(R.string.ach4);showNotification(Title,message); break;}
            case 100:{message = getString(R.string.ach5);showNotification(Title,message); break;}
            case 500:{message = getString(R.string.ach6);showNotification(Title,message); break;}
            case 1000:{message = getString(R.string.ach7);showNotification(Title,message); break;}
        }


    }

    private void showNotification(String title, String content) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(content)// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), Logo.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }


    private String SaveImage(Bitmap finalBitmap) {
        File newFile= new File("" + File.separator + "test.png");
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        try {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "album_photos";
            File outputDir= new File(path);
            if(!outputDir.exists()) {
                boolean wasSuccessful =outputDir.mkdirs();
                if (!wasSuccessful) {
                    System.out.println(getString(R.string.fail2));
                }
            }

            newFile = new File(path + File.separator +fileName +"-"+n+".png");
            FileOutputStream out = new FileOutputStream(newFile);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFile.toString();
    }
}