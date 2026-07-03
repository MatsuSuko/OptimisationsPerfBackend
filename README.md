# Design Patterns — Java

Projet réalisé dans le cadre du module **Optimisation Backend avec Java** (M2 Dev).

Implémentation de plusieurs design patterns en Java.

**Auteurs :** Souvanny BOUNMY, Léo LAFORE

## Stack technique

- **Java 21**
- **Spring Boot 3.3** / Maven
- **JUnit 4 / 5** pour les tests unitaires
- **H2** (base en mémoire pour tester sans MySQL) / **MySQL** en alternative

## Vérifier que tout fonctionne

```bash
# Version Java (doit afficher 21.x)
java -version

# Tests unitaires
mvn clean test

# Classes exécutables des patterns
mvn compile
mvn exec:java -Dexec.mainClass="org.sebsy.strategy.DemoTri"
mvn exec:java -Dexec.mainClass="org.sebsy.composite.TestComposite"

# Application Spring Boot (ETL Open Food Facts)
mvn spring-boot:run
# → au premier lancement : "Démarrage de l'import ETL..." puis "ETL terminé : XXXX produits chargés"
# → aux lancements suivants : "ETL ignoré au démarrage : XXXX produits déjà présents en base"
# → dans tous les cas : "Tomcat started on port 8080" sans erreur
```

## ETL Open Food Facts
Package : `org.sebsy.openfoodfacts`

Application Spring Boot qui lit le fichier CSV Open Food Facts
([src/main/resources/open-food-facts.csv](src/main/resources/open-food-facts.csv)),
nettoie les données et les persiste en base via JPA/Hibernate (`Produit`, `Categorie`,
`Marque`, `Ingredient`, `Allergene`, `Additif`).

Par défaut la configuration ([application.properties](src/main/resources/application.properties))
utilise **H2** en local (aucun serveur à lancer). MySQL reste disponible en
alternative (lignes commentées dans le même fichier).

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
cd /Users/leolafore/OptimisationsPerfBackend
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

Si tu lances le projet avec une autre URL JDBC, il faut mettre cette même valeur
dans le champ `JDBC URL` de la console H2.

## Objectif 3 — Optimisation

Le projet utilise :

- **Spring Cache** pour éviter certaines recherches répétées
- **Virtual Threads** pour paralléliser le traitement des lignes du CSV
- **H2** pour tester localement rapidement

Configuration utile dans [application.properties](src/main/resources/application.properties) :

```properties
spring.cache.type=simple
etl.virtual-threads-enabled=true
etl.max-in-flight-tasks=256
etl.run-on-startup=true
```

## Objectif 4 — API REST

Important :

- `http://localhost:8081/` renvoie `404`, c'est normal
- il faut appeler directement une route API

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

## Patterns implémentés

### GRASP — Refactoring `ReservationController`
Package : `org.sebsy.grasps`

Refactoring d'un contrôleur de réservation de billets (cinéma / théâtre) en appliquant les principes GRASP :
- **Information Expert** : `TypeReservation.calculerTotal()` calcule le montant total
- **Information Expert** : `Client.ajouterReservation()` gère sa propre liste de réservations
- **Low Coupling** : injection des DAOs via constructeur
- **Information Expert** : `Params.getDateReservationAsLocalDateTime()` convertit la date

---

### Builder — `ProduitBuilder`
Package : `org.sebsy.builder`

Construction d'un objet `Produit` (alimentaire) de manière fluent :

```java
Produit produit = new ProduitBuilder()
    .nom("Coca-Cola")
    .grade("A")
    .categorie("Boisson")
    .marque("Coca-Cola Company")
    .ajouterIngredient("Eau", 330.0)
    .ajouterAllergene("Caféine", 5.0)
    .build();
```

---

### Factory Method — `ElementFactory`
Package : `org.sebsy.factory`

Factory qui retourne une instance d'`Element` (`Ingredient`, `Additif`, `Allergene`) en fonction d'un `TypeElement` (énumération) :

```java
Element e = factory.creerElement(TypeElement.INGREDIENT, "Farine", 200.0, Unite.MILLI_GRAMMES);
```

---

### Strategy — Algorithmes de tri
Package : `org.sebsy.strategy`

Refactoring d'une méthode `exec` contenant 3 algorithmes de tri dans un seul bloc `if/else` vers le pattern Strategy :
- `BubbleSort`
- `InsertionSort`
- `SelectionSort`

```java
tri.exec(TypeTri.BUBBLE_SORT, array);
```

---

### Composite — Organisation hiérarchique
Package : `org.sebsy.composite`

Représentation d'une hiérarchie de services et d'employés. `Service` peut contenir des `Employe` ou d'autres `Service`, et `calculerSalaire()` remonte récursivement toute la hiérarchie.

---

### State — Cycle de vie d'une `Commande`
Package : `org.sebsy.state`

Gestion des états d'une commande via le pattern State :

| État | `ajouterProduit` | `payer` | `livrer` | `annuler` |
|---|---|---|---|---|
| CREATION | ✅ | ✅ | ❌ | ✅ |
| PAIEMENT | ❌ | ❌ | ✅ | ✅ |
| EN_LIVRAISON | ❌ | ❌ | ❌ | ❌ |
| ANNULEE | ❌ | ❌ | ❌ | ❌ |
