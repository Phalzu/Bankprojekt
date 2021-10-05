package main.java.bankprojekt.verarbeitung.abstractFactory;

import main.java.bankprojekt.verarbeitung.*;
import org.mockito.Mockito;

/**
 * Erzeugt Konto-Mock-Objekte
 * @author Dorothea Hubrich
 * überarbeitet von Philipp Zurek bzgl. erstellen -> 3. Argument hinzugefügt
 */
public class MockFabrik extends KontoFabrik {
    private Konto neu;
    public Kontoart art = Kontoart.GIROKONTO;

    @Override
    public Konto erstellen(Kontoart art, Kunde inhaber, long kontonr){
        switch (art) {
            case SPARBUCH:
                neu = Mockito.mock(Sparbuch.class);
                break;
            case GIROKONTO:
                neu = Mockito.mock(Girokonto.class);
                break;
            default:
                throw new IllegalArgumentException();
        }
        Mockito.when(neu.getInhaber()).thenReturn(inhaber);
        Mockito.when(neu.getKontonummer()).thenReturn(kontonr);
        return neu;
    }

    /**
     * liefert das zuletzt von erstellen erzeugte Konto-Mock
     * @return
     */
    public Konto getZuletztErstelltesKonto(){
        return neu;
    }
}
