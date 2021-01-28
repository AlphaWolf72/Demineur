package com.example.newdemineur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class AproposActivity extends AppCompatActivity {

    public TextView apropos;
    public TextView apropos2;
    public MediaPlayer musiqueFond;
    public static int media_length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //suppression de la bar du haut
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_apropos);

        //création mediaPlayer pour lire la musique de fond
        musiqueFond = MediaPlayer.create(this, R.raw.musicfond);
        musiqueFond.setLooping(true);
        //on récupére la position précédente de la lecture
        musiqueFond.seekTo(media_length);
        musiqueFond.start();

        apropos = findViewById(R.id.apropos);
        apropos2 = findViewById(R.id.apropos2);

        Button home = findViewById(R.id.accueilB);

        //récupération de la taille (en pixel) de l'écran pour ajuster les tailles de polices et des boutons
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int densityDpi = (int) (displayMetrics.density * 160f);
        int height = displayMetrics.heightPixels;

        home.setHeight((int) (height * 0.12));
        apropos.setTextSize((int) ((height / densityDpi) * 6.0));
        apropos2.setTextSize((int) ((height / densityDpi) * 6.0));

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //récupération de la position actuelle de la lecture
                media_length=musiqueFond.getCurrentPosition();
                HomeActivity.initSound(media_length);

                //changement d'activity pour aller à l'accueil
                goHome();
            }
        });
    }

    //initialisation de la position de la lecture
    public static void initSound(int length){
        media_length=length;
    }

    //changement d'activity pour aller à l'accueil
    public void goHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    //Récupérer l'avancée de la musique et mets pause
    @Override
    public void onResume(){
        super.onResume();
        musiqueFond.seekTo(media_length);
        musiqueFond.start();
    }

    //initialise l'avancée de la lecture et mets start
    @Override
    public void onPause() {
        super.onPause();
        musiqueFond.pause();
        media_length = musiqueFond.getCurrentPosition();
    }

}