package org.sebsy.openfoodfacts.service;

import org.sebsy.openfoodfacts.dao.CategorieDao;
import org.sebsy.openfoodfacts.dao.MarqueDao;
import org.sebsy.openfoodfacts.dao.ProduitDao;
import org.sebsy.openfoodfacts.entity.Categorie;
import org.sebsy.openfoodfacts.entity.Marque;
import org.sebsy.openfoodfacts.entity.Produit;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service de gestion des produits alimentaires.
 * Fournit les opérations de persistance et les requêtes métier pour l'API REST.
 */
@Service
public class ProduitService {

    private final ProduitDao produitDao;
    private final CategorieDao categorieDao;
    private final MarqueDao marqueDao;

    public ProduitService(ProduitDao produitDao, CategorieDao categorieDao, MarqueDao marqueDao) {
        this.produitDao = produitDao;
        this.categorieDao = categorieDao;
        this.marqueDao = marqueDao;
    }

    /**
     * Persiste un produit en base de données.
     *
     * @param produit le produit à sauvegarder
     * @return le produit sauvegardé avec son identifiant généré
     */
    @Transactional
    @CacheEvict(
            cacheNames = {
                    "topProductsByBrand",
                    "topProductsByCategory",
                    "topProductsByBrandAndCategory"
            },
            allEntries = true
    )
    public Produit save(Produit produit) {
        return produitDao.save(produit);
    }

    /**
     * Retourne le nombre total de produits en base.
     *
     * @return le nombre de produits persistés
     */
    public long count() {
        return produitDao.count();
    }

    /**
     * Retourne tous les produits triés par identifiant.
     * Utile pour les affichages de diagnostic en console.
     *
     * @return la liste complète des produits persistés
     */
    public List<Produit> findAllForConsole() {
        return produitDao.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    /**
     * Retourne les N meilleurs produits d'une marque, triés par score nutritionnel (A → F).
     *
     * @param nomMarque le nom de la marque
     * @param limit     le nombre de produits à retourner
     * @return la liste des produits, ou une liste vide si la marque est inconnue
     */
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "topProductsByBrand")
    public List<Produit> findTopByMarque(String nomMarque, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return marqueDao.findByNom(nomMarque)
                .map(m -> produitDao.findByMarqueOrderByNutritionGradeFrAsc(m, pageable))
                .orElse(List.of());
    }

    /**
     * Retourne les N meilleurs produits d'une catégorie, triés par score nutritionnel (A → F).
     *
     * @param nomCategorie le nom de la catégorie
     * @param limit        le nombre de produits à retourner
     * @return la liste des produits, ou une liste vide si la catégorie est inconnue
     */
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "topProductsByCategory")
    public List<Produit> findTopByCategorie(String nomCategorie, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return categorieDao.findByNom(nomCategorie)
                .map(c -> produitDao.findByCategorieOrderByNutritionGradeFrAsc(c, pageable))
                .orElse(List.of());
    }

    /**
     * Retourne les N meilleurs produits d'une marque et d'une catégorie, triés par score nutritionnel.
     *
     * @param nomMarque    le nom de la marque
     * @param nomCategorie le nom de la catégorie
     * @param limit        le nombre de produits à retourner
     * @return la liste des produits correspondant aux deux critères
     */
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "topProductsByBrandAndCategory")
    public List<Produit> findTopByMarqueAndCategorie(String nomMarque, String nomCategorie, int limit) {
        Marque marque = marqueDao.findByNom(nomMarque).orElse(null);
        Categorie categorie = categorieDao.findByNom(nomCategorie).orElse(null);
        if (marque == null || categorie == null) {
            return List.of();
        }
        return produitDao.findByMarqueAndCategorieOrderByNutritionGradeFrAsc(marque, categorie, PageRequest.of(0, limit));
    }
}
