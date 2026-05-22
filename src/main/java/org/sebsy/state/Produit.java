package org.sebsy.state;

public class Produit {

    private String nom;
    private String grade;

    public Produit(String nom, String grade) {
        this.nom = nom;
        this.grade = grade;
    }

    public String getNom() { return nom; }
    public String getGrade() { return grade; }
}
