package org.sebsy.openfoodfacts.service;

import org.sebsy.openfoodfacts.dao.AllergeneDao;
import org.sebsy.openfoodfacts.entity.Allergene;
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
    public List<Allergene> findTop(int limit) {
        return allergeneDao.findTopByOccurrence(limit);
    }
}
