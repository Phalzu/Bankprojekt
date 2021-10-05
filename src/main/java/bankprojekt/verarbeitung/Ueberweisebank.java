package main.java.bankprojekt.verarbeitung;

import main.java.bankprojekt.server.Bankserver;
import main.java.bankprojekt.server.Ueberweisungsdaten;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;


/**
 * eine Bank mit ihren Konten
 */
public class Ueberweisebank implements Serializable{
	/** 
	 * Liste aller Konten der Bank
	 */
	private HashMap<Long, Konto> kontenliste;

	/**
	 * Bankleitzahl
	 */
	private long bankleitzahl;

	/**
	 * die letzte bereits vergebene Kontonummer
	 */
	private long letzteNummer;
	
	private transient Ueberweisungsmanager manager;
		//manager wird nicht mit serialisiert
	
	private void readObject(ObjectInputStream s) 
			throws IOException, ClassNotFoundException
	{
		s.defaultReadObject();
		this.manager = new Ueberweisungsmanager();
	}

	
	/**
	 * Erstellt eine leere Bank ohne Konten
	 * als erste Kontonummer für ein neu zu eröffnendes Konto wird 1001 vergeben werden.
	 * @param blz long
	 */
	public Ueberweisebank(long blz) {
		this.bankleitzahl = blz;
		letzteNummer = 1000;
		kontenliste = new HashMap<Long, Konto>();
		manager = new Ueberweisungsmanager();
	}
	
	/**
	 * legt ein neues Girokonto für den übergebenen Kunden an und gibt die neue
	 * Kontonummer zurück
	 * @param inhaber Kunde
	 * @throws IllegalArgumentException wenn der inhaber null ist 
	 * @return long
	 */
	public long kontoAnlegen(Kunde inhaber) {
		long knr = letzteNummer + 1;
		letzteNummer++;
		Konto k = null;
		k = new Girokonto(inhaber, knr, 500);
		kontenliste.put(knr, k);
		return knr;
	}

	/**
	 * gibt eine Zeichenkettendarstellung aller Konten der Bank zurück
	 * @return String
	 */
	public String getKontoliste() {
		String ausgabe = "";
		if (kontenliste.isEmpty()) {
			return "Keine Konten vorhanden.";
		} else {
			for(Konto k: this.kontenliste.values()) {
				ausgabe += k.toString() + System.getProperty("line.separator");
			}
			return ausgabe;
		}
	}

	/**
	 * liefert die Bankleitzahl
	 * @return   int
	 */
	public long getBankleitzahl() {
		return bankleitzahl;
	}
	
	/**
	 * zahlt den angegebenen Betrag auf das Konto ein. Falls die Kontonummer
	 * nicht existiert, wird false zurückgegeben
	 * @param ktonr long
	 * @param betrag double
	 * @return boolean
	 */
	public void einzahlung(long ktonr, double betrag) {
			Konto k = this.kontenliste.get(ktonr);
			if (k != null) {
					k.einzahlen(betrag);
			}
	}	
	
	/**
	 * empfängt eine Überweisung von einem Konto einer anderen Bank
	 * für das angegebene Konto
	 * @param ktonrNach long
	 * @param ktonrVon long
	 * @param blzVon long
	 * @param absender String
	 * @param betrag double
	 * @param verwendung String
	 * 
	 * @return ob der Empfang geklappt hat oder nicht wegen nicht existierender Kontonummer
	 */
	public boolean ueberweisungsEmpfang(long ktonrNach, long ktonrVon, long blzVon, 
			String absender, double betrag, String verwendung) {
		try
		{
			Konto k = this.kontenliste.get(ktonrNach);
			if (k != null && k instanceof Girokonto) {
				Girokonto gk = (Girokonto) k;
				gk.ueberweisungEmpfangen(betrag, absender, ktonrVon, blzVon,
							verwendung);
				return true;
			}
			else
				return false;
		} catch (Exception e)
		{
			return false;
		}
	}
	
