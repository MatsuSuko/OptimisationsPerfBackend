package org.sebsy.openfoodfacts.entity;

import jakarta.persistence.*;

/**
 * Marque d'un produit alimentaire (ex : "La Patelière").
 * Le nom est unique en base de données.
 */
@Entity
@Table(name = "marque")
public class Marque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nom;

    public Marque() {}

    public Marque(String nom) {
        this.nom = nom;
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}
