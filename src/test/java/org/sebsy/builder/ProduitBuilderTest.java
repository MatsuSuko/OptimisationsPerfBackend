package org.sebsy.builder;

import junit.framework.TestCase;
import org.junit.Test;

public class ProduitBuilderTest extends TestCase {

    @Test
    public void testCasNominal() {
        Produit produit = new ProduitBuilder()
                .nom("Coca-Cola")
                .grade("A")
                .categorie("Boisson")
                .marque("Coca-Cola Company")
                .ajouterIngredient("Eau", 330.0)
                .ajouterIngredient("Sucre", 35.0)
                .ajouterAllergene("Caféine", 5.0)
                .ajouterAdditif("E150d", 2.0)
                .build();

        assertEquals("Coca-Cola", produit.getNom());
        assertEquals("A", produit.getGrade());
        assertEquals("Boisson", produit.getCategorie().getNom());
        assertEquals("Coca-Cola Company", produit.getMarque().getNom());
        assertEquals(2, produit.getIngredients().size());
        assertEquals(1, produit.getAllergenes().size());
        assertEquals(1, produit.getAdditifs().size());
    }

    @Test
    public void testSansIngredients() {
        Produit produit = new ProduitBuilder()
                .nom("Eau")
                .grade("A")
                .categorie("Boisson")
                .marque("Evian")
                .build();

        assertEquals("Eau", produit.getNom());
        assertTrue(produit.getIngredients().isEmpty());
        assertTrue(produit.getAllergenes().isEmpty());
        assertTrue(produit.getAdditifs().isEmpty());
    }

    @Test
    public void testPlusieursAdditifs() {
        Produit produit = new ProduitBuilder()
                .nom("Chips")
                .grade("D")
                .categorie("Snack")
                .marque("Lays")
                .ajouterAdditif("E621", 1.5)
                .ajouterAdditif("E631", 0.8)
                .ajouterAdditif("E627", 0.5)
                .build();

        assertEquals(3, produit.getAdditifs().size());
        assertEquals("E621", produit.getAdditifs().get(0).getNom());
    }
}
