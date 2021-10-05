package main.java.bankprojekt.verarbeitung.abstractFactory;

import main.java.bankprojekt.verarbeitung.*;

public class Bankkonten extends KontoFabrik{

    @Override
    public Konto erstellen(Kontoart art, Kunde besitzer, long kontonr){
        Konto neu;

        switch (art) {
            case SPARBUCH -> {
                neu = new Sparbuch(besitzer, kontonr);
                return neu;
            }
            case GIROKONTO -> {
                neu = new Girokonto(besitzer, kontonr, 500);
                return neu;
            }
            default -> throw new IllegalArgumentException();
        } // End of switch-case: Auswählen der Art des Kontos
    } // End of erstellen

} // End of Bankkonten
