package org.sebsy.openfoodfacts.service;

import org.sebsy.openfoodfacts.dao.AdditifDao;
import org.sebsy.openfoodfacts.entity.Additif;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service de gestion des additifs alimentaires.
 * Garantit l'unicité des additifs en base de données.
 */
@Service
public class AdditifService {

    private final AdditifDao additifDao;

    public AdditifService(AdditifDao additifDao) {
        this.additifDao = additifDao;
    }

    /**
     * Retourne l'additif portant ce nom si il existe, sinon le crée.
     *
     * @param nom le nom de l'additif
     * @return l'additif existant ou nouvellement créé
     */
    @Transactional
    @Cacheable(cacheNames = "additivesByName", unless = "#result == null")
    public Additif findOrCreate(String nom) {
        return additifDao.findByNom(nom)
                .orElseGet(() -> additifDao.save(new Additif(nom)));
    }

    /**
     * Retourne les N additifs les plus courants dans les produits.
     *
     * @param limit le nombre maximum de résultats
     * @return la liste des additifs les plus fréquents
     */
    @Cacheable(cacheNames = "topAdditives")
    public List<Additif> findTop(int limit) {
        return additifDao.findTopByOccurrence(limit);
    }
}
