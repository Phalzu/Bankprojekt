package main.java.bankprojekt.verarbeitung;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * probiert Lambda-Ausdrücke und Streams aus
 * @author Doro
 *
 */
public class LambdaUndStreams { 
	
	/**
	 * gibt die übergebene Liste l auf der Konsole aus
	 * @param l
	 */
	public static void listeAusgeben(List<Kunde> l)
	{
		for(Kunde k: l)
		{
			System.out.println(k.toString());
				//Erster Schritt: Wandle Kunde in String um
		}
	}

	/**
	 * gibt die übergebene Liste l auf der Konsole aus
	 * @param l
	 * @param formatierung Umwandlungsfunktion von Kunde in einen String
	 */
	public static void listeAusgeben(List<Kunde> l, 
									Umwandlung formatierung)
	{
		for(Kunde k: l)
		{
			System.out.println(formatierung.umwandeln(k));
		}
	}

	/**
	 * gibt die übergebene Liste l auf der Konsole aus
	 * @param l
	 * @param formatierung Umwandlungsfunktion von Kunde in einen String
	 */
	public static void listeAusgeben(List<Kunde> l, 
						Function<Kunde, String> formatierung)
	{
		/*for(Kunde k: l)
		{
			System.out.println(formatierung.apply(k));
		}
		*/
		Stream<Kunde> kundenstream = l.stream();
		Stream<String> stringstream = kundenstream.map(formatierung);
		//stringstream.forEach(string -> System.out.println(string));
		stringstream.forEach(System.out::println);
	}

	/**
	 * spielt mit Lambda-Ausdrücken und Streams herum
	 * @param args wird nicht benutzt
	 */
	public static void main(String[] args) {
		Runnable r = () ->
					{
						try {
						Thread.sleep(3000);
						} catch (InterruptedException e) {}
						System.out.println("Guten Morgen");
					};
		Thread begruessung = new Thread(r);
		begruessung.start();

		TreeSet<Kunde> kundenliste = new TreeSet<Kunde>();
		
		Kunde hans = new Kunde("Hans", "Meier", "Unterm Regenbogen 19",LocalDate.of(1990, 1, 5));
		Kunde otto = new Kunde("Otto", "Kar", "Hoch über den Wolken 7",LocalDate.of(1992, 2, 25));
		Kunde sabrina = new Kunde("Sabrina", "August", "Im Wald 15",LocalDate.of(1988, 3, 21));
		
		kundenliste.add(hans);
		kundenliste.add(otto);
		kundenliste.add(sabrina);
		
		System.out.println("In natürlicher Ordnung - nach dem Nachnamen: ");
		LinkedList<Kunde> liste1 = new LinkedList<Kunde>(kundenliste);
		listeAusgeben(liste1);
		System.out.println("-------");
		
		System.out.println("Nach Adresse sortiert: ");
		LinkedList<Kunde> liste2 = new LinkedList<Kunde>(kundenliste);
		List<Kunde> neu = liste2.stream().sorted(
				(Kunde a, Kunde b) ->
				{
					return a.getAdresse().compareTo(b.getAdresse());
				})
				.collect(Collectors.toList());
		Function<Kunde,String> u = Kunde::getName;
					//kunde -> kunde.getName();
		listeAusgeben(neu, u);
		System.out.println("-------");
		
		System.out.println("Nach Geburtstag sortiert: ");
		LinkedList<Kunde> liste3 = new LinkedList<Kunde>(kundenliste);
		Comparator<Kunde> vergleicher3 = 
			(kundeA, kundeB) ->
				kundeA.getGeburtstag().compareTo(kundeA.getGeburtstag());
		Collections.sort(liste3, vergleicher3);
		listeAusgeben(liste3);
		System.out.println("-------");

/*		int x = 0;
		Runnable r = () -> System.out.println(x);
		Thread t = new Thread(r);
		t.start();
*/		
		Konto eins = new Girokonto(hans, 1, 0);
		eins.einzahlen(100);
		Konto zwei = new Girokonto(otto, 2, 0);
		zwei.einzahlen(200);
		Konto drei = new Girokonto(sabrina, 3, 0);
		drei.einzahlen(100);
		
		Map<Long, Konto> kontenliste = new HashMap<Long, Konto>();
		kontenliste.put(1L, eins);
		kontenliste.put(2L, zwei);
		kontenliste.put(3L, drei);
		
		// Gesamtbetrag berechnen:
		double gesamt = 0.0;
		for(Konto k: kontenliste.values())
			gesamt = gesamt + k.getKontostand();
		System.out.println("Gesamtbetrag auf allen Konten: " + gesamt);

		Stream<Konto> kontostream = kontenliste.values().stream();
		Stream<Double> zahlenstream = 
					kontostream.map(konto -> konto.getKontostand());
		gesamt = zahlenstream.reduce(0.0, (ges, zahl) -> ges + zahl);
		System.out.println("Gesamtbetrag auf allen Konten: " + gesamt);

		gesamt = kontenliste.values().stream()
				.mapToDouble(Konto::getKontostand)
				.sum();
		
		gesamt = kontenliste.values().parallelStream()
				.reduce(0.0, (ges, konto) -> ges + konto.getKontostand(),
														(a, b) -> a + b);

	}

}
