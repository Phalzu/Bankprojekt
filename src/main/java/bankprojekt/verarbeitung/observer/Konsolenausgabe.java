package main.java.bankprojekt.verarbeitung.observer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Konsolenausgabe implements PropertyChangeListener {

    /**
     *  Standartkonstruktor
     */
    public Konsolenausgabe(){
    } // End of Konsolenausgabe

    @Override
    public void propertyChange(PropertyChangeEvent evt){
        System.out.println("\n" + evt.getPropertyName() + " von " + evt.getOldValue().toString() + " auf " + evt.getNewValue().toString());

    } // End of propertyChange

} // End of Konsolenausgabe
