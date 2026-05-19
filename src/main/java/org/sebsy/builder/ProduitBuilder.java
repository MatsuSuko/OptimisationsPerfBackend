package org.sebsy.builder;

import java.util.ArrayList;
import java.util.List;

public class ProduitBuilder {

    private String nom;
    private String grade;
    private String categorieNom;
    private String marqueNom;
    private List<Ingredient> ingredients = new ArrayList<>();
    private List<Allergene> allergenes = new ArrayList<>();
    private List<Additif> additifs = new ArrayList<>();

    public ProduitBuilder nom(String nom) {
        this.nom = nom;
        return this;
    }

    public ProduitBuilder grade(String grade) {
        this.grade = grade;
        return this;
    }

    public ProduitBuilder categorie(String nom) {
        this.categorieNom = nom;
        return this;
    }

    public ProduitBuilder marque(String nom) {
        this.marqueNom = nom;
        return this;
    }

    public ProduitBuilder ajouterIngredient(String nom, double qteMilligrammes) {
        this.ingredients.add(new Ingredient(nom, qteMilligrammes));
        return this;
    }

    public ProduitBuilder ajouterAllergene(String nom, double qteMilligrammes) {
        this.allergenes.add(new Allergene(nom, qteMilligrammes));
        return this;
    }

    public ProduitBuilder ajouterAdditif(String nom, double qteMilligrammes) {
        this.additifs.add(new Additif(nom, qteMilligrammes));
        return this;
    }

    public Produit build() {
        return new Produit(nom, grade, new Categorie(categorieNom), new Marque(marqueNom),
                ingredients, allergenes, additifs);
    }
}
