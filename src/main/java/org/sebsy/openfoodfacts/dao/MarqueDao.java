package org.sebsy.openfoodfacts.dao;

import org.sebsy.openfoodfacts.entity.Marque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * DAO pour l'entité {@link Marque}.
 * Fournit les opérations CRUD et la recherche par nom.
 */
@Repository
public interface MarqueDao extends JpaRepository<Marque, Long> {

    /**
     * Recherche une marque par son nom exact.
     *
     * @param nom le nom de la marque
     * @return un Optional contenant la marque si elle existe
     */
    Optional<Marque> findByNom(String nom);
}
