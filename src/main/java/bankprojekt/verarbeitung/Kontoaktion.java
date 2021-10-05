/*
 * Beschreibung - Gibt Kontoauszüge aus
 * Autor - Philipp Zurek (Matrikel-Nr. 573243)
 * Datum - 10.05.2021
 * Version - v1.0
 */
package main.java.bankprojekt.verarbeitung;

import java.time.LocalDate;

public class Kontoaktion {
    private String beschreibung;
    private double betrag;
    private LocalDate datum;

    /**
     * Erstellt ein Objekt Kontoaktion, der einen typischen Eintrag eines Kontoauszuges darstellt
     * In diesem Konstruktor wird nur eine Beschreibung gefordert.
     *
     * @param beschreibung - Die Beschreibung, was mit dieser Kontoaktion passiert ist
     */
    public Kontoaktion(String beschreibung){
        this.beschreibung = beschreibung;
        this.betrag = 0.0;
        this.datum = LocalDate.now();
    } // End of Kontoaktion

    /**
     * Erstellt ein Objekt Kontoaktion, der einen typischen Eintrag eines Kontoauszuges darstellt
     * In diesem Konstruktor wird eine Beschreibung und ein Betrag gefordert.
     *
     * @param beschreibung - Die Beschreibung, was mit dieser Kontoaktion passiert ist
     * @param betrag - Ein Betrag, der mit dieser Kontoaktion verbunden ist
     */
    public Kontoaktion(String beschreibung, double betrag){
        this.beschreibung = beschreibung;
        this.betrag = betrag;
        this.datum = LocalDate.now();
    } // End of Kontoaktion

} // End of Kontoaktion
