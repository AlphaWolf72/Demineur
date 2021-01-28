package com.example.newdemineur;

import android.widget.ImageButton;

public class Cellule{

    private boolean is_decouvert;       //true si la cellule est découverte
    private boolean is_bombe;           //true si la cellule est une bombe
    private boolean is_flag;            //true si la cellule est proteger par un drapeau
    private ImageButton image;
    private int number;                 //valeur que va prendre la cellule
    private final int x;                //position x dans la grille
    private final int y;                //position y dans la grille

    //constructeur cellule
    public Cellule(boolean is_decouvert, boolean is_bombe, int number, int x, int y, boolean is_flag) {
        this.is_decouvert = is_decouvert;
        this.is_bombe = is_bombe;
        this.number = number;
        this.x = x;
        this.y = y;
        this.is_flag = is_flag;
    }


    public boolean isIs_decouvert() {
        return !is_decouvert;
    }

    public void setIs_decouvert(boolean is_decouvert) {
        this.is_decouvert = is_decouvert;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isIs_bombe() {
        return is_bombe;
    }

    public void setIs_bombe(boolean is_bombe) {
        this.is_bombe = is_bombe;
    }

    public ImageButton getImage() {
        return image;
    }

    public void setImage(ImageButton image) {
        this.image = image;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isIs_flag() {
        return is_flag;
    }

    public void setIs_flag(boolean is_flag) {
        this.is_flag = is_flag;
    }
}
