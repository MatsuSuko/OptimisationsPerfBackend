package org.sebsy.builder;

public class Allergene {

    private String nom;
    private double qteMilligrammes;

    public Allergene(String nom, double qteMilligrammes) {
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
