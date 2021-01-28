package com.example.newdemineur;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity {

    public static String SHARED_PREF = "shared_prefs";
    public static String FACILE = "facile";
    public static String MOYEN = "moyen";
    public static String DIFFICILE = "difficile";
    public static int modeO;
    public static int scoreO;
    public static int bestScoreO;
    public TextView yourScore;
    public TextView bestScore;
    public static SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public MediaPlayer loose;

    @SuppressLint({"CommitPrefEdits", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_game_over);
        modeO = GameActivity.modeG;
        loose = MediaPlayer.create(this, R.raw.loose);
        loose.start();

        sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        scoreO = GameActivity.scoreNb;
        GameActivity.scoreNb = 0;
        yourScore = findViewById(R.id.yourScore);
        bestScore = findViewById(R.id.bestScore);
        Button restart = findViewById(R.id.restartButton);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
            }
        });
        Button home = findViewById(R.id.homeB);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int densityDpi = (int) (displayMetrics.density * 160f);
        restart.setHeight((int) (height * 0.12));
        home.setHeight((int) (height * 0.12));
        bestScore.setTextSize((int) ((height / densityDpi) * 8));
        bestScore.setText("BEST SCORE : " + bestScoreO);
        yourScore.setTextSize((int) ((height / densityDpi) * 8));
        yourScore.setText("SCORE : " + scoreO);
        saveData();
        loadData();
        updateViews();
    }

    public void restartGame() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void goHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void saveData() {
        if (modeO == 1 && scoreO > sharedPreferences.getInt(FACILE, 0)) {
            editor.putInt(FACILE, scoreO);
            Log.e("test", "F");
        } else if (modeO == 2 && scoreO > sharedPreferences.getInt(MOYEN, 0)) {
            editor.putInt(MOYEN, scoreO);
            Log.e("test", "M");
        } else if (modeO == 3 && scoreO > sharedPreferences.getInt(DIFFICILE, 0)) {
            editor.putInt(DIFFICILE, scoreO);
            Log.e("test", "D");
        }
        editor.commit();

    }

    public void loadData() {
        if (modeO == 1) {
            bestScoreO = sharedPreferences.getInt(FACILE, 0);
        } else if (modeO == 2) {
            bestScoreO = sharedPreferences.getInt(MOYEN, 0);
        } else if (modeO == 3) {
            bestScoreO = sharedPreferences.getInt(DIFFICILE, 0);
        }
    }


    @SuppressLint("SetTextI18n")
    public void updateViews() {
        bestScore.setText("BEST SCORE: " + bestScoreO);
    }

}