package org.sebsy.state;

public class EtatCreation implements EtatCommande {

    @Override
    public void ajouterProduit(Produit produit, Commande commande) {
        commande.getProduits().add(produit);
        System.out.println("Produit '" + produit.getNom() + "' ajouté à la commande.");
    }

    @Override
    public void payer(Commande commande) {
        commande.setMontant(commande.getProduits().size() * 0.5);
        commande.setEtat(new EtatPaiement());
        System.out.println("Commande payée. Montant : " + commande.getMontant() + "€");
    }

    @Override
    public void livrer(String adresse, Commande commande) {
        System.out.println("Impossible de livrer : la commande n'a pas encore été payée.");
    }

    @Override
    public void annuler(Commande commande) {
        commande.setEtat(new EtatAnnulee());
        System.out.println("Commande annulée.");
    }
}
