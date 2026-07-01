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
# → doit afficher "Tomcat started on port 8080" sans erreur
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
- [ ] **Objectif 3** — Optimisation (cache, Virtual Threads, performances)
- [ ] **Objectif 4** — API REST (`/products`, `/ingredients`, `/allergens`, `/additives`)

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
