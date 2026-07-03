package org.sebsy.openfoodfacts.dao;

/**
 * Projection légère pour exposer un libellé et son nombre d'occurrences.
 */
public interface NamedOccurrenceProjection {

    Long getId();

    String getNom();

    long getOccurrence();
}
