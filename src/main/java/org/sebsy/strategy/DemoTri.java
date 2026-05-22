package org.sebsy.strategy;

import java.util.List;

public class DemoTri {

    public static void main(String[] args) {
        Tri tri = new Tri();

        System.out.println("Avec le Bubble Sort:");

        Integer[] array1 = {12, -5, 7, 0, 8, 4, -3, 9, 15};
        tri.exec(TypeTri.BUBBLE_SORT, array1);
        List.of(array1).forEach(i -> System.out.print(i + " "));

        System.out.println("\nAvec l'insertion Sort:");

        Integer[] array2 = {12, -5, 7, 0, 8, 4, -3, 9, 15};
        tri.exec(TypeTri.INSERTION_SORT, array2);
        List.of(array2).forEach(i -> System.out.print(i + " "));

        System.out.println("\nAvec le selection Sort:");

        Integer[] array3 = {12, -5, 7, 0, 8, 4, -3, 9, 15};
        tri.exec(TypeTri.SELECTION_SORT, array3);
        List.of(array3).forEach(i -> System.out.print(i + " "));


    }

}
