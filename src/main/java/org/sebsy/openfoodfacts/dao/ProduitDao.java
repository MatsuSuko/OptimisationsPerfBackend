package org.sebsy.openfoodfacts.dao;

import org.sebsy.openfoodfacts.entity.Categorie;
import org.sebsy.openfoodfacts.entity.Marque;
import org.sebsy.openfoodfacts.entity.Produit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO pour l'entité {@link Produit}.
 * Fournit les opérations CRUD et des requêtes métier pour l'API REST.
 */
@Repository
public interface ProduitDao extends JpaRepository<Produit, Long> {

    /**
     * Retourne les N meilleurs produits d'une marque, triés par score nutritionnel.
     *
     * @param marque   la marque ciblée
     * @param pageable pagination définissant le nombre de produits à retourner
     * @return la liste des produits triés par grade (A → F)
     */
    List<Produit> findByMarqueOrderByNutritionGradeFrAsc(Marque marque, Pageable pageable);

    /**
     * Retourne les N meilleurs produits d'une catégorie, triés par score nutritionnel.
     *
     * @param categorie la catégorie ciblée
     * @param pageable  pagination définissant le nombre de produits à retourner
     * @return la liste des produits triés par grade (A → F)
     */
    List<Produit> findByCategorieOrderByNutritionGradeFrAsc(Categorie categorie, Pageable pageable);

    /**
     * Retourne les N meilleurs produits d'une marque et d'une catégorie, triés par score nutritionnel.
     *
     * @param marque    la marque ciblée
     * @param categorie la catégorie ciblée
     * @param pageable  pagination définissant le nombre de produits à retourner
     * @return la liste des produits triés par grade (A → F)
     */
    List<Produit> findByMarqueAndCategorieOrderByNutritionGradeFrAsc(Marque marque, Categorie categorie, Pageable pageable);
}