	/**
	 * führt die Überweisung von vonKontonr nach nachKontonr durch, wenn
	 * beide bei dieser Bank sind
	 * @param vonKontonr Absendekonto
	 * @param nachKontonr Empfängerkonto
	 * @param betrag zu überweisender Betrag
	 * @param verwendungszweck Verwendungszweck
	 * @throws RuntimeException wenn eine der Kontonummern 
	 *                          nicht in dieser Bank vorhanden ist
	 * @return true, wenn die Überweisung ausgeführt werden konnte,
	 *         false, wenn nicht (z.B. wegen zu geringer Deckung
	 *         oder gesperrtem Konto...)
	 */
	private boolean geldUeberweisen(long vonKontonr, long nachKontonr, double betrag, String verwendungszweck)
	{
		Konto kVon = this.kontenliste.get(vonKontonr);
		Konto kNach = this.kontenliste.get(nachKontonr);
		if (kVon == null) throw new RuntimeException();
		if (kNach == null) throw new RuntimeException();
		if (!(kVon instanceof Girokonto)) throw new RuntimeException();
		if (!(kNach instanceof Girokonto)) throw new RuntimeException();
		Girokonto gkVon = (Girokonto) kVon;
		Girokonto gkNach = (Girokonto) kNach;
		boolean geklappt;
			try {
				geklappt = gkVon.ueberweisungAbsenden(betrag, gkNach.getInhaber()
						.getName(), nachKontonr, this.bankleitzahl, verwendungszweck);
			} catch (GesperrtException e) {
				geklappt = false;
			}
		if (geklappt) {
				gkNach.ueberweisungEmpfangen(betrag, gkVon
						.getInhaber().getName(), vonKontonr,
						this.bankleitzahl, verwendungszweck);
			return true;
		}
		return false;
	}
	
	/**
	 * üeberweist den angegebenen Betrag von einem Konto auf ein anderes
	 * @param ktonrVon long
	 * @param ktonrNach long
	 * @param blzNach long
	 * @param empfaenger String
	 * @param betrag double
	 * @param verwendung String
	 * @return boolean true, wenn die Überweisung erfolgreich durchgeführt wurde
	 * @throws GesperrtException wenn das Absenderkonto gesperrt ist
	 */
	public boolean ueberweisung(long ktonrVon, long ktonrNach, long blzNach, String empfaenger, 
			double betrag, String verwendung) throws GesperrtException {
		try
		{
			if (this.getBankleitzahl() == blzNach)
			{
				return this.geldUeberweisen(ktonrVon, ktonrNach, betrag, verwendung);
			}
			else
			{
				Konto kVon = this.kontenliste.get(ktonrVon);
				if (kVon == null)
					return false;
				if (!(kVon instanceof Girokonto))
					return false;
				Girokonto gkVon = (Girokonto) kVon;
				boolean geklappt;
					geklappt = gkVon.ueberweisungAbsenden(betrag, empfaenger, ktonrNach,
							blzNach, verwendung);
				if (geklappt) {
					boolean ausgefuehrt;
					ausgefuehrt = manager.ueberweisungAnFremdeBank(ktonrVon, gkVon.getInhaber().getName(), ktonrNach, blzNach, empfaenger, betrag, verwendung);
					if (ausgefuehrt)
						return true;
					else {
						gkVon.einzahlen(betrag);
						return false;
					}
				}
				return false;
			}
		} catch (Exception e)
		{
			return false;
		}
	}

	/**
	 * Klasse zum Managen der Verbindung zum Server,
	 * der die Überweisungen von einer Bank zur anderen
	 * leitet
	 * @author Doro
	 *
	 */
	private class Ueberweisungsmanager {
		/**
		 * Verbindung zum Bankserver
		 */
		private Socket verbindung; //nicht serialisierbar!!!
		
