package com.example.ss12dark.album;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
TextView numberOfAlbum;
Button newMe,refresh;
MyDBHandler db;
List <Photo> all;
LinearLayout upperPage,bottomPage;
ScrollView pageColor;
String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backgroundColor();
        findViewsAndDB();

        Intent lobby = getIntent();
        final int num = lobby.getIntExtra("num",666);
        final String alna = lobby.getStringExtra("alna");
        numberOfAlbum.setText(alna);

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

        number = num+"";
        all =db.getAllPhotoList(num);
        if(all.size()>0){
            new AsyncCaller().execute(number);
        }else{
            Toast.makeText(MainActivity.this,"this album is empty",Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public void findViewsAndDB(){
        upperPage = (LinearLayout) findViewById(R.id.upperPage);
        bottomPage = (LinearLayout) findViewById(R.id.bottomPage);
        numberOfAlbum = (TextView) findViewById(R.id.numberofplace);
        newMe = (Button) findViewById(R.id.newM);
        refresh = (Button) findViewById(R.id.refresh);
        db = new MyDBHandler(this);
    }

    public void backgroundColor(){
        pageColor = (ScrollView) findViewById(R.id.pagecolor);
        SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(this);
        int background =myPref.getInt("pageColor",1);
        switch (background){
            case 1:{ pageColor.setBackground(getDrawable(R.drawable.mainactivity));break;}
            case 2:{ pageColor.setBackground(getDrawable(R.drawable.mainactivityblack));break;}
            case 3:{ pageColor.setBackground(getDrawable(R.drawable.mainactivitypink));break;}
        }
    }

    private class AsyncCaller extends AsyncTask<String, Void, Void> {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pdLoading.setMessage("\tLoading...");
                    pdLoading.show();
                }
            });

        }
        @Override
        protected Void doInBackground(String... params) {
            String number = params[0];
            int num = Integer.parseInt(number);
            startAlbum(num);


            return null;
        }

        private void startAlbum(int num) {
            int i = 0;


                while(i<all.size()){
                    int album = all.get(i).getAlbumNum();
                    if(album==num){
                        String name = all.get(i).getName();
                        String filePath = all.get(i).getUrl();
                        int ID = all.get(i).getID();
                        createImage(name,filePath,ID);
                    }
                    i++;
                }

        }

        private void createImage(String name, String filePath,int ID){
            final ImageView image = new ImageView(MainActivity.this);
            resizeImage(image,filePath,ID);
            int sdkVersion = Build.VERSION.SDK_INT;
            if(sdkVersion<24){
                Uri myUri = Uri.parse(filePath);
                image.setImageURI(myUri);
            }else{
                File imgFile = new  File(filePath);
                if(imgFile.exists()){

                    Bitmap myBitmap =decodeSampledBitmapFromURL(filePath);
                    image.setImageBitmap(myBitmap);

                }
            }
            final TextView text = new TextView(MainActivity.this);
            resizeText(text);
            text.setText("\""+name+"\"");

            MainActivity.this.runOnUiThread(new Runnable() {
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
            sv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
            sv.getLayoutParams().height=200;
            sv.getLayoutParams().width=575;
            sv.setTextSize(20);
            sv.setTextColor(Color.BLACK);
            positionRules.setMargins(10, 1, 10, 1);
            sv.setPadding(5,5,5,5);
            sv.setBackground(getDrawable(R.drawable.textmain));
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pdLoading.dismiss();
                }
            });

        }

    }
}

