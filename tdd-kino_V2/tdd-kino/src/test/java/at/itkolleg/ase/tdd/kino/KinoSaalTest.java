package at.itkolleg.ase.tdd.kino;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class KinoSaalTest {

    private KinoSaal kinoSaal;

    @BeforeEach
    void setUp() {
        Map<Character, Integer> reihen = new HashMap<>();
        reihen.put('A', 10);
        reihen.put('B', 20);
        reihen.put('C', 15);
        kinoSaal = new KinoSaal("Saal 1", reihen);
    }

    @Test
    void testGetName() {
        Assertions.assertEquals("Saal 1", kinoSaal.getName());
    }

    @Test
    void testPruefePlatz() {
        // Test mit gültigem Platz
        Assertions.assertTrue(kinoSaal.pruefePlatz('B', 15));

        // Test mit ungültiger Reihe
        Assertions.assertFalse(kinoSaal.pruefePlatz('D', 5));

        // Test mit ungültigem Platz
        Assertions.assertFalse(kinoSaal.pruefePlatz('C', 16));

        // Test mit Platz 0
        Assertions.assertFalse(kinoSaal.pruefePlatz('A', 0));
    }

    @Test
    void testEquals() {
        KinoSaal kinoSaal2 = new KinoSaal("Saal 1", null);
        KinoSaal kinoSaal3 = new KinoSaal("Saal 2", null);

        // Test mit gleichem Namen
        Assertions.assertEquals(kinoSaal, kinoSaal2);

        // Test mit unterschiedlichem Namen
        Assertions.assertNotEquals(kinoSaal, kinoSaal3);

        // Test mit einem Objekt, das keine Instanz von KinoSaal ist
        Assertions.assertNotEquals("Saal 1", kinoSaal);
    }
}
