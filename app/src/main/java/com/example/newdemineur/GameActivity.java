package com.example.newdemineur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class GameActivity extends AppCompatActivity {

    public ConstraintLayout fenetrePrincipal;       //
    public static int scoreNb;                      //score
    public int decompte;                            //nombre de bombes restantes
    public TextView scoreText;                      //
    public TextView nombreBombe;                    //
    public static int nbCell;                       //nombre de cellules
    public static int nbBom;                        //nombre de bombes
    public static int modeG;                        //difficulté facile=1 moyen=2 difficile=3
    Vibrator vibrator;                              //

    public MediaPlayer start;                       //son jouer pour un start
    public MediaPlayer afficher;                    //son jouer pour un affichage
    public MediaPlayer flag;                        //son jouer pour un drapeau

    public static String SHARED_PREF = "shared_prefs";
    public static String FACILE = "facile";
    public static String MOYEN = "moyen";
    public static String DIFFICILE = "difficile";
    public int bestScore;
    public static SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;


    @SuppressLint({"SetTextI18n", "CommitPrefEdits"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //suppression bar du haut
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        //création d'un vibrator
        setContentView(R.layout.activity_main);

        decompte = nbBom;   //init du nombre de bombes restantes par le nombres de bombes total

        //init des mediaPlayer
        flag = MediaPlayer.create(this, R.raw.flag);
        afficher = MediaPlayer.create(this, R.raw.afficher);
        start = MediaPlayer.create(this, R.raw.start);

        //init du sharedPrefs
        sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        scoreText = findViewById(R.id.score123);                //init de l'affichage du text
        scoreNb = 0;                                              //init du score à 0
        scoreText.setText("Score : " + scoreNb);                //modificaiton du text qui est afficher dans scoreText
        nombreBombe = findViewById(R.id.nbBombe);               //init de l'affichage du décompte de bombes
        nombreBombe.setText("Bombes restantes : " + decompte);  //modificaiton du text qui est afficher dans nombreBombe

        fenetrePrincipal = findViewById(R.id.fenetrePrincipale);

        //récupération des dimensions en pixel de l'écran
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int densityDpi = (int) (displayMetrics.density * 160f);

        //changement de la taille des text pour les adapter à la taille de l'écrans
        scoreText.setTextSize((int) ((height / densityDpi) * 5.0));
        nombreBombe.setTextSize((int) ((height / densityDpi) * 4.0));

        Button restartB = findViewById(R.id.restartB);
        //changement de la hauteur du boutton pour l'adapter à la taille de l'écrans
        restartB.setHeight((int) (height * 0.08));
        restartB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
            }
        });

        Button home = findViewById(R.id.gameHomeB);
        //changement de la hauteur du boutton pour l'adapter à la taille de l'écrans
        home.setHeight((int) (height * 0.08));
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });

        Game game = new Game(); //création d'un jeux
        game.init(this, this, fenetrePrincipal, width, nbCell, nbBom);//lancement de l'initialisation du jeux
        saveData();
        loadData();
    }

    //init le nombre de cellules, bombes et mode en fonction du mode choisie
    public static void initNb(int nbC, int nbB, int mode) {
        nbCell = nbC;
        nbBom = nbB;
        modeG = mode;
    }

    //relancer l'activité
    public void restartGame() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    //ajouter le score
    public void addScore(int scoreAdd) {
        scoreNb = scoreNb + scoreAdd;
        String txt = "Score : " + scoreNb + "\nBest Scrore : " + bestScore;
        scoreText.setText(txt);
    }

    //désincrémenter le nombre de bombes restantes si un drapeau est mits
    public void decompteBombe() {
        decompte--;
        String txt = "Bombe(s) restante(s) : " + decompte;
        nombreBombe.setText(txt);
    }

    //incrémenter le nombre de bombes restantes si un drapeau est enlevé
    public void ajoutBombe() {
        decompte = decompte + 1;
        String txt = "Bombe(s) restante(s) : " + decompte;
        nombreBombe.setText(txt);
    }

    //init le nombre de bombes restantes au nombre de bombes total
    public void ajoutBombes(int nb) {
        decompte = nb;
        String txt = "Bombe(s) restante(s) : " + decompte;
        nombreBombe.setText(txt);
    }

    //changement d'activité vers l'activité accueil
    public void goHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    //fonction lancement vibration en fonction du temps voulue (en ms)
    public void vibreur(int time) {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(time);
    }

    public void saveData() {
        if (modeG == 1 && scoreNb > sharedPreferences.getInt(FACILE, 0)) {
            editor.putInt(FACILE, scoreNb);
        } else if (modeG == 2 && scoreNb > sharedPreferences.getInt(MOYEN, 0)) {
            editor.putInt(MOYEN, scoreNb);
        } else if (modeG == 3 && scoreNb > sharedPreferences.getInt(DIFFICILE, 0)) {
            editor.putInt(DIFFICILE, scoreNb);
        }
        editor.commit();
    }

    public void loadData() {
        if (modeG == 1) {
            bestScore = sharedPreferences.getInt(FACILE, 0);
        } else if (modeG == 2) {
            bestScore = sharedPreferences.getInt(MOYEN, 0);
        } else if (modeG == 3) {
            bestScore = sharedPreferences.getInt(DIFFICILE, 0);
        }
    }
    /*
    @SuppressLint("SetTextI18n")
    public void updateViews() {
        bestScore.setText("BEST SCORE: " + bestScoreO);
    }
    */
}