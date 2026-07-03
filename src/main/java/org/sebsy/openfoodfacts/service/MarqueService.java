package org.sebsy.openfoodfacts.service;

import org.sebsy.openfoodfacts.dao.MarqueDao;
import org.sebsy.openfoodfacts.entity.Marque;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service de gestion des marques.
 * Garantit l'unicité des marques en base de données.
 */
@Service
public class MarqueService {

    private final MarqueDao marqueDao;

    public MarqueService(MarqueDao marqueDao) {
        this.marqueDao = marqueDao;
    }

    /**
     * Retourne la marque portant ce nom si elle existe, sinon la crée.
     *
     * @param nom le nom de la marque
     * @return la marque existante ou nouvellement créée
     */
    @Transactional
    @Cacheable(cacheNames = "brandsByName", unless = "#result == null")
    public Marque findOrCreate(String nom) {
        return marqueDao.findByNom(nom)
                .orElseGet(() -> marqueDao.save(new Marque(nom)));
    }

    /**
     * Retourne toutes les marques en base.
     *
     * @return la liste de toutes les marques
     */
    public List<Marque> findAll() {
        return marqueDao.findAll();
    }

    /**
     * Retourne le nombre total de marques en base.
     *
     * @return le nombre de marques persistées
     */
    public long count() {
        return marqueDao.count();
    }
}
