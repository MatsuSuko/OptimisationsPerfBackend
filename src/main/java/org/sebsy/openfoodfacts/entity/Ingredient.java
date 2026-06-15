package org.sebsy.openfoodfacts.entity;

import jakarta.persistence.*;

/**
 * Ingrédient d'un produit alimentaire (ex : "Sucre").
 * Le nom est unique en base de données.
 */
@Entity
@Table(name = "ingredient")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nom;

    public Ingredient() {}

    public Ingredient(String nom) {
        this.nom = nom;
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}
