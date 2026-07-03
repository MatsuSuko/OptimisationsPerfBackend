package org.sebsy.openfoodfacts.service;

import org.sebsy.openfoodfacts.dao.AllergeneDao;
import org.sebsy.openfoodfacts.dto.TopOccurrenceResponse;
import org.sebsy.openfoodfacts.entity.Allergene;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service de gestion des allergènes.
 * Garantit l'unicité des allergènes en base de données.
 */
@Service
public class AllergeneService {

    private final AllergeneDao allergeneDao;

    public AllergeneService(AllergeneDao allergeneDao) {
        this.allergeneDao = allergeneDao;
    }

    /**
     * Retourne l'allergène portant ce nom si il existe, sinon le crée.
     *
     * @param nom le nom de l'allergène
     * @return l'allergène existant ou nouvellement créé
     */
    @Transactional
    @Cacheable(cacheNames = "allergensByName", unless = "#result == null")
    public Allergene findOrCreate(String nom) {
        return allergeneDao.findByNom(nom)
                .orElseGet(() -> allergeneDao.save(new Allergene(nom)));
    }

    /**
     * Retourne les N allergènes les plus courants dans les produits.
     *
     * @param limit le nombre maximum de résultats
     * @return la liste des allergènes les plus fréquents
     */
    @Cacheable(cacheNames = "topAllergens")
    public List<TopOccurrenceResponse> findTop(int limit) {
        return allergeneDao.findTopByOccurrence(limit).stream()
                .map(result -> new TopOccurrenceResponse(result.getId(), result.getNom(), result.getOccurrence()))
                .toList();
    }

    /**
     * Retourne le nombre total d'allergènes en base.
     *
     * @return le nombre d'allergènes persistés
     */
    public long count() {
        return allergeneDao.count();
    }
}
