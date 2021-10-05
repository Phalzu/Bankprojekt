package main.java.bankprojekt.oberflaeche;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.java.bankprojekt.verarbeitung.Girokonto;
import main.java.bankprojekt.verarbeitung.Kunde;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Controller für die Ereignisse der Oberfläche KontoOberflaeche
 */
public class KontoController extends Application {

    /**
     * Das Hauptfenster
     */
    private Stage stage;

    @FXML public FlowPane containerOne;
    @FXML public Label lblUeberschrift;
    @FXML public GridPane containerTwo;
    @FXML public Label lblKontonr;
    @FXML public Label lblKontostand;
    @FXML public Label lblGesperrt;
    @FXML public Label lblKontonrAusgabe;
    @FXML public Label lblKontostandAusgabe;
    @FXML public CheckBox cbGesperrtAusgabe;
    @FXML public Label lblAdresse;
    @FXML public Label lblAdresseAusgabe;
    @FXML public Label lblWelcome;
    @FXML public FlowPane containerThree;
    @FXML public TextField textFieldMoney;
    @FXML public Button btnEinzahlen;
    @FXML public Button btnAbheben;

    private Kunde zurek = new Kunde("Philipp", "Zurek", "Oberfeldstr. 138", LocalDate.of(2000, 2, 16));
    private Girokonto meinKonto = new Girokonto(zurek, 1L, 5.0);

    @FXML public String kontonr = meinKonto.getKontonummerFormatiert();
    @FXML public String kontostand = meinKonto.getKontostandFormatiert();
    @FXML public String kundeAdresse = zurek.getAdresse();

    // DEPRECATED
    // Die Oberfläche, die abgebildet werden soll
    //
    // private KontoOberflaeche koOberflaeche = new KontoOberflaeche();

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;

        // FXMLLoader für Ihre FXML-Datei erstellen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../../Bank.fxml"));

        // loader.setController(this); <<--- Wird bereits in der FXML festgelegt!

        // FXML-Datei laden
        Parent root = loader.load();

        // Scene aufbauen...
        Scene scene = new Scene(root, 550, 520);
        primaryStage.setTitle("Kontoverwaltung");
        primaryStage.setScene(scene);

        // ...und anzeigen
        primaryStage.show();
    } // End of start

    /**
     * main-Methode die eine GUI für den Endnutzer bereitstellen soll
     * @param args ...
     */
    public static void main(String[] args) {
        launch(args);
    } // End of main

    public void showKontonr(){

    }

    /**
     * Schließen des Fensters
     */
    public void schliessen()
    {
        stage.close();
    }

} // End of Application
