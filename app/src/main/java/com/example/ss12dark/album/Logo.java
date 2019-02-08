package com.example.ss12dark.album;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;

public class Logo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_logo);

        //set the screen to full using windowManager
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_logo);

        appear();

    }

    public void appear(){
        final ImageView DianaProductions = (ImageView) findViewById(R.id.cap);

        ViewPropertyAnimator viewPropertyAnimator = DianaProductions.animate().alpha(1f).setDuration(2000); //making the picture visable in 2 sec
        viewPropertyAnimator.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ViewPropertyAnimator viewPropertyAnimator2 =DianaProductions.animate().alpha(0f).setDuration(2000);//making the picture invisable in 2 sec
                viewPropertyAnimator2.setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        come();
                    }
                });
            }
        });
    }

    public void come(){
        Intent next = new Intent(this,AlbumsLobby.class);
        startActivity(next);
        finish();
    }
}