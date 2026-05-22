package org.sebsy.state;

public class EtatEnLivraison implements EtatCommande {

    @Override
    public void ajouterProduit(Produit produit, Commande commande) {
        System.out.println("Impossible d'ajouter un produit : la commande est en cours de livraison.");
    }

    @Override
    public void payer(Commande commande) {
        System.out.println("Impossible de payer : la commande est en cours de livraison.");
    }

    @Override
    public void livrer(String adresse, Commande commande) {
        System.out.println("Impossible de livrer : la commande est déjà en cours de livraison.");
    }

    @Override
    public void annuler(Commande commande) {
        System.out.println("La commande est déjà en cours de livraison, l'annulation est impossible.");
    }
}
