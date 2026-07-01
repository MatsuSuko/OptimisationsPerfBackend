package org.sebsy.openfoodfacts.service;

import org.sebsy.openfoodfacts.dao.IngredientDao;
import org.sebsy.openfoodfacts.entity.Ingredient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service de gestion des ingrédients.
 * Garantit l'unicité des ingrédients en base de données.
 */
@Service
public class IngredientService {

    private final IngredientDao ingredientDao;

    public IngredientService(IngredientDao ingredientDao) {
        this.ingredientDao = ingredientDao;
    }

    /**
     * Retourne l'ingrédient portant ce nom si il existe, sinon le crée.
     *
     * @param nom le nom de l'ingrédient
     * @return l'ingrédient existant ou nouvellement créé
     */
    @Transactional
    @Cacheable(cacheNames = "ingredientsByName", unless = "#result == null")
    public Ingredient findOrCreate(String nom) {
        return ingredientDao.findByNom(nom)
                .orElseGet(() -> ingredientDao.save(new Ingredient(nom)));
    }

    /**
     * Retourne les N ingrédients les plus courants dans les produits.
     *
     * @param limit le nombre maximum de résultats
     * @return la liste des ingrédients les plus fréquents
     */
    @Cacheable(cacheNames = "topIngredients")
    public List<Ingredient> findTop(int limit) {
        return ingredientDao.findTopByOccurrence(limit);
    }
}
