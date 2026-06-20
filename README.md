# ETL Open Food Facts

Backend Java optimisé qui ingère, nettoie, stocke et expose les données du fichier
Open Food Facts (produits alimentaires fabriqués en France) via une API REST.

Projet réalisé dans le cadre du module **Optimisation Backend avec Java** (M2 Dev).

**Auteurs :** Souvanny BOUNMY, Léo LAFORE

## Stack technique

- **Spring Boot 3.3** / Java 21
- **Spring Data JPA** (Hibernate) pour l'accès aux données
- **MySQL** (H2 disponible en alternative pour les tests)
- Maven

## Architecture

```
org.sebsy.openfoodfacts/
├── entity/    # Entités JPA : Produit, Categorie, Marque, Ingredient, Allergene, Additif
├── dao/       # Repositories Spring Data JPA (un par entité métier)
└── service/   # Couche service + EtlService (lecture/nettoyage/chargement du CSV)
```

### Règles de gestion

- Catégorie, Marque, Ingrédient, Allergène et Additif sont **uniques en base**
  (pattern `findOrCreate` dans chaque service).
- Les ingrédients/allergènes/additifs sont nettoyés à l'import : suppression des
  parenthèses, des pourcentages et des caractères parasites (`*`, `_`, etc.), et
  découpés sur plusieurs séparateurs possibles (`,` ou `-`).

Voir [conception/](conception/) pour le diagramme de classes et le MLD.

## Configuration

La base de données est configurée dans
[application.properties](src/main/resources/application.properties) :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/open_food_facts?...
spring.datasource.username=root
spring.datasource.password=root
```

Adapter les identifiants à votre environnement. Une alternative H2 en mémoire est
disponible en commentaire dans le même fichier pour tester sans serveur MySQL.

Le chemin du fichier CSV source est défini par la propriété `etl.csv.path`.

## Lancer le projet

```bash
mvn clean install
mvn spring-boot:run
```

## Format du fichier source

Le fichier CSV (séparateur `|`) comporte 30 colonnes par produit : catégorie,
marque, nom, score nutritionnel (A à F), ingrédients, valeurs nutritionnelles
pour 100g, présence d'huile de palme, allergènes et additifs.

## Avancement du TP

- [x] **Objectif 1** — Conception (diagramme de classes, MLD) → [conception/](conception/)
- [x] **Objectif 2** — Entités JPA, DAOs, couche service
- [ ] **Objectif 3** — Optimisation (cache, Virtual Threads, performances)
- [ ] **Objectif 4** — API REST (`/products`, `/ingredients`, `/allergens`, `/additives`)
