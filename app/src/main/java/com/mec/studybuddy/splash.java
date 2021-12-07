package com.mec.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splash extends AppCompatActivity {
    private ImageView logo , logoyazi ;
    private FirebaseAuth auth ;
    private FirebaseUser currentUser ;
    private Animation animation ;
    public void baglayici(){
        logoyazi = (ImageView) findViewById(R.id.logoyazi);
        logo = (ImageView) findViewById(R.id.logo);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        baglayici();



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               if(currentUser == null){
                   Intent mi = new Intent(splash.this , MainActivity.class);
                   startActivity(mi);
                   finish();


               }else {
                   Intent mi = new Intent(splash.this , etut_merkezi.class);
                   startActivity(mi);
                   finish();


               }
            }
        },3000);




    }
}