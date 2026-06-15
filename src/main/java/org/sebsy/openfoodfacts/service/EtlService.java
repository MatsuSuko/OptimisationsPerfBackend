package org.sebsy.openfoodfacts.service;

import org.sebsy.openfoodfacts.entity.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service ETL principal : lit le fichier CSV Open Food Facts,
 * nettoie les données et les persiste en base via les services métier.
 */
@Service
public class EtlService {

    @Value("${etl.csv.path}")
    private String csvPath;

    private final CategorieService categorieService;
    private final MarqueService marqueService;
    private final IngredientService ingredientService;
    private final AllergeneService allergeneService;
    private final AdditifService additifService;
    private final ProduitService produitService;

    public EtlService(CategorieService categorieService,
                      MarqueService marqueService,
                      IngredientService ingredientService,
                      AllergeneService allergeneService,
                      AdditifService additifService,
                      ProduitService produitService) {
        this.categorieService = categorieService;
        this.marqueService = marqueService;
        this.ingredientService = ingredientService;
        this.allergeneService = allergeneService;
        this.additifService = additifService;
        this.produitService = produitService;
    }

    /**
     * Charge et traite le fichier CSV Open Food Facts.
     * La première ligne (en-tête) est ignorée.
     *
     * @throws IOException si le fichier est introuvable ou illisible
     */
    public void chargerFichier() throws IOException {
        long debut = System.currentTimeMillis();
        int compteur = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {
            reader.readLine(); // ignorer l'en-tête

            String ligne;
            while ((ligne = reader.readLine()) != null) {
                traiterLigne(ligne);
                compteur++;
            }
        }

        long duree = System.currentTimeMillis() - debut;
        System.out.printf("ETL terminé : %d produits chargés en %d ms%n", compteur, duree);
    }

    /**
     * Traite une ligne CSV et persiste le produit correspondant.
     *
     * @param ligne une ligne brute du fichier CSV
     */
    private void traiterLigne(String ligne) {
        String[] champs = ligne.split("\\|", -1);
        if (champs.length < 30) return;

        Categorie categorie = categorieService.findOrCreate(nettoyer(champs[0]));
        Marque marque = marqueService.findOrCreate(nettoyer(champs[1]));

        Produit produit = new Produit();
        produit.setNom(nettoyer(champs[2]));
        produit.setNutritionGradeFr(champs[3].trim().toLowerCase());
        produit.setCategorie(categorie);
        produit.setMarque(marque);

        produit.setEnergie100g(parseDouble(champs[5]));
        produit.setGraisse100g(parseDouble(champs[6]));
        produit.setSucres100g(parseDouble(champs[7]));
        produit.setFibres100g(parseDouble(champs[8]));
        produit.setProteines100g(parseDouble(champs[9]));
        produit.setSel100g(parseDouble(champs[10]));
        produit.setVitA100g(parseDouble(champs[11]));
        produit.setVitD100g(parseDouble(champs[12]));
        produit.setVitE100g(parseDouble(champs[13]));
        produit.setVitK100g(parseDouble(champs[14]));
        produit.setVitC100g(parseDouble(champs[15]));
        produit.setVitB1100g(parseDouble(champs[16]));
        produit.setVitB2100g(parseDouble(champs[17]));
        produit.setVitPP100g(parseDouble(champs[18]));
        produit.setVitB6100g(parseDouble(champs[19]));
        produit.setVitB9100g(parseDouble(champs[20]));
        produit.setVitB12100g(parseDouble(champs[21]));
        produit.setCalcium100g(parseDouble(champs[22]));
        produit.setMagnesium100g(parseDouble(champs[23]));
        produit.setIron100g(parseDouble(champs[24]));
        produit.setFer100g(parseDouble(champs[25]));
        produit.setBetaCarotene100g(parseDouble(champs[26]));
        produit.setPresenceHuilePalme("1".equals(champs[27].trim()));

        List<Ingredient> ingredients = decouperListe(champs[4]).stream()
                .map(ingredientService::findOrCreate)
                .collect(Collectors.toList());
        produit.setIngredients(ingredients);

        List<Allergene> allergenes = decouperListe(champs[28]).stream()
                .map(allergeneService::findOrCreate)
                .collect(Collectors.toList());
        produit.setAllergenes(allergenes);

        List<Additif> additifs = decouperListe(champs[29]).stream()
                .map(additifService::findOrCreate)
                .collect(Collectors.toList());
        produit.setAdditifs(additifs);

        produitService.save(produit);
    }

    /**
     * Nettoie une valeur brute du CSV :
     * supprime le contenu entre parenthèses, les pourcentages et les caractères spéciaux.
     *
     * @param valeur la chaîne brute
     * @return la chaîne nettoyée
     */
    String nettoyer(String valeur) {
        if (valeur == null || valeur.isBlank()) return "";
        // Supprime le contenu entre parenthèses : ex. "Pâte (Farine 50%)" → "Pâte"
        valeur = valeur.replaceAll("\\([^)]*\\)", "");
        // Supprime les pourcentages : ex. "Sucre 15%" → "Sucre"
        valeur = valeur.replaceAll("\\d+[,.]?\\d*\\s*%", "");
        // Supprime les caractères parasites : *, _, [, ], #, etc.
        valeur = valeur.replaceAll("[*_\\[\\]#@!^]", "");
        return valeur.trim();
    }

    /**
     * Découpe une liste d'éléments séparés par des virgules ou des tirets.
     * Applique le nettoyage sur chaque élément et supprime les doublons.
     *
     * @param valeur la chaîne contenant la liste brute
     * @return la liste des éléments nettoyés et dédupliqués
     */
    List<String> decouperListe(String valeur) {
        if (valeur == null || valeur.isBlank()) return Collections.emptyList();

        // Séparateur principal : virgule ; fallback : tiret entouré d'espaces
        String[] parties = valeur.split(",");
        if (parties.length <= 1 && valeur.contains(" - ")) {
            parties = valeur.split(" - ");
        }

        return Arrays.stream(parties)
                .map(this::nettoyer)
                .filter(s -> !s.isBlank())
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Convertit une chaîne en Double, retourne null si la valeur est vide ou invalide.
     *
     * @param valeur la chaîne à convertir
     * @return la valeur numérique ou null
     */
    private Double parseDouble(String valeur) {
        try {
            return Double.parseDouble(valeur.trim().replace(",", "."));
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }
}
