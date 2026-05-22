package org.sebsy.state;

public interface EtatCommande {

    void ajouterProduit(Produit produit, Commande commande);

    void payer(Commande commande);

    void livrer(String adresse, Commande commande);

    void annuler(Commande commande);
}
