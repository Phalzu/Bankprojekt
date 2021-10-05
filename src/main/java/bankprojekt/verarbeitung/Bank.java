package main.java.bankprojekt.verarbeitung;

import main.java.bankprojekt.verarbeitung.abstractFactory.Bankkonten;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Diese Klasse repraesentiert eine Bank
 *
 */
public class Bank implements Cloneable{

    /**
     * Versionsnummer zum Verhindern einer InvalidClassException bei erneutem Einlesen eines Klones dieser Klasse im weiteren Verlauf...
     */
    private static final long serialVersionUID = 66606660;

    /**
     * erste verfuegbare Kontonummer
     */
    private static final long FIRST_KONTONUMMER = 0L;

    /**
     * letzte verfuegbare Kontonummer
     */
    private static final long LAST_KONTONUMMER = 10L;

    /**
     * Bankleitzahl der Bank
     */
    private long bankleitzahl;

    /**
     * alle Konten in einer TreeMap (immer sortiert -> lastKey() moeglich)
     */
    private TreeMap<Long, Konto> konten;

    /**
     * Erstellt eine neue Bank
     *
     * @param bankleitzahl
     * Bankleitzahl der Bank
     */
    public Bank(long bankleitzahl) {
        this.bankleitzahl = bankleitzahl;
        this.konten = new TreeMap<>();
    }

    /**
     * Erstellt ein Konto für den angegebenen Kunden
     *
     * @param fabrik zum Erstellen der einzelnen Konten
     * @param besitzer das Objekt des Kunden
     * @param art des Kontos (GIROKONTO, SPARBUCH..)
     * @return Kontonummer long
     * @throws IllegalArgumentException, wenn besitzer == null
     */
    public long kontoErstellen(Bankkonten fabrik, Kunde besitzer, Kontoart art){
        Konto k = fabrik.erstellen(art, besitzer, getNeueKontonummer());
        konten.put(k.getKontonummer(), k);
        return getNeueKontonummer();
    } // End of kontoErstellen

    /**
     * gibt die Bankleitzahl der Bank zurueck
     *
     * @return Bankleitzahl
     */
    public long getBankleitzahl() {
        return bankleitzahl;
    }

    /**
     * gibt eine neue Kontonummer zurueck
     *
     * @return neue Kontonummer
     */
    private long getNeueKontonummer() {
        if (konten.isEmpty())
            return FIRST_KONTONUMMER;
        else
            return konten.lastKey() + 1;
    }

    /**
     * liefert eine Auflistung von Kontoinformationen aller Konten (mindestens
     * Kontonummer und Kontostand) mittels der Stream API
     *
     * @return Auflistung der Kontoinformationen aller Konten
     */
    public String getAlleKonten() {
        BinaryOperator<String> reduceToString = (a, b) -> a + b + System.getProperty("line.separator");
        return konten.values()
                .stream()
                .map(Konto::toString)
                .reduce("", reduceToString);
    }

    /**
     * liefert eine Liste aller gueltigen Kontonummern in der Bank
     *
     * @return Liste aller gueltigen Kontonummern
     */
    public List<Long> getAlleKontonummern() {
        return new LinkedList(konten.keySet());
    }

    /**
     * Testet ob eine Kontonummer in der Kontenliste konten existiert, falls nciht
     * wird eine KontonummerNotFoundException geworfen
     *
     * @param kontonummer
     * Kontonummer die getestet werden soll
     * @throws KontonummerNotFoundException
     * wenn Kontonummer nicht gefunden wurde
     */
    private void testIfKontonummerExists(long kontonummer) throws KontonummerNotFoundException {
        if (!konten.containsKey(kontonummer)) {
            throw new KontonummerNotFoundException(kontonummer);
        }
    }

    /**
    * hebt den Betrag vom Konto mit der Nummer von ab und gibt zurueck, ob die
    * Abhebung geklappt hat
    *
    * @param vonKontonummer
    * Kontonummer von dem der Betrag abgehoben werden soll
    * @param betrag
    * Betrag der abgehoben werden soll
    * @return true, wenn abheben geklappt hat KontonummerNotFoundException * wenn
    * Kontonummer nicht gefunden wurde
    * @throws GesperrtException
    * wenn Konto gesperrt ist
    *
    * @throws KontonummerNotFoundException
    * wenn Kontonummer nicht gefunden wurde
    * @throws IllegalArgumentException, wenn betrag ungültig
    *
    */
    public boolean geldAbheben(long vonKontonummer, double betrag)
        throws KontonummerNotFoundException, GesperrtException {
    testIfKontonummerExists(vonKontonummer);
    Konto k = konten.get(vonKontonummer);
    return k.abheben(betrag);
    }

