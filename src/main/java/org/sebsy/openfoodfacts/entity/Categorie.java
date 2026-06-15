package org.sebsy.openfoodfacts.entity;

import jakarta.persistence.*;

/**
 * Catégorie d'un produit alimentaire (ex : "Additifs alimentaires").
 * Le nom est unique en base de données.
 */
@Entity
@Table(name = "categorie")
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nom;

    public Categorie() {}

    public Categorie(String nom) {
        this.nom = nom;
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}
