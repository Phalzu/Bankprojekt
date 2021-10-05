package main.java.bankprojekt.verarbeitung;

/**
 * Eine Liste der zur Verfügung stehenden Währungen mit festen Euro-Wechselkurs
 */
public enum Waehrung {
    /** Euro */
    EUR(1),
    /** Bulgarische Leva */
    BGN(1.95583),
    /** Litauische Litas */
    LTL(3.4528),
    /** Konvertible Mark */
    KM(1.95583);

    private double kursZuEuro;

    private Waehrung(double kursZuEuro) {
        this.kursZuEuro = kursZuEuro;
    } // End of Konstruktor

    /**
     * Berechnet Euro -> andere Waehrung
     *
     * @param betrag des umzuwechselndes Geldes
     * @return der umgerechnete Betrag
     */
    public double euroInWaehrungUmrechnen(double betrag) {
        return betrag * kursZuEuro;
    } // End of euroInWaehrungUmrechnen

    /**
     * Berechnet Waehrung -> zu Euro
     *
     * @param betrag der Geldbetrag
     * @return der umgerechnete Geldbetrag
     */
    public double waehrungInEuroUmrechnen(double betrag) {
        return betrag / kursZuEuro;
    } // End of waehrungInEuroUmrechnen

    /**
     * Ein kleiner Wrapper fuer euroInWaehrungUmrechnen, waehrungInEuroUmrechnen, der es einfacher gestaltet
     * einen Betrag in eine beliebige Waehrung umzurechnen.
     * Mach die beiden Methoden zwar obsolet, sie werden jedoch von der Aufgarbe gefordert.
     *
     * @param betrag der umgerechnet werden soll
     * @param von   Waehrung von der umgerechnet werden soll
     * @param zu    Waehrung in die umgerechnet werden soll
     * @return das Ergebnis
     */
    public static double waehrungZuWaehrung(double betrag, Waehrung von, Waehrung zu) {
        return zu.euroInWaehrungUmrechnen(von.waehrungInEuroUmrechnen(betrag));
    } // End of waehrungZuWaehrung
} // End of Waehrung
