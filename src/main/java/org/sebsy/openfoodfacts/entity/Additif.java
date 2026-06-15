package org.sebsy.openfoodfacts.entity;

import jakarta.persistence.*;

/**
 * Additif alimentaire présent dans un produit (ex : "E500 - Carbonates de sodium").
 * Le nom est unique en base de données.
 */
@Entity
@Table(name = "additif")
public class Additif {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nom;

    public Additif() {}

    public Additif(String nom) {
        this.nom = nom;
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}
