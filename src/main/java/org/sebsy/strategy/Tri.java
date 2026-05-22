package org.sebsy.strategy;

public class Tri {

    private StrategyFactory strategyFactory = new StrategyFactory();

    public void exec(TypeTri typeTri, Integer[] arr) {
        Strategy strategy = strategyFactory.getStrategy(typeTri);
        strategy.trier(arr);
    }
}
