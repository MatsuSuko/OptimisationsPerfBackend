package org.sebsy.state;

import java.util.ArrayList;
import java.util.List;

public class Commande {

    private double montant;
    private EtatCommande etat;
    private List<Produit> produits = new ArrayList<>();
    private String adresse;

    public Commande() {
        this.etat = new EtatCreation();
    }

    public void ajouterProduit(Produit produit) {
        etat.ajouterProduit(produit, this);
    }

    public void payer() {
        etat.payer(this);
    }

    public void livrer(String adresse) {
        etat.livrer(adresse, this);
    }

    public void annuler() {
        etat.annuler(this);
    }

    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }
    public EtatCommande getEtat() { return etat; }
    public void setEtat(EtatCommande etat) { this.etat = etat; }
    public List<Produit> getProduits() { return produits; }
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
}
