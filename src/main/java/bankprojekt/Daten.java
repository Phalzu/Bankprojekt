package main.java.bankprojekt;

import java.io.Serializable;

/**
 * Daten mit einer ID
 * @author Doro
 *
 * @param <IDTyp> Datentyp der ID
 */
public interface Daten<IDTyp> extends Serializable
{
	/**
	 * liefert die ID des Datenpaketes
	 * @return
	 */
	public IDTyp getId();
}