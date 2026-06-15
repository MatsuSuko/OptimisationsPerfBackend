package org.sebsy.openfoodfacts.dao;

import org.sebsy.openfoodfacts.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * DAO pour l'entité {@link Ingredient}.
 * Fournit les opérations CRUD, la recherche par nom et le classement par fréquence.
 */
@Repository
public interface IngredientDao extends JpaRepository<Ingredient, Long> {

    /**
     * Recherche un ingrédient par son nom exact.
     *
     * @param nom le nom de l'ingrédient
     * @return un Optional contenant l'ingrédient si il existe
     */
    Optional<Ingredient> findByNom(String nom);

    /**
     * Retourne les ingrédients les plus fréquents, triés par nombre d'occurrences décroissant.
     *
     * @param limit le nombre maximal de résultats
     * @return la liste des ingrédients les plus courants
     */
    @Query(value = """
        SELECT i.* FROM ingredient i
        JOIN produit_ingredient pi ON pi.ingredient_id = i.id
        GROUP BY i.id
        ORDER BY COUNT(pi.produit_id) DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<Ingredient> findTopByOccurrence(int limit);
}
