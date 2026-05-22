package org.sebsy.strategy;

public class StrategyFactory {

    public Strategy getStrategy(TypeTri typeTri) {
        switch (typeTri) {
            case BUBBLE_SORT:
                return new BubbleSort();
            case INSERTION_SORT:
                return new InsertionSort();
            case SELECTION_SORT:
                return new SelectionSort();
            default:
                throw new IllegalArgumentException("Type de tri inconnu : " + typeTri);
        }
    }
}
