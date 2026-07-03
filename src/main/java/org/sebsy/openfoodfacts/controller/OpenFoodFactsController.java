package org.sebsy.openfoodfacts.controller;

import org.sebsy.openfoodfacts.dto.ProductSummaryResponse;
import org.sebsy.openfoodfacts.dto.TopOccurrenceResponse;
import org.sebsy.openfoodfacts.entity.Produit;
import org.sebsy.openfoodfacts.service.AdditifService;
import org.sebsy.openfoodfacts.service.AllergeneService;
import org.sebsy.openfoodfacts.service.IngredientService;
import org.sebsy.openfoodfacts.service.ProduitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API REST pour exposer les données Open Food Facts nettoyées.
 */
@RestController
public class OpenFoodFactsController {

    private final ProduitService produitService;
    private final IngredientService ingredientService;
    private final AllergeneService allergeneService;
    private final AdditifService additifService;

    public OpenFoodFactsController(ProduitService produitService,
                                   IngredientService ingredientService,
                                   AllergeneService allergeneService,
                                   AdditifService additifService) {
        this.produitService = produitService;
        this.ingredientService = ingredientService;
        this.allergeneService = allergeneService;
        this.additifService = additifService;
    }

    @GetMapping("/products/top-by-brand")
    public List<ProductSummaryResponse> getTopByBrand(@RequestParam("brand") String brand,
                                                      @RequestParam("limit") int limit) {
        return produitService.findTopByMarque(brand, normalizeLimit(limit)).stream()
                .map(this::toProductSummary)
                .toList();
    }

    @GetMapping("/products/top-by-category")
    public List<ProductSummaryResponse> getTopByCategory(@RequestParam("category") String category,
                                                         @RequestParam("limit") int limit) {
        return produitService.findTopByCategorie(category, normalizeLimit(limit)).stream()
                .map(this::toProductSummary)
                .toList();
    }

    @GetMapping("/products/top-by-brand-category")
    public List<ProductSummaryResponse> getTopByBrandAndCategory(@RequestParam("brand") String brand,
                                                                 @RequestParam("category") String category,
                                                                 @RequestParam("limit") int limit) {
        return produitService.findTopByMarqueAndCategorie(brand, category, normalizeLimit(limit)).stream()
                .map(this::toProductSummary)
                .toList();
    }

    @GetMapping("/ingredients/top")
    public List<TopOccurrenceResponse> getTopIngredients(@RequestParam("limit") int limit) {
        return ingredientService.findTop(normalizeLimit(limit));
    }

    @GetMapping("/allergens/top")
    public List<TopOccurrenceResponse> getTopAllergens(@RequestParam("limit") int limit) {
        return allergeneService.findTop(normalizeLimit(limit));
    }

    @GetMapping("/additives/top")
    public List<TopOccurrenceResponse> getTopAdditives(@RequestParam("limit") int limit) {
        return additifService.findTop(normalizeLimit(limit));
    }

    private int normalizeLimit(int limit) {
        return Math.max(1, limit);
    }

    private ProductSummaryResponse toProductSummary(Produit produit) {
        return new ProductSummaryResponse(
                produit.getId(),
                produit.getNom(),
                produit.getNutritionGradeFr(),
                produit.getMarque().getNom(),
                produit.getCategorie().getNom()
        );
    }
}
