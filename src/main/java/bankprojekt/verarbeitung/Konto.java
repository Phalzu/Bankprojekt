package main.java.bankprojekt.verarbeitung;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * stellt ein allgemeines Konto dar
 */
public abstract class Konto implements Comparable<Konto> {


	private PropertyChangeSupport support = new PropertyChangeSupport(this);

	/**
	 * Aus der Dokumentation:
	 *
	 * Add a PropertyChangeListener to the listener
	 * list. The listener is registered for all
	 * properties. The same listener object may
	 * be added more than once, and will be called
	 * as many times as it is added. If listener
	 * is null, no exception is thrown and no action
	 * is taken.
	 *
	 * @param pcl - The PropertyChangeListener to be added
	 */
	public void addPropertyChangeListener(PropertyChangeListener pcl){
		support.addPropertyChangeListener(pcl);
	} // End of addPropertyChangeListener

	/**
	 * Aus der Dokumentation:
	 *
	 * Remove a PropertyChangeListener from the
	 * listener list. This removes a PropertyChangeListener
	 * that was registered for all properties. If listener
	 * was added more than once to the same event source,
	 * it will be notified one less time after being removed.
	 * If listener is null, or was never added, no exception
	 * is thrown and no action is taken.
	 *
	 * @param pcl – The PropertyChangeListener to be removed
	 */
	public void removePropertyChangeListener(PropertyChangeListener pcl){
		support.removePropertyChangeListener(pcl);
	} // End of removePropertyChangeListener

	/**
	 * Alle Kontoaktionen die auf diesem Konto stattfinden werden in dieser Eigenschaft gespeichert
	 */
	private List<Kontoaktion> kontoaktion = new LinkedList<>();

	/** 
	 * der Kontoinhaber
	 */
	private Kunde inhaber;

	/**
	 * die Kontonummer
	 */
	private final long nummer;

	/**
	 * der aktuelle Kontostand
	 */
	private double kontostand;

	/**
	 * der aktuelle Kontostand als private ReadOnlyDoubleProperty
	 */
	private ReadOnlyDoubleProperty kontostandAsProperty = new SimpleDoubleProperty(this.kontostand);

	/**
	 * der aktuelle Kontostand als Property
	 *
	 * @return kontostand als ReadOnlyDoubleProperty
	 */
	public ReadOnlyDoubleProperty kontostandProperty()
	{
		return this.kontostandAsProperty;
	}

	/**
	 * setzt den aktuellen Kontostand
	 * @param kontostand neuer Kontostand
	 */
	protected void setKontostand(double kontostand) {
		// Teilt das setzen des Kontostandes den Listenern mit, bevor dieser Verändert wird
		support.firePropertyChange("Kontostand gesetzt", this.kontostand, kontostand);

		this.kontostand = kontostand;
	}

	/**
	 * Wenn das Konto gesperrt ist (gesperrt = true), können keine Aktionen daran mehr vorgenommen werden,
	 * die zum Schaden des Kontoinhabers wären (abheben, Inhaberwechsel)
	 */
	private boolean gesperrt;

	/**
	 * der aktuelle gesperrt-Zustand als private ReadOnlyBooleanProperty
	 */
	private ReadOnlyBooleanProperty gesperrtAsProperty = new SimpleBooleanProperty(this.gesperrt);

	/**
	 * der aktuelle gesperrt-Zustand als Property
	 *
	 * @return gesperrt als ReadOnlyBooleanProperty
	 */
	public ReadOnlyBooleanProperty gesperrtProperty()
	{
		return this.gesperrtAsProperty;
	}

	/**
	 * Eine Property die verwaltet ob der Kontostand dieses Konto-Objekts
	 * positiv oder negativ ist.
	 */
	private ReadOnlyBooleanProperty kontostandPositivAsProperty = new SimpleBooleanProperty();

	/**
	 * Gibt eine Property die verwaltet ob der Kontostand dieses
	 * Konto-Objekts positiv oder negativ ist zurück.
	 *
	 * @return kontostandPositivAsProperty als ReadOnlyBooleanProperty
	 */
	public ReadOnlyBooleanProperty kontostandPositivProperty()
	{
		// TODO: Sorge dafür das Wert aktuell bleibt...
		return this.kontostandPositivAsProperty;
	}

