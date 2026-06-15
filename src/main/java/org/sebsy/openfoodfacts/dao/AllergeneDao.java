package org.sebsy.openfoodfacts.dao;

import org.sebsy.openfoodfacts.entity.Allergene;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * DAO pour l'entité {@link Allergene}.
 * Fournit les opérations CRUD, la recherche par nom et le classement par fréquence.
 */
@Repository
public interface AllergeneDao extends JpaRepository<Allergene, Long> {

    /**
     * Recherche un allergène par son nom exact.
     *
     * @param nom le nom de l'allergène
     * @return un Optional contenant l'allergène si il existe
     */
    Optional<Allergene> findByNom(String nom);

    /**
     * Retourne les allergènes les plus fréquents, triés par nombre d'occurrences décroissant.
     *
     * @param limit le nombre maximal de résultats
     * @return la liste des allergènes les plus courants
     */
    @Query(value = """
        SELECT a.* FROM allergene a
        JOIN produit_allergene pa ON pa.allergene_id = a.id
        GROUP BY a.id
        ORDER BY COUNT(pa.produit_id) DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<Allergene> findTopByOccurrence(int limit);
}
