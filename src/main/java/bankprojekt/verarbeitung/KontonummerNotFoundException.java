package main.java.bankprojekt.verarbeitung;

/**
 * tritt bei einem Zugriffsversuch auf ein nicht vorhandenes Konto üb die Kontonummer auf
 */
public class KontonummerNotFoundException extends Exception {

        /**
         * Zugriff auf nicht vorhandenes Konto mit der angegebenen Kontonummer
         *
         * @param kontonummer die Nummer des nicht existierenden Kontos, auf das zugegriffen werden soll
         *
         */
        public KontonummerNotFoundException(long kontonummer) {
            super("Zugriff über nicht vorhandene Kontonummer " + kontonummer);
        }

}
