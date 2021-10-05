package main.java;

import main.java.bankprojekt.verarbeitung.Girokonto;
import main.java.bankprojekt.verarbeitung.Konto;
import main.java.bankprojekt.verarbeitung.Kunde;
import main.java.bankprojekt.verarbeitung.Ueberweisebank;

import java.io.*;


/**
 * Spielereien mit Dateien
 * @author Doro
 *
 */
public class DateiLesen {

	/**
	 * liest und schreibt Dateien
	 * @param args wird nicht genutzt
	 * @throws IOException tritt nicht auf
	 */
	public static void main(String[] args) throws IOException
	{
		
		Kunde ich = new Kunde("Dorothea", "Hubrich", "zuhause", "13.07.76");
		Konto k = new Girokonto(ich, 1234, 987);
		Ueberweisebank b = new Ueberweisebank(34569);
		
		
		try (FileOutputStream fo = new FileOutputStream("konto.dat");
			 ObjectOutputStream oo = new ObjectOutputStream(fo);)
		{
			//oo.writeObject(k);
			oo.writeObject(b); //Achtung: Compiler erkennt nicht, wenn
				//das Objekt nicht serialisierbar ist. Das gibt eine Exception
		}
		catch(Exception e) { e.printStackTrace();}
		
		try (FileInputStream fi = new FileInputStream("konto.dat");
			 ObjectInputStream oi = new ObjectInputStream(fi);	)
		{
			//Konto kopie = (Konto) oi.readObject();
			Ueberweisebank kopie = (Ueberweisebank) oi.readObject();
			System.out.println(kopie);
			System.out.println(kopie.getManager());
				//�rgerlicherweise null, wenn man nichts weiter tut
				//readObject bietet die M�glichkeit, das zu �ndern
		} catch (ClassNotFoundException e) {
				//hier nicht
		}
		catch(ClassCastException e)
		{
			//hier nicht
		}

		//Ueberweisebank b = new Ueberweisebank(34569);
		
		
		//nicht besser als der Scanner, nur anders:
		BufferedReader console = new BufferedReader(
									new InputStreamReader(
									System.in));
		String datei="";
		
		try {
			datei = console.readLine();
		} catch (IOException e) {
		}
		
		try (FileReader fr = new FileReader(datei);
			 BufferedReader br = new BufferedReader(fr);
			 LineNumberReader lnr = new LineNumberReader(br);)
			//try-with-Ressourcen
		{
		
			while(lnr.ready())
			{
				String zeile = lnr.readLine();
				if(zeile.equals("Ende"))
				{	//return;
				
				}
				int nummer = lnr.getLineNumber();
				System.out.println(nummer + ": " + zeile);
			}
			//Alle angelegten Resourcen werden automatisch geschlossen
		}
		catch (IOException e)
		{}
		
		PrintStream neu = new PrintStream(
				new FileOutputStream("neueDatei.txt"));
		//System.setOut(neu);
		//System.out.println("Ein Text");
		
		
		
		

		
		
	}

}