	/**
	 * die Waehrung, die fuer dieses Konto verwendet wird
	 */
	private Waehrung waehrung = Waehrung.EUR;

	/**
	 * Liefert die vom Konto genutzte Waehrung zurueck
	 *
	 * @return die verwendete Waehrung
	 */
	public Waehrung getAktuelleWaehrung() {
		return waehrung;
	} // End of getAktuelleWaehrung

	/**
	 * Setzt die beiden Eigenschaften kontoinhaber und kontonummer auf die angegebenen Werte,
	 * der anfängliche Kontostand wird auf 0 gesetzt.
	 *
	 * @param inhaber der Inhaber
	 * @param kontonummer die gewünschte Kontonummer
	 * @throws IllegalArgumentException wenn der Inhaber null
	 */
	public Konto(Kunde inhaber, long kontonummer) {
		if(inhaber == null)
			throw new IllegalArgumentException("Inhaber darf nicht null sein!");
		this.inhaber = inhaber;
		this.nummer = kontonummer;
		this.kontostand = 0;
		this.gesperrt = false;
		kontoaktion.add(new Kontoaktion("Das Konto für" + inhaber.toString() +  " wurde mit der Kontonummer " +
				kontonummer + "dem Kontostand " + kontostand + " Euro eröffnet."));
	}
	
	/**
	 * setzt alle Eigenschaften des Kontos auf Standardwerte
	 */
	public Konto() {
		this(Kunde.MUSTERMANN, 1234567);
		kontoaktion.add(new Kontoaktion("Das Konto für" + inhaber.toString() +  " wurde mit der Kontonummer " +
				"1234567 dem Kontostand " + kontostand + " Euro eröffnet."));
	}

	/**
	 * liefert den Kontoinhaber zurück
	 * @return   der Inhaber
	 */
	public final Kunde getInhaber() {
		return this.inhaber;
	}
	
	/**
	 * setzt den Kontoinhaber
	 * @param kinh   neuer Kontoinhaber
	 * @throws GesperrtException wenn das Konto gesperrt ist
	 * @throws IllegalArgumentException wenn kinh null ist
	 */
	public final void setInhaber(Kunde kinh) throws GesperrtException{
		if (kinh == null)
			throw new IllegalArgumentException("Der Inhaber darf nicht null sein!");
		if(this.gesperrt)
			throw new GesperrtException(this.nummer);

		// Teilt das Setzen des Kontostandes den Listenern mit, bevor dieser Verändert wird
		support.firePropertyChange("Setze Inhaber des Kontos", this.inhaber, kinh);

		this.inhaber = kinh;

		kontoaktion.add(new Kontoaktion("Der Kontoinhaber hat sich auf " + kinh.toString() + " geändert."));
	}

	/**
	 * liefert den aktuellen Kontostand
	 * @return   double
	 */
	public final double getKontostand() {
		return kontostand;
	}

	/**
	 * liefert die Kontonummer zurück
	 * @return   long
	 */
	public final long getKontonummer() {
		return nummer;
	}

	/**
	 * liefert zurück, ob das Konto gesperrt ist oder nicht
	 * @return true, wenn das Konto gesperrt ist
	 */
	public final boolean isGesperrt() {   //Getter, aber eben für booleschen Wert
		return gesperrt;
	}
	
	/**
	 * Erhöht den Kontostand um den eingezahlten Betrag.
	 *
	 * @param betrag double
	 * @throws IllegalArgumentException wenn der betrag negativ ist 
	 */
	public void einzahlen(double betrag) {
		if (betrag < 0 || Double.isNaN(betrag)) {
			throw new IllegalArgumentException("Falscher Betrag");
		}

		setKontostand(getKontostand() + betrag);
		kontoaktion.add(new Kontoaktion(("Es wurden " + betrag + " Euro eingezahlt."), betrag));
	}

