package org.sebsy.openfoodfacts.entity;

import jakarta.persistence.*;

/**
 * Allergène présent dans un produit alimentaire (ex : "Gluten").
 * Le nom est unique en base de données.
 */
@Entity
@Table(name = "allergene")
public class Allergene {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nom;

    public Allergene() {}

    public Allergene(String nom) {
        this.nom = nom;
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}
