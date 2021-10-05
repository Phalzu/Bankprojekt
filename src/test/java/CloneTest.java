package test.java;

/*
 * Testet das erfolgreiche Klonen eines Bank-Objekts
 * @author Philipp Zurek
 */
public class CloneTest {
   /*
    private static Kunde testKundeOne = null;
    private static Kunde testKundeTwo = null;
    private static Long testGirokontoNr = null;
    private static Long testSparbuchNr = null;
    private static Bank testBank = null;
    private static Bank klonMitFlecken = null;

    @BeforeEach
    public void setUp(){
        testKundeOne = new Kunde("Karl", "Heinz", "Wolkenstraße 7", LocalDate.now());
        testKundeTwo = new Kunde("Sabine", "Springer", "Schloßallee 13a", LocalDate.now());

        testBank = new Bank(123);
        testSparbuchNr = testBank.sparbuchErstellen(testKundeOne);
        testGirokontoNr = testBank.girokontoErstellen(testKundeTwo);
    } // End of setUp

    @AfterAll
    public static void tearDown(){
        testKundeOne = null;
        testKundeTwo = null;
        testGirokontoNr = null;
        testSparbuchNr = null;
        testBank = null;
        klonMitFlecken = null;
    } // End of tearDown

    @Test
    public void bankWirdErfolgreichErstellt(){
        // SetUp
        setUp();

        String konten = testBank.getAlleKonten();
        Assertions.assertTrue(konten.contains(testGirokontoNr.toString()));
        Assertions.assertTrue(konten.contains(testSparbuchNr.toString()));

        // TearDown
        tearDown();
    } // End of DatenbankTest

    @Test
    public void kopieErfolgreichErstellt(){
        // SetUp
        setUp();

        try {
            klonMitFlecken = testBank.clone();
        }catch (CloneNotSupportedException cnse){
            cnse.printStackTrace();
        } // End of try-catch

        Assertions.assertEquals(klonMitFlecken, testBank);

        // TearDown
        tearDown();
    } // End of DatenbankTest

    @Test
    public void kopieistUnabhaengig() throws KontonummerNotFoundException {
        // SetUp
        setUp();

        try {
            Bank klonMitFlecken = testBank.clone();
        }catch (CloneNotSupportedException cnse){
            cnse.printStackTrace();
        } // End of try-catch

        testBank.geldEinzahlen(testGirokontoNr, 5000.0);

        Assertions.assertNotEquals(klonMitFlecken.getKontostand(testGirokontoNr), testBank.getKontostand(testGirokontoNr));

        // TearDown
        tearDown();
    } // End of DatenbankTest
*/
} // End of cloneTest