		/**
		 * nimmt die Kommunikation mit einer anderen Bank auf, um dort den Empfang der
		 * Überweisung zu veranlassen
		 * @param ktonrVon
		 * @param absender
		 * @param ktonrNach
		 * @param blzNach
		 * @param empfaenger
		 * @param betrag
		 * @param verwendung
		 * @return ob die Überweisung empfangen wurde
		 */
		private boolean ueberweisungAnFremdeBank(long ktonrVon, String absender, long ktonrNach, 
				long blzNach, String empfaenger, double betrag, String verwendung)
		{
			try
			{
				Ueberweisungsdaten udaten;
				udaten = new Ueberweisungsdaten(ktonrNach, blzNach, verwendung,
						betrag, ktonrVon, absender,
						Ueberweisebank.this.getBankleitzahl());
				boolean ausgefuehrt;
				ausgefuehrt = this.ueberweisungSenden(udaten);
				return ausgefuehrt;
			} catch (Exception e)
			{
				return false;
			}
		}
		
		/**
		 * sendet die übergebene Überweisung an den Bankserver und wartet auf seine Bestätigung
		 * @param udaten
		 * @return ob die Überweisung ausgeführt werden konnte oder nicht
		 * @throws Exception
		 */
		private boolean ueberweisungSenden(Ueberweisungsdaten udaten)
		throws Exception {
			InetAddress ip;
			ip = this.verbindung.getInetAddress();
			Socket so;
			so = new Socket(ip, Bankserver.UEBERWEISUNGSPORT);
			OutputStream out = so.getOutputStream();
			ObjectOutputStream oout = new ObjectOutputStream(out);
			oout.writeObject(udaten);
			oout.flush();
			DataInputStream din = new DataInputStream(so.getInputStream());
			boolean ausgefuehrt = din.readBoolean();
			so.close();
			return ausgefuehrt;
		}
		
		public void wartenAufUeberweisung() throws Exception {
			InputStream in;
			in = verbindung.getInputStream();
			ObjectInputStream oin = new ObjectInputStream(in);
			Ueberweisungsdaten udaten = (Ueberweisungsdaten) oin.readObject();
			long ktonr = udaten.getKontonummer();
			long ktonrVon = udaten.getKontonummerVon();
			long blzVon = udaten.getBankleitzahlVon();
			String nameVon = udaten.getAbsender();
			double betrag = udaten.getBetrag();
			String verwendung = udaten.getVerwendungszweck();
			boolean geklappt = Ueberweisebank.this.ueberweisungsEmpfang(ktonr, ktonrVon, blzVon, nameVon, betrag,
					verwendung);
			DataOutputStream dout = new DataOutputStream(verbindung.getOutputStream());
			dout.writeBoolean(geklappt);
			dout.flush();
		}
		
		/**
		 * meldet sich vom Bankserver ab
		 * @throws Exception
		 */
		public void verbindungKappen() throws Exception {
			InetAddress ip;
			ip = this.verbindung.getInetAddress();
			Socket so;
			so = new Socket(ip, Bankserver.ANMELDEPORT);
			OutputStream out = so.getOutputStream();
			ObjectOutputStream dout = new ObjectOutputStream(out);
			dout.writeObject(Ueberweisebank.this.getBankleitzahl());
			dout.flush();
			so.close();
			this.verbindung.close();
		}
		
		/**
		 * Baut die Verbindung zum Bankserver an der angegebenen 
		 * IP-Adresse auf
		 * @param ipAdresse
		 * @throws Exception wenn beim Verbindungsaufbau etwas schief geht,
		 * 	       z.B. kein Bankserver erreichbar unter dieser Adresse
		 */
		public void mitServerVerbinden(String ipAdresse) throws Exception {
			InetAddress ip;
			ip = InetAddress.getByName(ipAdresse);
			this.verbindung= new Socket(ip, Bankserver.ANMELDEPORT);
			OutputStream out = verbindung.getOutputStream();
			ObjectOutputStream dout = new ObjectOutputStream(out);
			dout.writeObject(Ueberweisebank.this.getBankleitzahl());
			dout.flush();
		}
	}
	
	/**
	 * liefert den Ueberweisungsmanager
	 * @return
	 */
	public Ueberweisungsmanager getManager() {
		return manager;
	}
}
