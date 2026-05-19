package org.sebsy.builder;

import java.util.List;

public class Produit {

    private String nom;
    private String grade;
    private Categorie categorie;
    private Marque marque;
    private List<Ingredient> ingredients;
    private List<Allergene> allergenes;
    private List<Additif> additifs;

    public Produit(String nom, String grade, Categorie categorie, Marque marque,
                   List<Ingredient> ingredients, List<Allergene> allergenes, List<Additif> additifs) {
        this.nom = nom;
        this.grade = grade;
        this.categorie = categorie;
        this.marque = marque;
        this.ingredients = ingredients;
        this.allergenes = allergenes;
        this.additifs = additifs;
    }

    public String getNom() { return nom; }
    public String getGrade() { return grade; }
    public Categorie getCategorie() { return categorie; }
    public Marque getMarque() { return marque; }
    public List<Ingredient> getIngredients() { return ingredients; }
    public List<Allergene> getAllergenes() { return allergenes; }
    public List<Additif> getAdditifs() { return additifs; }
}
