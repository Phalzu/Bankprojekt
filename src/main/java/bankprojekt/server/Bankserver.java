package main.java.bankprojekt.server;

import main.java.bankprojekt.Daten;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Bankserver für Überweisungen von einer Überweisebank zu einer anderen
 * @author Dorothea Hubrich
 * @version 1.0
 * @param <IDTyp> Datentyp der ID, unter der sich die Überweisebanken
 *                anmelden
 */
public class Bankserver<IDTyp> {
	/**
	 * Speicherung der Sockets zu den einzelnen Banken
	 */
	private HashMap<IDTyp, Socket> tabelle;

	/**
	 * Port, auf dem sich Banken anmelden können
	 */
	public static final int ANMELDEPORT = 7777; //Warten auf Anmeldung am Server
	
	/**
	 * Port, an dem der Server Überweisungen entgegennimmt
	 */
	public static final int UEBERWEISUNGSPORT = 6666;

	/**
	 * erstellt einen Bankserver und startet ihn
	 */
	public Bankserver() {
		tabelle = new HashMap<IDTyp, Socket>();
	}

	/**
	 * warten auf eingehende An- und Abmeldungen von Banken
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("unchecked")
	public void verbindung() throws IOException, ClassNotFoundException {

		ServerSocket seso;
		seso = new ServerSocket(Bankserver.ANMELDEPORT);
		Socket so;
		so = seso.accept();

		InputStream in;
		in = so.getInputStream();
		ObjectInputStream din = new ObjectInputStream(in);
		IDTyp blz = (IDTyp) din.readObject();
		if (tabelle.containsKey(blz))
		{
			Socket gespeichert = tabelle.get(blz);
			gespeichert.close();
			tabelle.remove(blz);
			so.close();
			System.out.println("Abmeldung von " + blz);
		}
		else
		{
			tabelle.put(blz, so);
			System.out.println("Anmeldung von " + blz);
		}
		seso.close();
	}
	
	/**
	 * wartet auf eine eingehende Überweisung und leitet sie an die Zielbank weiter
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void ueberweisungAusfuehren() throws Exception {
		boolean ausgefuehrt = false;
		ServerSocket seso;
		seso = new ServerSocket(Bankserver.UEBERWEISUNGSPORT);
		Socket so1;
		so1 = seso.accept();

		InputStream in;
		in = so1.getInputStream();
		ObjectInputStream oin = new ObjectInputStream(in);
		Daten<IDTyp> udaten = (Daten<IDTyp>) oin.readObject();
		IDTyp blz = udaten.getId();
		Socket so2 = tabelle.get(blz);
		if (so2 != null) {
			OutputStream out = so2.getOutputStream();
			ObjectOutputStream oout = new ObjectOutputStream(out);
			oout.writeObject(udaten);
			oout.flush();
			DataInputStream din = new DataInputStream(so2.getInputStream());
			ausgefuehrt = din.readBoolean();
		}
		DataOutputStream dout = new DataOutputStream(so1.getOutputStream());
		dout.writeBoolean(ausgefuehrt);
		dout.flush();
		so1.close();
		seso.close();
	}

	/**
	 * wartet, dass sich zwei Banken anmelden, genau eine Überweisung
	 * ausführen und sich dann wieder abmelden
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Bankserver<Long> bankserver = new Bankserver<Long>();
		bankserver.verbindung();
		bankserver.verbindung();
		bankserver.ueberweisungAusfuehren();
		bankserver.verbindung();
		bankserver.verbindung();
	}
}
