package org.sebsy.state;

public class EtatPaiement implements EtatCommande {

    @Override
    public void ajouterProduit(Produit produit, Commande commande) {
        System.out.println("Impossible d'ajouter un produit : la commande a déjà été payée.");
    }

    @Override
    public void payer(Commande commande) {
        System.out.println("Impossible de payer : la commande a déjà été payée.");
    }

    @Override
    public void livrer(String adresse, Commande commande) {
        commande.setAdresse(adresse);
        commande.setEtat(new EtatEnLivraison());
        System.out.println("Commande en cours de livraison à : " + adresse);
    }

    @Override
    public void annuler(Commande commande) {
        commande.setEtat(new EtatAnnulee());
        System.out.println("Commande annulée.");
    }
}
