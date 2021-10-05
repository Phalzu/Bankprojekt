package main.java.bankprojekt.verarbeitung;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

/**
 * verwaltet eine Menge von Kunden
 * @author Doro
 *
 */
public class Kundenmenge {

	/**
	 * erstellt eine Menge von Kunden und löscht die unnötigen
	 * wieder
	 * @param args
	 */
	public static void main(String[] args) {
		Kunde anna = new Kunde("Anna", "Anna", "hier", LocalDate.now());
		Kunde berta = new Kunde("Berta", "Berta", "hier", LocalDate.now());
		Kunde chris = new Kunde("Chris", "Chris", "hier", LocalDate.now());
		Kunde anton = new Kunde("Anton", "Anton", "hier", LocalDate.now());
		Kunde adalbert = new Kunde("Bert", "Adal", "hier", LocalDate.now());

		Set<Kunde> menge = new TreeSet<>();

		menge.add(anna);
		menge.add(berta);
		menge.add(chris);
		menge.add(anton);
		menge.add(adalbert);

		for(Kunde k: menge)
		{
			System.out.println(k.getName());
		}

		System.out.println();

		// End of for: Lösche jede/n Kunden/Kundin mit Anfangsbuchstabe "A" des Nachnamens
		menge.removeIf(t -> t.getNachname().startsWith("A"));

		for(Kunde k: menge)
		{
			System.out.println(k.getName());
		}

		System.out.println("NEUE AUFGABE \n");


	}

}