    /**
     * zahlt den angegebenen Betrag auf das Konto mit der Nummer auf ein
     *
     * @param aufKontonummer
     * Kontonummer auf der das Geld eingezahlt werden soll
     * @param betrag
     * Betrag der eingezahlt werden soll
     * @throws KontonummerNotFoundException
     * wenn Kontonummer nicht gefunden wurde
     * @throws IllegalArgumentException
     * wenn Betrag negativ ist
     * @throws IllegalArgumentException, wenn betrag ungültig
     */
    public void geldEinzahlen(long aufKontonummer, double betrag) throws KontonummerNotFoundException {
        testIfKontonummerExists(aufKontonummer);
        Konto k = konten.get(aufKontonummer);
        k.einzahlen(betrag);
    }

    /**
     * Loescht ein Konto aus der Kontenliste konten
     *
     * @param kontonummer
     * Kontonummer des Kontos das geloescht werden soll
     * @return true, wenn Konto gefunden und geloescht wurde
     */
    public boolean kontoLoeschen(long kontonummer) {
        if (!konten.containsKey(kontonummer)) {
            return false;
        }
        konten.remove(kontonummer);
        return true;
    }

    /**
     * liefert den Kontostand des Kontos mit der angegebenen nummer zurueck.
     *
     * @param kontonummer
     * Kontonummer
     * @return Kontostand
     * @throws KontonummerNotFoundException
     * wenn Kontonummer nicht gefunden wurde
     */
    public double getKontostand(long kontonummer) throws KontonummerNotFoundException {
        testIfKontonummerExists(kontonummer);
        Konto k = konten.get(kontonummer);
        return k.getKontostand();
    }

    /**
     * Ueberweist den genannten Betrag vom Girokonto mit der Nummer vonKontonummer
     * zum Girokonto mit der Nummer nachKontonummer und gibt zurueck, ob die
     * Ueberweisung geklappt hat (nur bankinterne Ueberweisungen!)
     *
     * @param vonKontonummer
     * Kontonummer von dem der Betrag ueberwiesen werden soll
     * @param nachKontonummer
     * Kontonummer auf den der Betrag ueberwiesen werden soll
     * @param betrag
     * betrag der ueberwiesen werden soll
     * @param verwendungszweck
     * Verwendungszweck der Ueberweisung
     * @return true, wenn Ueberweisung geklappt hat
     * @throws GesperrtException
     * wenn eines der Konten gesperrt ist
     * @throws IllegalArgumentException
     * wenn Betrag ungültig ist oder der Verwednungszweck null
     */
    public boolean geldUeberweisen(long vonKontonummer, long nachKontonummer, double betrag, String verwendungszweck) throws GesperrtException {
        if (!konten.containsKey(vonKontonummer) || !konten.containsKey(nachKontonummer)) {
            return false;
        }

        // hole die Konten aus der Kontenliste
        Konto tmpKontoVon = konten.get(vonKontonummer);
        Konto tmpKontoNach = konten.get(nachKontonummer);

        // pruefe ob es sich bei beiden Konten um überweisungsfähige // Konten handelt
        if (!(tmpKontoVon instanceof Ueberweisungsfaehig) || !(tmpKontoNach instanceof Ueberweisungsfaehig)) {
            return false;
        }

        Ueberweisungsfaehig kontoVon = (Ueberweisungsfaehig) tmpKontoVon;
        Ueberweisungsfaehig kontoNach = (Ueberweisungsfaehig) tmpKontoNach;

        if (!kontoVon.ueberweisungAbsenden(betrag, tmpKontoNach.getInhaber().getName(), nachKontonummer, getBankleitzahl(),
                verwendungszweck)) {
            return false;
        }

        // ueberweisen hat funktioniert, fuehre ueberweisungEmpfangen beim Empfaenger
        // aus und gib true zurueck
        kontoNach.ueberweisungEmpfangen(betrag, tmpKontoVon.getInhaber().getName(), vonKontonummer, getBankleitzahl(),
                verwendungszweck);
        return true;
    }

    /**
     * Diese Methode gibt zurueck ob eine Kontonummer gesperrt ist
     *
     * @param kontonummer
     * Kontonummer die geprueft werden soll
     * @return true wenn Konto gesperrt ist
     * @throws KontonummerNotFoundException
     * wenn Kontonummer nicht gefunden wurde
     */
    public boolean isGesperrt(long kontonummer) throws KontonummerNotFoundException {
        testIfKontonummerExists(kontonummer);
        Konto k = konten.get(kontonummer);
        return k.isGesperrt();
    }

    /**
     * Die Methode sperrt alle Konten, deren Kontostand im Minus ist
     */
    public void pleiteGeierSperren() {
        Predicate<Konto> filterKontostandImMinus = konto -> konto.getKontostand() < 0;
        Consumer<Konto> actionKontoSperren = konto -> konto.sperren();
        konten
                .values()
                .stream()
                .filter(filterKontostandImMinus)
                .forEach(actionKontoSperren);
    }

