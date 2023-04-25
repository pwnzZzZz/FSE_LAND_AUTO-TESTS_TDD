package at.itkolleg.ase.tdd.kino;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class AdvancedTests {
    private Map<Character,Integer> map;
    private KinoVerwaltung kinoVerwaltung;
    private KinoSaal ks1;
    private LocalDate dateNow;
    private Vorstellung vorstellung1;
    private Vorstellung vorstellung2;
    private Vorstellung vorstellung3;



    @BeforeEach
    public void setUp() {
        map = new HashMap<>();
        map.put('A',10);
        map.put('B',10);
        map.put('C',15);

        ks1 = new KinoSaal("Kino ++", map);
        dateNow = LocalDate.now();
        kinoVerwaltung = new KinoVerwaltung();
        vorstellung1 = new Vorstellung(ks1, Zeitfenster.ABEND, dateNow, "Super Mario Bros", 15.99f);
        vorstellung2 = new Vorstellung(ks1, Zeitfenster.ABEND, dateNow, "John Wick", 15.99f);
        vorstellung3 = new Vorstellung(ks1, Zeitfenster.NACHMITTAG, dateNow, "The Ring", 15.99f);
        kinoVerwaltung.einplanenVorstellung(vorstellung1);
    }

    /**
     * 1. Schreiben Sie einen Test, der validiert, dass das Anlegen einer Vorstellung
     * korrekt funktioniert. Der Test sollte eine fachliche Bezeichnung haben und die
     * Assertions sollten bei Validierungsfehler eine Hinweistext liefern.
     */
    @Test
    void vorstellungAnlegen() {
        Vorstellung vorstellung = new Vorstellung(ks1, Zeitfenster.ABEND, dateNow, "Super Mario Bros", 15.99f);
        assertNotNull(vorstellung, "Die Vorstellung darf nicht NULL sein!");
        assertEquals(ks1, vorstellung.getSaal(), "Der Kinosaal hat den falschen Namen.");
        assertEquals(Zeitfenster.ABEND, vorstellung.getZeitfenster(), "Falsche Zeitangabe!.");
        assertEquals(dateNow, vorstellung.getDatum(), "Falsches Datum!");
    }


    /**
     * 2. Schreiben Sie einen Test, der validiert, dass das Einplanen mehrerer
     * Vorstellungen korrekt funktioniert. Stellen Sie zudem sicher, dass beim möglichen
     * Auftreten eines Fehlers trotzdem alle Validierungen ausgeführt werden.
     */
    @Test
    public void testMehrereVorstellungenEinplanen() {
        kinoVerwaltung.einplanenVorstellung(vorstellung3);

        List<Vorstellung> vorstellungen = kinoVerwaltung.getVorstellungen();
        assertEquals(2, vorstellungen.size());
        assertTrue(vorstellungen.contains(vorstellung1));
        assertTrue(vorstellungen.contains(vorstellung2));
    }

    /**
     * 3. Schreiben Sie einen Test, der sicherstellt, dass ein Fehler geworfen wird,
     * wenn eine Veranstaltung doppelt eingeplant wird.
     */
    @Test
    void testDoppelteVorstellungEinplanen() {
        assertThrows(IllegalArgumentException.class, () -> kinoVerwaltung.einplanenVorstellung(vorstellung1),
                "Vorstellung bereits eingeplant!.");
    }

    /**
     * 4. Schreiben Sie einen parametrisierten Test, der mehrere Ticketkäufe mit
     * unterschiedlichen Parametern überprüft.
     */
    private static Stream<Arguments> ticketKaufParameter() {
        return Stream.of(
                Arguments.of('A', 1, 25.0f),
                Arguments.of('A', 5, 22.0f),
                Arguments.of('B', 8, 20.0f),
                Arguments.of('B', 4, 20.5f)
        );
    }
    @ParameterizedTest
    @MethodSource("ticketKaufParameter")
    void testKaufeTicketMitVerschiedenenParametern(char reihe, int platz, float geld) {
        Ticket ticket = vorstellung1.kaufeTicket(reihe, platz, geld);

        assertNotNull(ticket, "Das gekaufte Ticket sollte nicht null sein");
        assertEquals(reihe, ticket.getReihe(), "Die Reihen sollten übereinstimmen");
        assertEquals(platz, ticket.getPlatz(), "Die Plätze sollten übereinstimmen");
    }

    @TestFactory
    Stream<DynamicTest> testTicketKaufMitZufaelligenWerten() {
        Random rand = new Random();
        int numTests = 100;
        List<DynamicTest> tests = new ArrayList<>();

        for (int i = 0; i < numTests; i++) {
            char reihe = (char) (rand.nextInt(5) + 'A');
            int platz = rand.nextInt(10) + 1;
            float geld = rand.nextFloat() * (30 - 5) + 5;

            String testName = String.format("Test #%d mit Parametern: Vorstellung %s, Reihe %s, Platz %d, Geld %.2f", i + 1, vorstellung1.toString(), reihe, platz, geld);

            DynamicTest test = DynamicTest.dynamicTest(testName, () -> {
                try {
                    Ticket ticket = kinoVerwaltung.kaufeTicket(vorstellung1, reihe, platz, geld);
                    assertNotNull(ticket, "Das Ticket darf nicht NULL sein!");
                    assertEquals(reihe, ticket.getReihe());
                    assertEquals(platz, ticket.getPlatz());
                } catch (IllegalArgumentException e) {
                    assertTrue(e.getMessage().equals("Nicht ausreichend Geld.")
                                    || e.getMessage().startsWith("Der Platz "),
                            "Unerwartete IllegalArgumentException: " + e.getMessage());
                } catch (IllegalStateException e) {
                    assertTrue(e.getMessage().startsWith("Der Platz "),
                            "Unerwartete IllegalStateException: " + e.getMessage());
                }
            });

            tests.add(test);
        }

        return tests.stream();
    }

    }
