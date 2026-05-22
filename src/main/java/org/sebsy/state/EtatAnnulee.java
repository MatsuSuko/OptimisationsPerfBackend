package org.sebsy.state;

public class EtatAnnulee implements EtatCommande {

    @Override
    public void ajouterProduit(Produit produit, Commande commande) {
        System.out.println("Impossible d'ajouter un produit : la commande a été annulée.");
    }

    @Override
    public void payer(Commande commande) {
        System.out.println("Impossible de payer : la commande a été annulée.");
    }

    @Override
    public void livrer(String adresse, Commande commande) {
        System.out.println("Impossible de livrer : la commande a été annulée.");
    }

    @Override
    public void annuler(Commande commande) {
        System.out.println("La commande a déjà été annulée.");
    }
}