    /**
     * Die Methode liefert eine Liste aller Kunden, die auf einem Konto einen
     * Kontostand haben, der mindestens minimum betraegt
     *
     * @param minimum
     * minimaler Kontostand
     * @return Liste aller Kunden mit Kontostand minimum
     */
    public List<Kunde> getKundenMitVollemKonto(double minimum) {
        Predicate<Konto> filterKontostandMindestensMinimum =
                konto -> konto.getKontostand() >= minimum;

        List<Kunde> kundenliste = konten
                .values()
                .stream()
                .filter(filterKontostandMindestensMinimum)
                .map(Konto::getInhaber)
                .collect(Collectors.toList());
        return kundenliste;
    }

    /**
     * Die Methode liefert die Namen und Geburtstage aller Kunden der Bank. Doppelte
     * Namen sollen dabei aussortiert werden. Die Liste ist nach dem Geburtsdatum
     * sortiert
     *
     * @return Kundengeburtstage als String
     */
    public String getKundengeburtstage() {
        String reduceStartStr = "Geburtstagliste" +
                System.getProperty("line.separator");

        BinaryOperator<String> reduceToString =
                (alt, neu) -> alt + neu +
                        System.getProperty("line.separator");

        Comparator<Kunde> compare =
                (kunde1, kunde2) ->
                        kunde1.getGeburtstag().compareTo(kunde2.getGeburtstag());

        Function<Kunde, String> mapKundeToString =
                kunde -> kunde.getName() + " (" + kunde.getGeburtstag() + ")";

        String geburtstagsliste = konten
                .values()
                .stream()
                .map(Konto::getInhaber)
                .distinct() // nur verschiedene Kunden
                .sorted(compare)
                .map(mapKundeToString)
                .reduce(reduceStartStr, reduceToString);
        return geburtstagsliste;
    }

    /**
     * liefert eine Liste aller freien Kontonummern, die im vergebenen Bereich
     * liegen
     *
     * @return Liste aller freien Kontonummerns
     */
    public List<Long> getKontonummernLuecken() {
        List<Long> aktuelleKontonummern = getAlleKontonummern();
        LongPredicate filterOutVorhandeneKontonummern =
                nummer -> !aktuelleKontonummern.contains(nummer);

        List<Long> freieKontonummern = LongStream
                .range(FIRST_KONTONUMMER, LAST_KONTONUMMER)
                .filter(filterOutVorhandeneKontonummern)
                .boxed()
                .collect(Collectors.toList());
        return freieKontonummern;
    }

    /**
     * Die Methode liefert eine Liste aller Kunden, deren Gesamteinlage auf all
     * ihren Konten (das kann mehr als eins sein) mehr als minimum betraegt.
     *
     * @param minimum minimale Geldeinlage Gesamt auf allen Konten
     * @return Liste aller Kunden
     */
    public List<Kunde> getAlleReichenKunden(double minimum) {
        BinaryOperator<Double> mergeFunction =
                (kontostandAlt, kontostandNeu) -> kontostandAlt += kontostandNeu;
        Predicate<Map.Entry<Kunde, Double>> filterMinimum =
                entry -> entry.getValue() > minimum;
        Map<Kunde, Double> kundenMap = konten
                .values()
                .stream()
                .collect(Collectors.toMap(Konto::getInhaber,
                        Konto::getKontostand, mergeFunction));
                List<Kunde> alleReichenKunden = kundenMap
                .entrySet()
                .stream()
                .filter(filterMinimum)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        return alleReichenKunden;
    }

    /**
     * Klont alle Attribute und Objekte der Klasse Bank und gibt sie zurück
     *
     * @return alle Attribute und Objekte der Klasse Bank
     * @throws CloneNotSupportedException, wenn das klonen misslingt
     */
    @Override
    public Bank clone() throws CloneNotSupportedException {
        Bank clone = (Bank) super.clone(); // Dummy-Klon

        // Aufgabe: Schreiben Sie das zu kopierende Objekt per Serialisierung in einen ByteArrayOut-putStream hinein.
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        // Aufgabe: Wandeln Sie diesen in ein Byte-Array (byte[]) um.
        byte[] kontenClone = konten.toString().getBytes();

        try {
            buffer.write(kontenClone);
            buffer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Aufgabe: Öffnen Sie für dieses Byte-Array einen ByteArrayInputStream...
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer.toByteArray());

        try {
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Aufgabe: ...und lesen Sie daraus per Serialisierung die Kopie.
        try {
            ObjectInput input = new ObjectInputStream(bais);
            clone = (Bank) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return clone;
    } // End of clone


}