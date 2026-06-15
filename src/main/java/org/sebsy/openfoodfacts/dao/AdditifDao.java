package org.sebsy.openfoodfacts.dao;

import org.sebsy.openfoodfacts.entity.Additif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * DAO pour l'entité {@link Additif}.
 * Fournit les opérations CRUD, la recherche par nom et le classement par fréquence.
 */
@Repository
public interface AdditifDao extends JpaRepository<Additif, Long> {

    /**
     * Recherche un additif par son nom exact.
     *
     * @param nom le nom de l'additif
     * @return un Optional contenant l'additif si il existe
     */
    Optional<Additif> findByNom(String nom);

    /**
     * Retourne les additifs les plus fréquents, triés par nombre d'occurrences décroissant.
     *
     * @param limit le nombre maximal de résultats
     * @return la liste des additifs les plus courants
     */
    @Query(value = """
        SELECT a.* FROM additif a
        JOIN produit_additif pad ON pad.additif_id = a.id
        GROUP BY a.id
        ORDER BY COUNT(pad.produit_id) DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<Additif> findTopByOccurrence(int limit);
}
