package main.java.bankprojekt.verarbeitung.abstractFactory;

import main.java.bankprojekt.verarbeitung.Konto;
import main.java.bankprojekt.verarbeitung.Kontoart;
import main.java.bankprojekt.verarbeitung.Kunde;

/**
 * Dient dem erstellen neuer Konten
 */
public abstract class KontoFabrik {

    /**
     * Erstellt ein neues Konto der angegebenen Art, mit dem angegebenen Besitzer und einer Kontonummer
     *
     * @param art des Kontos
     * @param besitzer des Kontos (Kundenobjekt)
     * @param kontonr stellt die Kontonummer des neuen Kontos dar
     * @return ein Kontoobjekt
     * @throws IllegalArgumentException, wenn besitzer null oder Art des Kontos inexistent
     */
    public abstract Konto erstellen(Kontoart art, Kunde besitzer, long kontonr);

} // End of KontoFabrik
