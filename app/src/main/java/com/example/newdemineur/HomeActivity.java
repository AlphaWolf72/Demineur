package com.example.newdemineur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {


    public ConstraintLayout homeWindow;
    public ImageView logo;
    public TextView nameTxt;
    public TextView presTxt;
    public Button tuto;
    public Button facile;
    public Button moyen;
    public Button difficile;
    public Button apropos;
    public int nbCellules;
    public MediaPlayer musiqueFond;     //création d'un mediaPlayer pour la musique de fond
    public static int media_length;     //variable pour stocker l'avancée de la lecture de la musique

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_home);

        //création mediaPlayer pour lire la musique de fond
        musiqueFond = MediaPlayer.create(this, R.raw.musicfond);
        musiqueFond.setLooping(true);
        musiqueFond.start();

        //init des views
        homeWindow = findViewById(R.id.homeLayout);
        logo = findViewById(R.id.logo);
        nameTxt = findViewById(R.id.nameTxt);
        presTxt = findViewById(R.id.presTxt);

        tuto = findViewById(R.id.tutoB);
        tuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTuto();
            }
        });

        //boutton pour lancer le jeux en facile
        facile = findViewById(R.id.facileB);
        facile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nbCellules = 9;
                start(8, 10, 1);
            }
        });
        //boutton pour lancer le jeux en moyen
        moyen = findViewById(R.id.moyenB);
        moyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nbCellules = 13;
                start(12, 20, 2);
            }
        });
        //boutton pour lancer le jeux en difficile
        difficile = findViewById(R.id.homeB);
        difficile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nbCellules = 17;
                start(16, 40, 3);
            }
        });
        //boutton pour aller dans A propos
        apropos = findViewById(R.id.aproposB);
        apropos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                media_length=musiqueFond.getCurrentPosition();
                AproposActivity.initSound(media_length);
                goApropos();
            }
        });

        //récupération de la taille (en pixel) de l'écran pour ajuster les tailles de polices et des bouttons
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int densityDpi = (int) (displayMetrics.density * 160f);
        int height = displayMetrics.heightPixels;
        facile.setHeight((int) (height * 0.09));
        moyen.setHeight((int) (height * 0.09));
        difficile.setHeight((int) (height * 0.09));
        apropos.setHeight((int) (height * 0.09));
        tuto.setHeight((int) (height * 0.09));
        nameTxt.setTextSize((int) ((height / densityDpi) * 10.0));
        presTxt.setTextSize((int) ((height / densityDpi) * 3.5));
    }

    //changement d'activity pour lancer le jeux
    public void start(int nbC, int nbB, int mode) {
        GameActivity.initNb(nbC, nbB, mode); //init du nombres de cellules/bombes/mode, avant le lancement du jeux
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    //changement d'activity pour aller dans A propos
    public void goApropos() {
        Intent intent = new Intent(this, AproposActivity.class);
        startActivity(intent);
    }

    //changement d'activity pour aller dans Tuto
    public void goTuto() {
        Intent intent = new Intent(this, TutoActivity.class);
        startActivity(intent);
    }

    @Override
    //recuperer l'avancée de la musique et mets pause
    public void onResume(){
        super.onResume();
        musiqueFond.seekTo(media_length);
        musiqueFond.start();
    }

    public static void initSound(int length){
        media_length=length;
    }

    @Override
    //initialise l'avancée de la lecture et mets strat
    public void onPause() {
        super.onPause();
        musiqueFond.pause();
        media_length = musiqueFond.getCurrentPosition();
    }
}