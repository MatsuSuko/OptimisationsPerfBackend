package org.sebsy.openfoodfacts.dao;

import org.sebsy.openfoodfacts.entity.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * DAO pour l'entité {@link Categorie}.
 * Fournit les opérations CRUD et la recherche par nom.
 */
@Repository
public interface CategorieDao extends JpaRepository<Categorie, Long> {

    /**
     * Recherche une catégorie par son nom exact.
     *
     * @param nom le nom de la catégorie
     * @return un Optional contenant la catégorie si elle existe
     */
    Optional<Categorie> findByNom(String nom);
}
