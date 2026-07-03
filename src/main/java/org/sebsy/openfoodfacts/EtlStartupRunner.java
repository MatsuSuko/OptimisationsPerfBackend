package org.sebsy.openfoodfacts;

import org.sebsy.openfoodfacts.entity.Produit;
import org.sebsy.openfoodfacts.service.EtlService;
import org.sebsy.openfoodfacts.service.CategorieService;
import org.sebsy.openfoodfacts.service.MarqueService;
import org.sebsy.openfoodfacts.service.IngredientService;
import org.sebsy.openfoodfacts.service.AllergeneService;
import org.sebsy.openfoodfacts.service.AdditifService;
import org.sebsy.openfoodfacts.service.ProduitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Déclenche le chargement initial du CSV Open Food Facts au démarrage.
 * Si des produits sont déjà présents en base, l'import est ignoré pour
 * éviter de dupliquer les données à chaque relance.
 */
@Component
public class EtlStartupRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(EtlStartupRunner.class);

    private final EtlService etlService;
    private final ProduitService produitService;
    private final CategorieService categorieService;
    private final MarqueService marqueService;
    private final IngredientService ingredientService;
    private final AllergeneService allergeneService;
    private final AdditifService additifService;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${etl.run-on-startup:true}")
    private boolean runOnStartup;

    @Value("${etl.console.print-products:false}")
    private boolean printProductsInConsole;

    public EtlStartupRunner(EtlService etlService,
                            ProduitService produitService,
                            CategorieService categorieService,
                            MarqueService marqueService,
                            IngredientService ingredientService,
                            AllergeneService allergeneService,
                            AdditifService additifService) {
        this.etlService = etlService;
        this.produitService = produitService;
        this.categorieService = categorieService;
        this.marqueService = marqueService;
        this.ingredientService = ingredientService;
        this.allergeneService = allergeneService;
        this.additifService = additifService;
    }

    @Override
    public void run(String... args) {
        afficherEtatBase();

        if (!runOnStartup) {
            logger.info("ETL désactivé au démarrage (etl.run-on-startup=false).");
            return;
        }

        long productCount = produitService.count();
        if (productCount > 0) {
            logger.info("ETL ignoré au démarrage : {} produits déjà présents en base.", productCount);
            afficherResumeBase();
            return;
        }

        try {
            logger.info("Démarrage de l'import ETL Open Food Facts...");
            etlService.chargerFichier();
            logger.info("Import ETL terminé.");
            afficherResumeBase();
        } catch (IOException e) {
            throw new IllegalStateException("Impossible de charger le fichier CSV Open Food Facts.", e);
        }
    }

    private void afficherEtatBase() {
        if (datasourceUrl.contains(":mem:")) {
            logger.info("Mode de base temporaire détecté : {}.", datasourceUrl);
            System.out.printf("BDD INFO : mode temporaire detecte (%s) -> les donnees ne sont pas conservees entre les lancements.%n",
                    datasourceUrl);
            return;
        }

        logger.info("Mode de base persistant détecté : {}.", datasourceUrl);
        System.out.printf("BDD INFO : mode persistant detecte (%s) -> les donnees peuvent etre conservees entre les lancements.%n",
                datasourceUrl);
    }

    private void afficherResumeBase() {
        long produits = produitService.count();
        long categories = categorieService.count();
        long marques = marqueService.count();
        long ingredients = ingredientService.count();
        long allergenes = allergeneService.count();
        long additifs = additifService.count();

        logger.info("Résumé BDD | produits={} | categories={} | marques={} | ingredients={} | allergenes={} | additifs={}",
                produits, categories, marques, ingredients, allergenes, additifs);
        System.out.printf(
                "BDD RESUME : produits=%d | categories=%d | marques=%d | ingredients=%d | allergenes=%d | additifs=%d%n",
                produits, categories, marques, ingredients, allergenes, additifs
        );

        if (printProductsInConsole) {
            afficherProduitsImportes();
        }
    }

    private void afficherProduitsImportes() {
        List<Produit> produits = produitService.findAllForConsole();
        logger.info("Affichage console des produits importés : {} entrées.", produits.size());
        System.out.println("BDD PRODUITS IMPORTES :");

        for (Produit produit : produits) {
            System.out.printf("- id=%d | nom=%s | grade=%s%n",
                    produit.getId(),
                    produit.getNom(),
                    produit.getNutritionGradeFr() == null ? "n/a" : produit.getNutritionGradeFr()
            );
        }
    }
}
