package com.daude.calendari;

import android.graphics.Color;

public class DiaSetmana {
    private String nom;
    private String numero;
    private String mes;
    private int color;

    public DiaSetmana(String nombre, String num, String smes){
        nom = nombre;
        numero = num;
        mes = smes;
        color = 0xf0f0f0;    //Por defecto color blanco
    }

    public String getNom(){
        return nom;
    }
    public String getNumero(){
        return numero;
    }
    public String getMes(){
        return mes;
    }
    public int getColor() { return color; }

    public void setNom(String n){
        nom = n;
    }
    public void setColor(int c){
        //Modificador del color del layout del item concreto
        color = c;
    }
}
