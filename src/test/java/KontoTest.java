package test.java;

import main.java.bankprojekt.verarbeitung.GesperrtException;
import main.java.bankprojekt.verarbeitung.Girokonto;
import main.java.bankprojekt.verarbeitung.Kunde;
import main.java.bankprojekt.verarbeitung.observer.Konsolenausgabe;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 * Diese Klasse dient zum Simulieren einer Datenbankanfrage für die Klasse Konto
 */
class KontoTest {
    private static Statement stmt;
    private static ResultSet rs;
    private static ResultSet rsFalse;
    private static Girokonto testGirokonto;

    @BeforeEach
    public void setUp() throws SQLException {

        stmt = Mockito.mock(java.sql.Statement.class);
        rs = Mockito.mock(java.sql.ResultSet.class);
        rsFalse = Mockito.mock(java.sql.ResultSet.class);

        Mockito.when(stmt.executeQuery("false")).thenReturn(rsFalse);
        Mockito.when(stmt.executeQuery(Mockito.anyString())).thenReturn(rs);

        Mockito.when(rs.next()).thenReturn(true);
        Mockito.when(rsFalse.next()).thenReturn(false);

        testGirokonto = new Girokonto();
    } // End of setUp

    @AfterAll
    public static void tearDown(){
        stmt = null;
        rs = null;
        rsFalse = null;
        testGirokonto = null;
    } // End of tearDown

    @Test
    public void erfolgreicheQueryTest() throws Exception{
        // SetUp
        rs = stmt.executeQuery(" ");

        // Exercise
        testGirokonto.speichern(stmt);

        // Verify
        Mockito.verify(rs).updateRow();

        // TearDown
        tearDown();

    } // End of DatenbankTest

    @Test
    public void unerfolgreicheQueryTest() throws Exception{
        // SetUp
        rsFalse = stmt.executeQuery("false");

        // Exercise
        testGirokonto.speichern(stmt);

        // Verify
        Mockito.verify(rsFalse, Mockito.never()).insertRow();

        // TearDown
        tearDown();

    } // End of DatenbankTest

    @Test
    public void girokontoStalkingTest() throws GesperrtException {
        Konsolenausgabe kontoBeobachter = new Konsolenausgabe();

        testGirokonto.addPropertyChangeListener(kontoBeobachter);

        System.out.println("\n\t-----START---TEST-OBSERVER-KONSOLE-OUTPUT-----");

        testGirokonto.einzahlen(100); // Blick in die Konsole

        try {
            testGirokonto.abheben(100); // Blick wieder in die Konsole
        } catch (GesperrtException ignored){
        }

        testGirokonto.setInhaber(new Kunde("Guenther", "Oettinger", "Deutschland um die Ecke", LocalDate.now()));

        System.out.println("\n\t-----END---TEST-OBSERVER-KONSOLE-OUTPUT-----\n");

        // TearDown
        tearDown();
    } // End of girokontoStalkingTest


} // End of SQLTest
