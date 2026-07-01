package org.sebsy.openfoodfacts.service;

import org.sebsy.openfoodfacts.dao.CategorieDao;
import org.sebsy.openfoodfacts.dao.MarqueDao;
import org.sebsy.openfoodfacts.dao.ProduitDao;
import org.sebsy.openfoodfacts.entity.Categorie;
import org.sebsy.openfoodfacts.entity.Marque;
import org.sebsy.openfoodfacts.entity.Produit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Produit save(Produit produit) {
        return produitDao.save(produit);
    }

    /**
     * Retourne les N meilleurs produits d'une marque, triés par score nutritionnel (A → F).
     *
     * @param nomMarque le nom de la marque
     * @param limit     le nombre de produits à retourner
     * @return la liste des produits, ou une liste vide si la marque est inconnue
     */
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
    public List<Produit> findTopByMarqueAndCategorie(String nomMarque, String nomCategorie, int limit) {
        Marque marque = marqueDao.findByNom(nomMarque).orElse(null);
        Categorie categorie = categorieDao.findByNom(nomCategorie).orElse(null);
        if (marque == null || categorie == null) {
            return List.of();
        }
        return produitDao.findByMarqueAndCategorieOrderByNutritionGradeFrAsc(marque, categorie, PageRequest.of(0, limit));
    }
}
