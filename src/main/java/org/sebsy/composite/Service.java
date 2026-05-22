package org.sebsy.composite;

import java.util.ArrayList;
import java.util.List;

public class Service implements IElement {

    private String nom;
    private List<IElement> elements = new ArrayList<>();

    public Service(String nom) {
        this.nom = nom;
    }

    public void ajouter(IElement element) {
        elements.add(element);
    }

    @Override
    public double calculerSalaire() {
        return elements.stream()
                .mapToDouble(IElement::calculerSalaire)
                .sum();
    }

    public String getNom() { return nom; }
}
