package main.java.bankprojekt.server;


import main.java.bankprojekt.Daten;
/**
 * Daten einer Überweisung
 * @author   Dorothea Hubrich
 */
public class Ueberweisungsdaten implements Daten<Long>{
  private static final long serialVersionUID = 1L;
  private long kontonummerNach;
  private long bankleitzahlNach;
  private String verwendungszweck;
  private double betrag;
  private long kontonummerVon;
  
  /**
   * Zielkontonummer
   * @return Zielkontonummer
   */
  public long getKontonummerNach() {
	return kontonummerNach;
}

  /**
   * Zielkontonummer
   * @param kontonummerNach
   */
public void setKontonummerNach(long kontonummerNach) {
	this.kontonummerNach = kontonummerNach;
}

/**
 * Zielbankleitzahl
 * @return
 */
public long getBankleitzahlNach() {
	return bankleitzahlNach;
}

/**
 * Zielbankleitzahl
 * @param bankleitzahlNach
 */
public void setBankleitzahlNach(long bankleitzahlNach) {
	this.bankleitzahlNach = bankleitzahlNach;
}

/**
 * Verwendungszweck
 * @param verwendungszweck
 */
public void setVerwendungszweck(String verwendungszweck) {
	this.verwendungszweck = verwendungszweck;
}

/**
 * Überweisungsbetrag
 * @param betrag
 */
public void setBetrag(double betrag) {
	this.betrag = betrag;
}

/**
 * Quellkontonummer
 * @param kontonummerVon
 */
public void setKontonummerVon(long kontonummerVon) {
	this.kontonummerVon = kontonummerVon;
}

/**
 * Absender
 * @param absender
 */
public void setAbsender(String absender) {
	this.absender = absender;
}

/**
 * Quellbankleitzahl
 * @param bankleitzahlVon
 */
public void setBankleitzahlVon(long bankleitzahlVon) {
	this.bankleitzahlVon = bankleitzahlVon;
}

private String absender;
  private long bankleitzahlVon;

  /**
   * erstellt ein Überweisungsdatenobjekt mit den angegebenen Werten
   * @param kontonummer
   * @param bankleitzahl
   * @param verwendungszweck
   * @param betrag
   * @param kontonummerVon
   * @param absender
   * @param bankleitzahlVon
   */
  public Ueberweisungsdaten(long kontonummer, long bankleitzahl, String verwendungszweck,
                             double betrag, long kontonummerVon, String absender, long bankleitzahlVon) {
    this.kontonummerNach = kontonummer;
    this.bankleitzahlNach = bankleitzahl;
    this.verwendungszweck = verwendungszweck;
    this.betrag = betrag;
    this.kontonummerVon = kontonummerVon;
    this.absender = absender;
    this.bankleitzahlVon = bankleitzahlVon;
  }

  	/**
	 * @return   Returns the kontonummer.
	 */
	public long getKontonummer()
	  {
		return this.kontonummerNach;
	}
	  /**
	 * @return   Returns the bankleitzahl.
	 */
	public long getBankleitzahl()
	  {
		return this.bankleitzahlNach;
	}
	  /**
	 * @return   Returns the verwendungszweck.
	 */
	public String getVerwendungszweck()
	  {
		return this.verwendungszweck;
	}
	  /**
	 * @return   Returns the betrag.
	 */
	public double getBetrag()
	  {
		return this.betrag;
	}
	  /**
	 * @return   Returns the kontonummerVon.
	 */
	public long getKontonummerVon()
	  {
		return this.kontonummerVon;
	}
	  /**
	 * @return   Returns the absender.
	 */
	public String getAbsender()
	  {
		return this.absender;
	}
	  /**
	 * @return   Returns the bankleitzahlVon.
	 */
	public long getBankleitzahlVon()
	    {
			return this.bankleitzahlVon;
		}
	
	@Override
	public Long getId() {
		return this.getBankleitzahl();
	}
}