	/**
	 * Erhöht den Kontostand um den eingezahlten Betrag.
	 *
	 * @param betrag in double
	 * @param waehrung die eingezahlt werden soll
	 * @throws IllegalArgumentException wenn der betrag negativ ist
	 */
	public void einzahlen(double betrag, Waehrung waehrung) throws IllegalArgumentException {
		betrag = Waehrung.waehrungZuWaehrung(betrag, waehrung, this.waehrung);
		einzahlen(betrag);
	} // End of einzahlen

	/**
	 * Gibt eine Zeichenkettendarstellung der Kontodaten zurück.
	 */
	@Override
	public String toString() {
		String ausgabe;
		ausgabe = "Kontonummer: " + this.getKontonummerFormatiert()
				+ System.getProperty("line.separator");
		ausgabe += "Inhaber: " + this.inhaber;
		ausgabe += "Aktueller Kontostand: " + this.kontostand + " " + this.waehrung.name();
		ausgabe += this.getGesperrtText() + System.getProperty("line.separator");
		return ausgabe;
	}
	
	/**
	 * dient rein didaktischen Zwecken, gehört hier eigentlich nicht her
	 */
	public void ausgeben()
	{
		System.out.println(this.toString());
	}

	/**
	 * Mit dieser Methode wird der geforderte Betrag vom Konto abgehoben,
	 * wenn es nicht gesperrt ist.
	 *
	 * @param betrag in double
	 * @return true, wenn die Abhebung geklappt hat,
	 *         false, wenn sie abgelehnt wurde
	 * @throws GesperrtException, wenn das Konto gesperrt ist
	 * @throws IllegalArgumentException, wenn der betrag negativ ist
	 */
	public boolean abheben(double betrag) throws GesperrtException {
		// Auf Gesperrt Prüfen...
		if (betrag < 0 || Double.isNaN(betrag)) {
			throw new IllegalArgumentException("Betrag ungültig");
		}
		if(this.isGesperrt())
			throw new GesperrtException(this.getKontonummer());

		if(!this.pruefAbhebenBedingung(betrag))
			return false;

		this.setKontostand(this.getKontostand() - betrag);

		kontoaktion.add(new Kontoaktion(("Es wird versucht " + betrag + " Euro abzuheben."), betrag));

		nachAbhebung(betrag);
		return true;
	} // End of abheben

	/**
	 * Mit dieser Methode wird der geforderte Betrag vom Konto abgehoben,
	 * wenn es nicht gesperrt ist.
	 *
	 * @param betrag in double
	 * @param waehrung die verwendet werden soll
	 * @return true, wenn die Abhebung geklappt hat,
	 *         false, wenn sie abgelehnt wurde
	 * @throws GesperrtException, wenn das Konto gesperrt ist
	 * @throws IllegalArgumentException, wenn der betrag negativ ist
	 */
	public boolean abheben(double betrag, Waehrung waehrung) throws GesperrtException {
		betrag = Waehrung.waehrungZuWaehrung(betrag, waehrung, this.waehrung);

		kontoaktion.add(new Kontoaktion(("Es wird versucht " + betrag + " Euro abzuheben."), betrag));
		return abheben(betrag);
	} // End of abheben

	/**
	 * Stellt Konditionen für die abheben()-Methode dar, die von erbenden Klassen genutzt
	 * werden kann.
	 */
	protected abstract boolean pruefAbhebenBedingung(double betrag);

	/**
	 * Führt Aktionen nach Abhebung durch
	 *
	 * @param betrag double
	 */
	 protected void nachAbhebung(double betrag){}

	/**
	 * sperrt das Konto, Aktionen zum Schaden des Benutzers sind nicht mehr möglich.
	 */
	public final void sperren() {
		this.gesperrt = true;
	}

	/**
	 * entsperrt das Konto, alle Kontoaktionen sind wieder möglich.
	 */
	public final void entsperren() {
		this.gesperrt = false;
	}

	
	/**
	 * liefert eine String-Ausgabe, wenn das Konto gesperrt ist
	 * @return "GESPERRT", wenn das Konto gesperrt ist, ansonsten ""
	 */
	public final String getGesperrtText()
	{
		if (this.gesperrt)
		{
			return "GESPERRT";
		}
		else
		{
			return "";
		}
	}
	
