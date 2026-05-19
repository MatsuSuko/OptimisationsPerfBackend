package org.sebsy.builder;

public class Ingredient {

    private String nom;
    private double qteMilligrammes;

    public Ingredient(String nom, double qteMilligrammes) {
        this.nom = nom;
        this.qteMilligrammes = qteMilligrammes;
    }

    public String getNom() {
        return nom;
    }

    public double getQteMilligrammes() {
        return qteMilligrammes;
    }
}
