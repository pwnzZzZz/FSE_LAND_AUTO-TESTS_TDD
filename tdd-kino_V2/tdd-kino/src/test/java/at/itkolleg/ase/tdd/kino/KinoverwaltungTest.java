package at.itkolleg.ase.tdd.kino;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;

public class KinoverwaltungTest {
    private Map<Character,Integer> map;
    private KinoVerwaltung kinoVerwaltung;
    private KinoSaal ks1;
    private LocalDate dateNow;
    private Vorstellung vorstellung;

    @BeforeEach
    public void setUp() {
        map = new HashMap<>();
        map.put('A',10);
        map.put('B',10);
        map.put('C',15);

        ks1 = new KinoSaal("Kino ++", map);
        dateNow = LocalDate.now();
        kinoVerwaltung = new KinoVerwaltung();
        vorstellung = new Vorstellung(ks1, Zeitfenster.ABEND, dateNow, "Super Mario Bros", 15.99f);
        kinoVerwaltung.einplanenVorstellung(vorstellung);
    }

    @Test
    public void testEinplanenVorstellung() {
        assertTrue(kinoVerwaltung.getVorstellungen().contains(vorstellung));
    }

    @Test
    public void testEinplanenVorstellungBereitsEingeplant() {
        assertThrows(IllegalArgumentException.class, () -> kinoVerwaltung.einplanenVorstellung(vorstellung));
    }

    @Test
    public void testGetVorstellungen() {
        assertTrue(kinoVerwaltung.getVorstellungen().size() > 0);
    }

    @Test
    public void testKaufeTicket() {
        Ticket ticket = kinoVerwaltung.kaufeTicket(vorstellung, 'A', 5, 20.0f);
        assertNotNull(ticket);
        assertEquals("Kino ++", ticket.getSaal());
        assertEquals(Zeitfenster.ABEND, ticket.getZeitfenster());
        assertEquals(dateNow, ticket.getDatum());
    }


    @Test
    void testVorstellungEinplanenBereitsEingeplant() {
        assertThrows(IllegalArgumentException.class, () -> kinoVerwaltung.einplanenVorstellung(vorstellung));
    }


    @Test
    void testKaufeTicketNotEnoughMoney() {
        assertThrows(IllegalArgumentException.class, () -> kinoVerwaltung.kaufeTicket(vorstellung, 'A', 5, 5.0f));
    }

    @Test
    void testKaufeTicketInvalidSeat() {
        assertThrows(IllegalArgumentException.class, () -> kinoVerwaltung.kaufeTicket(vorstellung,'C', 5, 15.0f));
    }

}
