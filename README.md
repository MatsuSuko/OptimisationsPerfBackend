# Design Patterns — Java

Projet réalisé dans le cadre du module **Optimisation Backend avec Java** (M2 Dev).

Implémentation de plusieurs design patterns en Java.

**Auteurs :** Souvanny BOUNMY, Léo LAFORE

## Stack technique

- **Java 21**
- **Spring Boot 3.3** / Maven
- **JUnit 4 / 5** pour les tests unitaires
- **H2** (base en mémoire)

## ETL Open Food Facts
Package : `org.sebsy.openfoodfacts`

Application Spring Boot qui lit le fichier CSV Open Food Facts
([src/main/resources/open-food-facts.csv](src/main/resources/open-food-facts.csv)),
nettoie les données et les persiste en base via JPA/Hibernate (`Produit`, `Categorie`,
`Marque`, `Ingredient`, `Allergene`, `Additif`).

Par défaut la configuration ([application.properties](src/main/resources/application.properties))
utilise **H2** en local.

Au démarrage, l'application lance automatiquement l'ETL si la base est vide.
Si des produits sont déjà présents, l'import est ignoré pour éviter les doublons
à chaque redémarrage.

Pour désactiver ce comportement:

```properties
etl.run-on-startup=false
```

### Avancement

- [x] **Objectif 1** — Conception (diagramme de classes, MLD) → [conception/](conception/)
- [x] **Objectif 2** — Entités JPA, DAOs, couche service
- [x] **Objectif 3** — Optimisation (cache Spring, Virtual Threads, H2)
- [x] **Objectif 4** — API REST (`/products`, `/ingredients`, `/allergens`, `/additives`)

## Lancer le projet

```bash
mvn clean compile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.datasource.url=jdbc:h2:mem:etltest --server.port=8081"
```

À surveiller dans les logs :

- `ETL démarré | ...`
- `ETL terminé : XXXX produits chargés en YYYY ms`

Console H2 :

```text
http://localhost:8081/h2-console
```

Identifiants H2 :

```text
JDBC URL   : jdbc:h2:mem:etltest
User Name  : sa
Password   :
```

## Objectif 3 — Optimisation

Le projet utilise :

- **Spring Cache** pour éviter certaines recherches répétées
- **Virtual Threads** pour paralléliser le traitement des lignes du CSV
- **H2** pour tester localement rapidement

Configuration utile dans [application.properties](src/main/resources/application.properties) :

## Objectif 4 — API REST

Routes disponibles :

- `GET /products/top-by-brand?brand=X&limit=N`
- `GET /products/top-by-category?category=X&limit=N`
- `GET /products/top-by-brand-category?brand=X&category=Y&limit=N`
- `GET /ingredients/top?limit=N`
- `GET /allergens/top?limit=N`
- `GET /additives/top?limit=N`

Exemples d'URLs :

```text
http://localhost:8081/products/top-by-brand?brand=Nestle&limit=5
http://localhost:8081/products/top-by-category?category=Yaourts&limit=5
http://localhost:8081/products/top-by-brand-category?brand=Nestle&category=Yaourts&limit=5
http://localhost:8081/ingredients/top?limit=10
http://localhost:8081/allergens/top?limit=10
http://localhost:8081/additives/top?limit=10
```