	/**
	 * liefert die ordentlich formatierte Kontonummer
	 * @return auf 10 Stellen formatierte Kontonummer
	 */
	public String getKontonummerFormatiert()
	{
		return String.format("%10d", this.nummer);
	}
	
	/**
	 * liefert den ordentlich formatierten Kontostand
	 * @return formatierter Kontostand mit 2 Nachkommastellen und Währungssymbol
	 */
	public String getKontostandFormatiert(){
		return String.format("%10.2f Euro" + waehrung.name(), this.getKontostand());
	} // End of getKontostandFormatiert

	/**
	 * Ändert die Währung mit dem das Konto arbeitet
	 *
	 * @param neu die neue Waehrung
	 */
	public void waehrungswechsel(Waehrung neu){
		kontostand = Waehrung.waehrungZuWaehrung(kontostand, waehrung, neu);

		// Teilt das Ändern der Kontowährung den Listenern mit, bevor dieser Verändert wird
		support.firePropertyChange("Verändere Währung", this.waehrung, neu);

		this.waehrung = neu;
		kontoaktion.add(new Kontoaktion("Die Währung wurde auf " + neu + " gewechselt."));
	} // End of waehrungswechsel

	/**
	 * Vergleich von this mit other; Zwei Konten gelten als gleich,
	 * wen sie die gleiche Kontonummer haben
	 * @param other das Vergleichskonto
	 * @return true, wenn beide Konten die gleiche Nummer haben
	 */
	@Override
	public boolean equals(Object other)
	{
		if(this == other)
			return true;
		if(other == null)
			return false;
		if(this.getClass() != other.getClass())
			return false;
		if(this.nummer == ((Konto)other).nummer)
			return true;
		else
			return false;
	}

	/**
	 * Gibt einen Kontoauszug für dieses Konto als String zurück
	 *
	 * @return Einen Kontoauszug dieses Kontos als String
	 */
	public String getKontoauszug(){
		return kontoaktion.toString();
	} // End of getKontoauszug

	/**
	 * Löscht alle Einträge von vor dem angegebenen Datum
	 * @param vor - Das Datum vor dem alle älteren Einträge gelöscht werden sollen
	 */
	public void alteEintraegeLoeschen(LocalDate vor){
		kontoaktion.removeIf(t -> t.toString().startsWith(vor.toString()));
	} // End of alteEintraegeLoeschen

	@Override
	public int hashCode()
	{
		return 31 + (int) (this.nummer ^ (this.nummer >>> 32));
	}

	@Override
	public int compareTo(Konto other)
	{
		if(other.getKontonummer() > this.getKontonummer())
			return -1;
		if(other.getKontonummer() < this.getKontonummer())
			return 1;
		return 0;
	}

	/**
	 * "Aufgabe der speichern-Methode ist es, die Daten des Kontos in einer Datenbank zu speichern.
	 *  Da wir hier keine Datenbank zur Verfügung haben, kann man sie natürlich nicht einfach durch
	 *  ausprobieren testen. Also wird Mockito gebraucht." - Doro
	 *
	 * @param stmt das Statement eines DMS
	 * @throws Exception, wenn das Statement fehlerhaft war
	 */
	public void speichern(Statement stmt) throws Exception
	{
		ResultSet rs=null ;
		try
		{
			rs = stmt.executeQuery("SELECT * FROM konto WHERE kontonr="+this.nummer);
			if (rs.next())
			{
				rs.updateDouble("kontostand", this.kontostand);
				rs.updateBoolean("gesperrt", this.gesperrt);
				rs.updateRow();
			}
			else
			{
				rs.moveToInsertRow();
				rs.updateDouble("kontostand", this.kontostand);
				rs.updateBoolean("gesperrt", this.gesperrt);
				rs.updateLong("kontonr", this.nummer);
				int nummer = this.inhaber.speichern();
				//in Kunde einfach eine speichern-Methode erzeugen, die nichts tut. Um sie soll es hier nicht gehen.
				rs.updateInt("kunde", nummer);
				rs.insertRow();
			}

		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			try
			{
				if (rs != null) rs.close();
			} catch (Exception ex)
			{
				throw ex;
			}
		}
	} // End of speichern

}
