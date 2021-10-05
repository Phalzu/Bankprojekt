package test.java;
/*
import main.java.bankprojekt.verarbeitung.GesperrtException;
import main.java.bankprojekt.verarbeitung.Girokonto;
import main.java.bankprojekt.verarbeitung.Kunde;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.fail;
*/

//Achtung: Nicht vollständig...

/**
 * Testklasse zu Girokonto
 *
 * @author Philipp Zurek
 * @version 1.0-SNAPSHOT
 */
class GirokontoTest {
/*
    Girokonto gk;
    Kunde k = new Kunde("Dorothea", "Hubrich", "zuhause", LocalDate.parse("1976-07-13"));
    long nummer = 17;
    double dispo = 1000;

    @Test
    void konstruktorTest() {
        gk = new Girokonto();
        Assertions.assertEquals(gk.getKontostand(), 0);
        Assertions.assertEquals(gk.getKontostandFormatiert(), " 0,00 Euro");
        Assertions.assertFalse(gk.isGesperrt());
        Assertions.assertEquals(gk.getGesperrtText(), "");
        Assertions.assertEquals(gk.getInhaber(), Kunde.MUSTERMANN);
    }

    private void assertEquals(double kontostand, int i) {
    }

    @Test
    void konstruktorMitParameternTest() {
        gk = new Girokonto(k, nummer, dispo);
        Assertions.assertEquals(gk.getKontostand(), 0);
        Assertions.assertEquals(gk.getKontostandFormatiert(), " 0,00 Euro");
        Assertions.assertFalse(gk.isGesperrt());
        Assertions.assertEquals(gk.getGesperrtText(), "");
        Assertions.assertEquals(gk.getInhaber(), k);
        Assertions.assertEquals(gk.getDispo(), dispo);
        Assertions.assertEquals(gk.getKontonummer(), nummer);
        Assertions.assertEquals(gk.getKontonummerFormatiert(), " 17");
    }

    @Test
    void einzahlenTest() {
        gk = new Girokonto();
        gk.einzahlen(100);
        Assertions.assertEquals(gk.getKontostand(), 100);
        Assertions.assertEquals(gk.getKontostandFormatiert(), " 100,00 Euro");
        Assertions.assertFalse(gk.isGesperrt());
        Assertions.assertEquals(gk.getGesperrtText(), "");
        Assertions.assertEquals(gk.getInhaber(), Kunde.MUSTERMANN);
    }

    @Test
    void einzahlenNegativTest() {
        gk = new Girokonto();
        try {
            gk.einzahlen(-100);
            fail("Keine Exception!");
        } catch (IllegalArgumentException e) {
        }
        Assertions.assertEquals(gk.getKontostand(), 0);
        Assertions.assertEquals(gk.getKontostandFormatiert(), " 0,00 Euro");
        Assertions.assertFalse(gk.isGesperrt());
        Assertions.assertEquals(gk.getGesperrtText(), "");
        Assertions.assertEquals(gk.getInhaber(), Kunde.MUSTERMANN);
    }

    @Test
    void einzahlenNaNTest() {
        gk = new Girokonto();
        try {
            gk.einzahlen(Double.NaN);
            fail("Keine Exception!");
        } catch (IllegalArgumentException e) {
        }
        Assertions.assertEquals(gk.getKontostand(), 0);
        Assertions.assertEquals(gk.getKontostandFormatiert(), " 0,00 Euro");
        Assertions.assertFalse(gk.isGesperrt());
        Assertions.assertEquals(gk.getGesperrtText(), "");
        Assertions.assertEquals(gk.getInhaber(), Kunde.MUSTERMANN);
    }

    @Test
    void abhebenimKontostandTest() throws GesperrtException {
        boolean geklappt;
        gk = new Girokonto();
        gk.einzahlen(100);
        geklappt = gk.abheben(50);
        Assertions.assertTrue(geklappt);
        Assertions.assertEquals(gk.getKontostand(), 50);
        Assertions.assertEquals(gk.getKontostandFormatiert(), " 50,00 Euro");
        Assertions.assertFalse(gk.isGesperrt());
        Assertions.assertEquals(gk.getGesperrtText(), "");
        Assertions.assertEquals(gk.getInhaber(), Kunde.MUSTERMANN);
    }

    @Test
    void abhebenGenauKontostandTest() throws GesperrtException {
        boolean geklappt;
        gk = new Girokonto();
        gk.einzahlen(100);
        geklappt = gk.abheben(100);
        Assertions.assertTrue(geklappt);
        Assertions.assertEquals(gk.getKontostand(), 0);
        Assertions.assertEquals(gk.getKontostandFormatiert(), " 0,00 Euro");
        Assertions.assertFalse(gk.isGesperrt());
        Assertions.assertEquals(gk.getGesperrtText(), "");
        Assertions.assertEquals(gk.getInhaber(), Kunde.MUSTERMANN);
    }

    @Test
    void abhebeninEinzelschrittenBisLeerTest() throws GesperrtException {
        boolean geklappt;
        gk = new Girokonto();
        gk.einzahlen(100);
        geklappt = gk.abheben(50);
        Assertions.assertTrue(geklappt);
        geklappt = gk.abheben(50);
        Assertions.assertTrue(geklappt);
        Assertions.assertEquals(gk.getKontostand(), 0);
        Assertions.assertEquals(gk.getKontostandFormatiert(), " 0,00 Euro");
        Assertions.assertFalse(gk.isGesperrt());
        Assertions.assertEquals(gk.getGesperrtText(), "");
        Assertions.assertEquals(gk.getInhaber(), Kunde.MUSTERMANN);
    }

    @Test
    void abhebenInDenDispoTest() throws GesperrtException {
        boolean geklappt;
        gk = new Girokonto(k, nummer, dispo);
        gk.einzahlen(100);
        geklappt = gk.abheben(50 + dispo);
        Assertions.assertTrue(geklappt);
        Assertions.assertEquals(gk.getKontostand(), 50 - dispo);
        Assertions.assertFalse(gk.isGesperrt());
        Assertions.assertEquals(gk.getGesperrtText(), "");
        Assertions.assertEquals(gk.getInhaber(), k);
        Assertions.assertEquals(gk.getDispo(), dispo);
    }

    @Test
    void abhebenImDispoTest() throws GesperrtException {
        boolean geklappt;
        gk = new Girokonto(k, nummer, dispo);
        gk.einzahlen(100);
        geklappt = gk.abheben(50 + dispo);
        Assertions.assertTrue(geklappt);
        geklappt = gk.abheben(20);
        Assertions.assertTrue(geklappt);
        Assertions.assertEquals(gk.getKontostand(), 30 - dispo);
        Assertions.assertFalse(gk.isGesperrt());
        Assertions.assertEquals(gk.getGesperrtText(), "");
        Assertions.assertEquals(gk.getInhaber(), k);
        Assertions.assertEquals(gk.getDispo(), dispo);
    }

    @Test
    void abhebenGenauImDispoTest() throws GesperrtException {
        boolean geklappt;
        gk = new Girokonto(k, nummer, dispo);
        gk.einzahlen(100);
        geklappt = gk.abheben(100 + dispo);
        Assertions.assertTrue(geklappt);
        Assertions.assertEquals(gk.getKontostand(), -dispo);
        Assertions.assertFalse(gk.isGesperrt());
        Assertions.assertEquals(gk.getGesperrtText(), "");
        Assertions.assertEquals(gk.getInhaber(), k);
        Assertions.assertEquals(gk.getDispo(), dispo);
    }

    @Test
    void abhebenUeberDispoTest() throws GesperrtException {
        boolean geklappt;
        gk = new Girokonto(k, nummer, dispo);
        gk.einzahlen(100);
        geklappt = gk.abheben(500 + dispo);
        Assertions.assertFalse(geklappt);
        Assertions.assertEquals(gk.getKontostand(), 100);
        Assertions.assertFalse(gk.isGesperrt());
        Assertions.assertEquals(gk.getGesperrtText(), "");
        Assertions.assertEquals(gk.getInhaber(), k);
        Assertions.assertEquals(gk.getDispo(), dispo);
    }
     */
}