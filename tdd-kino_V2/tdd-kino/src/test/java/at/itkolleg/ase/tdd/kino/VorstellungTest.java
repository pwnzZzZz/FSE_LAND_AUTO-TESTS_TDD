package at.itkolleg.ase.tdd.kino;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class VorstellungTest {

    private Vorstellung vorstellung;
    private KinoSaal saal;

    @BeforeEach
    void setUp() {
        Map<Character, Integer> reihen = new HashMap<>();
        reihen.put('A', 10);
        reihen.put('B', 20);
        reihen.put('C', 15);
        saal = new KinoSaal("Saal 1", reihen);
        vorstellung = new Vorstellung(saal, Zeitfenster.NACHMITTAG, LocalDate.now(), "Film1", 10.0f);
    }

    @Test
    void testGetFilm() {
        assertEquals("Film1", vorstellung.getFilm());
    }

    @Test
    void testGetSaal() {
        assertEquals(saal, vorstellung.getSaal());
    }

    @Test
    void testGetZeitfenster() {
        assertEquals(Zeitfenster.NACHMITTAG, vorstellung.getZeitfenster());
    }

    @Test
    void testGetDatum() {
        assertEquals(LocalDate.now(), vorstellung.getDatum());
    }

    @Test
    void testKaufeTicket() {
        // Test mit gültigen Parametern
        Ticket ticket1 = vorstellung.kaufeTicket('B', 15, 15.0f);
        assertNotNull(ticket1);
        assertEquals('B', ticket1.getReihe());
        assertEquals(15, ticket1.getPlatz());

        // Test mit zu wenig Geld
        assertThrows(IllegalArgumentException.class, () -> vorstellung.kaufeTicket('A', 5, 5.0f));

        // Test mit ungültigem Platz
        assertThrows(IllegalArgumentException.class, () -> vorstellung.kaufeTicket('D', 10, 20.0f));

        // Test mit bereits verkauftem Ticket
        vorstellung.kaufeTicket('A', 1, 15.0f);
        assertThrows(IllegalStateException.class, () -> vorstellung.kaufeTicket('A', 1, 20.0f));
    }

    @Test
    void testEquals() {
        Map<Character, Integer> reihen2 = new HashMap<>();
        reihen2.put('A', 10);
        reihen2.put('B', 20);
        reihen2.put('C', 15);
        KinoSaal saal2 = new KinoSaal("Saal 1", reihen2);
        Vorstellung vorstellung2 = new Vorstellung(saal2, Zeitfenster.NACHMITTAG, LocalDate.now(), "Film1", 10.0f);

        // Test mit gleichem Saal, Zeitfenster und Datum
        assertEquals(vorstellung, vorstellung2);

        // Test mit unterschiedlichem Saal
        Vorstellung vorstellung3 = new Vorstellung(new KinoSaal("Saal 2", null), Zeitfenster.NACHMITTAG, LocalDate.now(), "Film1", 10.0f);
        assertNotEquals(vorstellung, vorstellung3);

        // Test mit unterschiedlichem Zeitfenster
        Vorstellung vorstellung4 = new Vorstellung(saal, Zeitfenster.ABEND, LocalDate.now(), "Film1", 10.0f);
    }
}