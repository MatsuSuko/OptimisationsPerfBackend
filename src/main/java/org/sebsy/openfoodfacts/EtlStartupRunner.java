package org.sebsy.openfoodfacts;

import org.sebsy.openfoodfacts.service.EtlService;
import org.sebsy.openfoodfacts.service.ProduitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

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

    @Value("${etl.run-on-startup:true}")
    private boolean runOnStartup;

    public EtlStartupRunner(EtlService etlService, ProduitService produitService) {
        this.etlService = etlService;
        this.produitService = produitService;
    }

    @Override
    public void run(String... args) {
        if (!runOnStartup) {
            logger.info("ETL désactivé au démarrage (etl.run-on-startup=false).");
            return;
        }

        long productCount = produitService.count();
        if (productCount > 0) {
            logger.info("ETL ignoré au démarrage : {} produits déjà présents en base.", productCount);
            return;
        }

        try {
            logger.info("Démarrage de l'import ETL Open Food Facts...");
            etlService.chargerFichier();
            logger.info("Import ETL terminé.");
        } catch (IOException e) {
            throw new IllegalStateException("Impossible de charger le fichier CSV Open Food Facts.", e);
        }
    }
}
