package org.sebsy.factory;

import junit.framework.TestCase;
import org.junit.Test;

public class FactoryTest extends TestCase {

    private ElementFactory factory = new ElementFactory();

    @Test
    public void testCreerIngredient() {
        Element element = factory.creerElement(TypeElement.INGREDIENT, "Farine", 200.0, Unite.MILLI_GRAMMES);

        assertNotNull(element);
        assertTrue(element instanceof Ingredient);
        assertEquals("Farine", element.getNom());
        assertEquals(200.0, element.getValeur());
        assertEquals(Unite.MILLI_GRAMMES, element.getUnite());
    }

    @Test
    public void testCreerAdditif() {
        Element element = factory.creerElement(TypeElement.ADDITIF, "E150d", 1.5, Unite.MICRO_GRAMMES);

        assertNotNull(element);
        assertTrue(element instanceof Additif);
        assertEquals("E150d", element.getNom());
        assertEquals(Unite.MICRO_GRAMMES, element.getUnite());
    }

    @Test
    public void testCreerAllergene() {
        Element element = factory.creerElement(TypeElement.ALLERGENE, "Gluten", 50.0, Unite.MILLI_GRAMMES);

        assertNotNull(element);
        assertTrue(element instanceof Allergene);
        assertEquals("Gluten", element.getNom());
        assertEquals(50.0, element.getValeur());
    }
}
