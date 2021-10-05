package main.java.bankprojekt.verarbeitung;

/**
 * wandelt Kunden in String um, entspricht Function<Kunde, String>
 * @author Doro
 *
 */
interface Umwandlung
{
	/**
	 * macht aus k einen String, entspricht apply
	 * @param k
	 * @return
	 */
	public String umwandeln(Kunde k);
}