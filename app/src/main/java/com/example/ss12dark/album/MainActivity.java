package com.example.ss12dark.album;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {
TextView albumNameView;
Button newMe;
MyDBHandler db;
List <Photo> all;
LinearLayout upperPage,bottomPage;
String number;
LinearLayout pageColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backgroundColor();

        upperPage = (LinearLayout) findViewById(R.id.upperPage);
        bottomPage = (LinearLayout) findViewById(R.id.bottomPage);
        albumNameView = (TextView) findViewById(R.id.numberofplace);
        Intent lobby = getIntent();
        final int num = lobby.getIntExtra("num",666);
        final String alna = lobby.getStringExtra("alna");
        albumNameView.setText(alna);

        newMe = (Button) findViewById(R.id.newM);

        newMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photo = new Intent(MainActivity.this,TakePhoto.class);
                photo.putExtra("album",num);
                photo.putExtra("alna",alna);
                startActivityForResult(photo,1);
            }
        });



        db = new MyDBHandler(this);
        number = num+"";
        all =db.getAllPhotoList(num);
        if(all.size()>0){
            new AsyncCaller().execute(number);
        }else{
            Toast.makeText(MainActivity.this,"this album is empty",Toast.LENGTH_SHORT).show();
        }
        db.close();
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

    protected void onActivityResult( int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1)
        {
            recreate();

        }
    }

    private class AsyncCaller extends AsyncTask<String, Void, Void>
    {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this); //the loading massage in the screen

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
        protected Void doInBackground(String... params) { //getting the album number and put it in int variable
            String number = params[0];
            int num = Integer.parseInt(number);
            startAlbum(num);


            return null;
        }

        private void startAlbum(int num) {
            int i = 0;

                //running with loop on the "all" list of photos and making a images
                while(i<all.size()){
                    int album = all.get(i).getAlbumNum();
                    if(album==num){//checking if the album number is the same
                        String name = all.get(i).getName();
                        String date = all.get(i).getDate();
                        String filePath = all.get(i).getUrl();//the path to the location of imgae in the phone
                        int ID = all.get(i).getID();
                        createImage(name,filePath,ID,date);
                    }
                    i++;
                }

        }

        private void createImage(String name, String filePath,int ID,String date){
            final ImageView image = new ImageView(MainActivity.this);
            resizeImage(image,filePath,ID);

            File imgFile = new  File(filePath);
            if(imgFile.exists()){

                Bitmap myBitmap =decodeSampledBitmapFromURL(filePath);//half quality method
                image.setImageBitmap(myBitmap);//set the imgae on the view

            }

            final TextView text = new TextView(MainActivity.this);
            resizeText(text);
            text.setText(date+"\n"+"\""+name+"\"");

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
            //taking the image and the quality of the image half sized (bad quality)
            options.inSampleSize = 2;
            return BitmapFactory.decodeFile(filePath, options);
        }

        private void resizeImage(final ImageView sv,final String filePath,final int ID){
            LinearLayout.LayoutParams positionRules = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            sv.setLayoutParams(positionRules); //settting the size of the image with hieght and width and the click and longclick listener action's
            sv.getLayoutParams().height=700;
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
                    Vibrator vi = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vi.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        vi.vibrate(500);
                    }
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
            sv.getLayoutParams().height=255;
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

