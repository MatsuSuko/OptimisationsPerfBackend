package org.sebsy.openfoodfacts.dto;

/**
 * Vue REST simplifiée d'un produit.
 */
public record ProductSummaryResponse(
        Long id,
        String nom,
        String nutritionGradeFr,
        String marque,
        String categorie
) {
}
