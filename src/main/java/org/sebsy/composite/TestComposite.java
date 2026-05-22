package org.sebsy.composite;

public class TestComposite {

    public static void main(String[] args) {

        // Service Big Data
        Service bigData = new Service("Big Data");
        bigData.ajouter(new Employe("GABORIT", "Audrey", 7500));
        bigData.ajouter(new Employe("LAINE", "Evan", 3500));

        // Service Java Dev
        Service javaDev = new Service("Java Dev");
        javaDev.ajouter(new Employe("BOUNMY", "Souvanny", 7500));
        javaDev.ajouter(new Employe("LAFORE", "Léo", 3500));

        // Service DSIN (service racine)
        Service dsin = new Service("DSIN");
        dsin.ajouter(new Employe("MALALATIANA", "Erika", 10000));
        dsin.ajouter(new Employe("NICE", "Brice", 8000));
        dsin.ajouter(bigData);
        dsin.ajouter(javaDev);

        // Affichage des salaires
        System.out.println("Salaire total Big Data  : " + bigData.calculerSalaire() + "€");
        System.out.println("Salaire total Java Dev  : " + javaDev.calculerSalaire() + "€");
        System.out.println("Salaire total DSIN      : " + dsin.calculerSalaire() + "€");
    }
}
