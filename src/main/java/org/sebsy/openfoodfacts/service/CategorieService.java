package org.sebsy.openfoodfacts.service;

import org.sebsy.openfoodfacts.dao.CategorieDao;
import org.sebsy.openfoodfacts.entity.Categorie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service de gestion des catégories.
 * Garantit l'unicité des catégories en base de données.
 */
@Service
public class CategorieService {

    private final CategorieDao categorieDao;

    public CategorieService(CategorieDao categorieDao) {
        this.categorieDao = categorieDao;
    }

    /**
     * Retourne la catégorie portant ce nom si elle existe, sinon la crée.
     *
     * @param nom le nom de la catégorie
     * @return la catégorie existante ou nouvellement créée
     */
    @Transactional
    public Categorie findOrCreate(String nom) {
        return categorieDao.findByNom(nom)
                .orElseGet(() -> categorieDao.save(new Categorie(nom)));
    }

    /**
     * Retourne toutes les catégories en base.
     *
     * @return la liste de toutes les catégories
     */
    public List<Categorie> findAll() {
        return categorieDao.findAll();
    }
}
