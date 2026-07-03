package org.sebsy.openfoodfacts.dto;

/**
 * Réponse REST pour un ingrédient, allergène ou additif fréquent.
 */
public record TopOccurrenceResponse(
        Long id,
        String nom,
        long occurrence
) {
}
