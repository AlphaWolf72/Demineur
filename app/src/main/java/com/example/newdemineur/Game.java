package com.example.newdemineur;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


import androidx.constraintlayout.widget.ConstraintLayout;


public class Game extends GameActivity {

    private Cellule[][] cellules;           //grille en 2 dimension
    public int xPos = 0;                    //position x en pixel
    public int yPos = 100;                  //position y en pixel
    public int SIZE = 50;                   //taille pour les cellules (en pixel)
    private GameActivity gameActivity;
    private Context cont;
    private ConstraintLayout window;
    public int nbC;                         //nombre de colones et de lignes
    public int nbDecouvert = 0;             //nombre de cellules découvertent
    public int nbB;                         //nombre de bombes


    //init du jeux
    public void init(GameActivity gameActivity, Context context, ConstraintLayout fenetrePrincipal, int width, int nbC, int nbB) {
        this.gameActivity = gameActivity;
        this.cont = context;
        this.window = fenetrePrincipal;
        this.nbC = nbC;
        this.nbB = nbB;

        //set size en fonction de la largeure de l'écran en pixel et du nombre de colones
        SIZE = width / nbC;
        cellules = new Cellule[nbC][nbC];
        //initialisation de la grille avec comme valeures par défauts
        //is_découvert = false
        //is_bombe = false
        //number = 0
        //posX = i
        //posY = j
        //is_Flag = false
        for (int i = 0; i < nbC; i++) {
            for (int j = 0; j < nbC; j++) {
                final Cellule cell = new Cellule(false, false, 0, i, j, false);
                final ImageButton imageNum = new ImageButton(cont);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(SIZE, SIZE);
                imageNum.setLayoutParams(params);
                imageNum.setX(xPos + SIZE * i);
                imageNum.setY(yPos + SIZE * j);
                imageNum.setBackgroundResource(R.drawable.back);
                cell.setImage(imageNum);
                window.addView(imageNum);
                imageNum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addBombe(nbBom, cell.getX(), cell.getY()); //ajouts des bombes aléatoirement
                        start();// set les onClickListener et les onLongClickListener
                        afficher(cell);//afficher la premiere cellule clicker
                    }
                });
                cellules[i][j] = cell;
            }
        }
    }

    //fonction appeller après la fin d'un grille pour supprimer les cellules de la grille
    private void remove() {
        for (int i = 0; i < nbC; i++) {
            for (int j = 0; j < nbC; j++) {
                final Cellule cell = cellules[i][j];
                ImageButton imageNum = cell.getImage();
                window.removeView(imageNum);
                imageNum.setBackgroundResource(R.drawable.back);
                imageNum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        init();
                    }
                });
                cell.setImage(imageNum);
                window.addView(imageNum);
                cellules[i][j] = cell;
            }
        }
    }

    //fonction appellée après remove() pour crée une nouvelle grille
    private void init() {
        for (int i = 0; i < nbC; i++) {
            for (int j = 0; j < nbC; j++) {
                final Cellule cell = cellules[i][j];
                cell.setIs_decouvert(false);
                cell.setIs_bombe(false);
                cell.setNumber(0);
                cell.setIs_flag(false);
                ImageButton imageNum = cell.getImage();
                window.removeView(imageNum);
                imageNum.setBackgroundResource(R.drawable.back);
                cell.setImage(imageNum);
                window.addView(imageNum);
                imageNum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addBombe(nbBom, cell.getX(), cell.getY());
                        start();
                        afficher(cell);
                    }
                });
                cellules[i][j] = cell;
            }
        }
    }

    //set les onClickListener et les onLongClickListener de chaque cellules
    //appui long = drapeau
    //click = affichage
    public void start() {
        gameActivity.start.start();
        for (int i = 0; i < nbC; i++) {
            for (int j = 0; j < nbC; j++) {
                final Cellule cell = cellules[i][j];
                cell.getImage().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        drapeau(cell);
                        return true;
                    }
                });
                cell.getImage().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        afficher(cell);
                    }
                });
            }
        }
    }

    public void addBombe(int nbBombe, int xPos, int yPos) {
        int x;
        int y;
        //tant que toutes les bombes ne sont pas placées
        for (int k = 0; k < nbBombe; k++) {
            x = (int) (Math.random() * ((nbC - 1) + 1));
            y = (int) (Math.random() * ((nbC - 1) + 1));

            //si la position aléatoire correspond à une cellule qui a déja une bombe,
            //ou bien que la position est en dehors de la grille
            while (cellules[x][y].isIs_bombe() || (x <= (xPos + 1) && x >= (xPos - 1)) && (y <= (yPos + 1) && y >= (yPos - 1))) {
                x = (int) (Math.random() * ((nbC - 1) + 1));
                y = (int) (Math.random() * ((nbC - 1) + 1));
            }
            float posX = cellules[x][y].getImage().getX();
            float posY = cellules[x][y].getImage().getY();
            window.removeView(cellules[x][y].getImage());
            ImageButton imageBombe = new ImageButton(cont);
            imageBombe.setBackgroundResource(R.drawable.back);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(SIZE, SIZE);
            imageBombe.setLayoutParams(params);
            imageBombe.setX(posX);
            imageBombe.setY(posY);
            window.addView(imageBombe);
            cellules[x][y].setImage(imageBombe);
            cellules[x][y].setIs_bombe(true);
            //ajout possibilitée de mettre un drapeau
            final Cellule cell2 = cellules[x][y];
            imageBombe.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    drapeau(cell2);
                    return true;
                }
            });
            imageBombe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveData();
                    afficher(cell2);
                }
            });
            //un foit la bombe mise, on va incrémenenter la valeure des cellules autour d'elle, à condition que ce n'est pas déja une bombe
            addNumber(x, y);
        }
    }

    public void drapeau(final Cellule cell) {
        //on avertit le joueur qu'il a placé un drapeau en jouant un son et un faisant une vibration
        gameActivity.vibreur(100);
        gameActivity.flag.start();
        float posX = cell.getImage().getX();
        float posY = cell.getImage().getY();
        gameActivity.decompteBombe();
        window.removeView(cell.getImage());
        ImageButton imageNum = new ImageButton(cont);
        imageNum.setBackgroundResource(R.drawable.flag);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(SIZE, SIZE);
        imageNum.setLayoutParams(params);
        imageNum.setX(posX);
        imageNum.setY(posY);
        cell.setIs_flag(true); //on set is_flag true pour dire que la cellule est protégée
        //si il y'a déja un drapeau, sa va l'enlever
        imageNum.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                gameActivity.ajoutBombe();          //on incremente le compteur de bombe pour afficher le nombre de bombre restante
                gameActivity.vibreur(100);    //on avertit le joueur qu'il a enlevé un drapeau en jouant un son et un faisant une vibration
                gameActivity.flag.start();
                float posX = cell.getImage().getX();
                float posY = cell.getImage().getY();
                window.removeView(cell.getImage());
                ImageButton imageNum = new ImageButton(cont);
                imageNum.setBackgroundResource(R.drawable.back);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(SIZE, SIZE);
                imageNum.setLayoutParams(params);
                imageNum.setX(posX);
                imageNum.setY(posY);
                cell.setIs_flag(false);
                imageNum.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        drapeau(cell);
                        return true;
                    }
                });
                imageNum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        afficher(cell);
                    }
                });
                window.addView(imageNum);
                return true;
            }
        });
        window.addView(imageNum);
    }

    //on afficher la cellule en remplacent sont image par la valeur correspondante
    //si c'est une bombe c'est GameOver,
    //sinon incrmente nbDecouvert de 1 et on on passe Is_Decouvert à true
    //et on ajoute le score en fonction de la valeur de la cellule
    public void afficher(final Cellule cell) {
        //on récupére les coordonées de la cellule
        float posX = cell.getImage().getX();
        float posY = cell.getImage().getY();
        //on joue le son start
        gameActivity.afficher.start();
        window.removeView(cell.getImage());
        final ImageButton imageNum = new ImageButton(cont);
        //si c'est une bombe
        if (cell.isIs_bombe()) {
            imageNum.setBackgroundResource(R.drawable.bombe);
            gameActivity.vibreur(500);                              //activation vibration pendant 500ms
            Intent intent = new Intent(cont, GameOverActivity.class);     //lancement de l'activiter GameOver
            cont.startActivity(intent);
        } else if (cell.getNumber() == 1) { //si c'est un 1
            imageNum.setBackgroundResource(R.drawable.num1);
            if (cell.isIs_decouvert()) {
                nbDecouvert++;
                cell.setIs_decouvert(true);
                gameActivity.addScore(10);
            }
        } else if (cell.getNumber() == 2) { //si c'est un 2
            imageNum.setBackgroundResource(R.drawable.num2);
            if (cell.isIs_decouvert()) {
                nbDecouvert++;
                cell.setIs_decouvert(true);
                gameActivity.addScore(20);
            }
        } else if (cell.getNumber() == 3) { //si c'est un 3
            imageNum.setBackgroundResource(R.drawable.num3);
            if (cell.isIs_decouvert()) {
                nbDecouvert++;
                cell.setIs_decouvert(true);
                gameActivity.addScore(30);
            }
        } else if (cell.getNumber() == 4) { //si c'est un 4
            imageNum.setBackgroundResource(R.drawable.num4);
            if (cell.isIs_decouvert()) {
                nbDecouvert++;
                cell.setIs_decouvert(true);
                gameActivity.addScore(40);
            }
        } else if (cell.getNumber() == 5) { //si c'est un 5
            imageNum.setBackgroundResource(R.drawable.num5);
            if (cell.isIs_decouvert()) {
                nbDecouvert++;
                cell.setIs_decouvert(true);
                gameActivity.addScore(50);
            }
        } else if (cell.getNumber() == 6) { //si c'est un 6
            imageNum.setBackgroundResource(R.drawable.num6);
            if (cell.isIs_decouvert()) {
                nbDecouvert++;
                cell.setIs_decouvert(true);
                gameActivity.addScore(60);
            }
        } else if (cell.getNumber() == 7) { //si c'est un 7
            imageNum.setBackgroundResource(R.drawable.num7);
            if (cell.isIs_decouvert()) {
                nbDecouvert++;
                cell.setIs_decouvert(true);
                gameActivity.addScore(70);
            }
        } else if (cell.getNumber() == 8) { //si c'est un 8
            imageNum.setBackgroundResource(R.drawable.num8);
            if (cell.isIs_decouvert()) {
                nbDecouvert++;
                cell.setIs_decouvert(true);
                gameActivity.addScore(80);
            }
        } else if (cell.getNumber() == 0) { //si c'est un 0
            imageNum.setBackgroundResource(R.drawable.num0);
            if (cell.isIs_decouvert()) {
                cell.setIs_decouvert(true);
                decouvrir(cell.getX(), cell.getY());
                nbDecouvert++;
                gameActivity.addScore(1);
            }
        }
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(SIZE, SIZE);
        imageNum.setLayoutParams(params);
        imageNum.setX(posX);
        imageNum.setY(posY);
        //on créee onClickListener aprés qu'elle soit découverte
        //il permet d'afficher les cases autour d'un cellules découverte
        //si sa valeur est égale au nombre de cellules protéger par un drapeau autour d'elle
        imageNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nb = cell.getNumber();
                int nbF = 0;
                for (int a = cell.getX() - 1; a <= cell.getX() + 1; a++) {
                    for (int b = cell.getY() - 1; b <= cell.getY() + 1; b++) {
                        if ((a >= 0 && a < nbC) && (b >= 0 && b < nbC) && cellules[a][b].isIs_flag()) {
                            nbF++;
                        }
                    }
                }
                if (nb == nbF) {
                    decouvrir(cell.getX(), cell.getY());
                }
            }
        });
        window.addView(imageNum);
        //si le nombre de cellules découverte est égale au nombre de cellules total moins le nombre de bombre
        //la grille est fini, on lance une nouvelle grille
        //le score reste le même et continue d'augmenter
        if (nbDecouvert == ((nbCell * nbCell) - nbBom)) {
            nbDecouvert = 0;
            gameActivity.ajoutBombes(nbB);
            remove();
        }
    }

    public void decouvrir(int x, int y) {
        for (int i = x - 1; i <= x + 1; i++) {          // x et y sont la postion de la bombe
            for (int j = y - 1; j <= y + 1; j++) {      //on va regarder les cellules de x-1 y+1 à x+1 y-1
                //on verifie que ça ne sort pas de la grille et que la cellule n'est pas protéger et qu'elle n'est pas encore découverte
                if ((i >= 0 && i < nbC) && (j >= 0 && j < nbC) && !cellules[i][j].isIs_flag() && cellules[i][j].isIs_decouvert()) {
                    //on affiche la cellule
                    afficher(cellules[i][j]);
                }
            }
        }
    }

    //ajouter les nombres aprés l'ajout d'une bombe
    public void addNumber(int x, int y) {                                           // x et y sont la postion de la bombe
        for (int i = x - 1; i <= x + 1; i++) {                                      //on va regarder les cellules de x-1 y+1 à x+1 y-1
            for (int j = y - 1; j <= y + 1; j++) {                                  //
                if (i >= 0 && j >= 0 && i <= nbC - 1 && j <= nbC - 1) {             //on verifie que ça ne sort pas de la grille
                    if (!cellules[i][j].isIs_bombe()) {                             //si la case autour de la bombe n'est pas elle aussi une bombe
                        cellules[i][j].setNumber(cellules[i][j].getNumber() + 1);   //on incrémente de 1 la valeur de la cellule
                    }
                }
            }
        }
    }
}
