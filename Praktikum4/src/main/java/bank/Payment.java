package bank;
import bank.exceptions.*;

/**
 * Klasse die eine Zahlung wiederspiegelt
 */
public class Payment extends Transaction {

    //Attributes
    /**
     * Speichert den Prozentwert des Eingangszinssatzes einer Zahlung
     */
    private double incomingInterest;
    /**
     * Speichert den Prozentwert des Ausgangszinssatzes einer Zahlung
     */
    private double outgoingInterest;

    //Setter

    /**
     * Belegt den Wert incomingInterest der Klasse Payment neu
     * @param incomingInterest Der neue Wert für incomingInterest der Zahlung
     */
    public void set_incomingInterest(double incomingInterest) throws TransactionAttributeException {
        if (incomingInterest < 0 || incomingInterest > 1) {
            throw new TransactionAttributeException("Falsche Eingabe fuer incomingInterest");
        } else {
            this.incomingInterest = incomingInterest;
        }
    }

    /**
     * Belegt den Wert outgoingInterest der Klasse Payment neu
     * @param outgoingInterest Der neue Wert für outgoingInterest der Zahlung
     */
    public void set_outgoingInterest(double outgoingInterest) throws TransactionAttributeException {
        if (outgoingInterest < 0 || outgoingInterest > 1) {
            throw new TransactionAttributeException("Falsche Eingabe fuer outgoingInterest");
        } else {
            this.outgoingInterest = outgoingInterest;
        }
    }

    //Getter

    /**
     * Gibt den Wert incomingInterest zurück
     * @return Rückgabewert incomingInterest
     */
    public double get_incomingInterest(){return incomingInterest;}

    /**
     * Gibt den Wert outgoingInterest zurück
     * @return Rückgabewert outgoingInterest
     */
    public double get_outgoingInterest(){return outgoingInterest;}

    //Constructors
    /**
     * Konstruktor der Klasse Payment
     * @param date Datum der Zahlung
     * @param amount Betrag der Zahlung
     * @param description Beschreibung der Zahlung
     * @param incomingInterest Eingangszinssatz der Zahlung
     * @param outgoingInterest Ausgangszinssatz der Zahlung
     */
    public Payment(String date, double amount, String description, double incomingInterest, double outgoingInterest) throws TransactionAttributeException {
        super(date, amount, description);

        set_incomingInterest(incomingInterest);
        set_outgoingInterest(outgoingInterest);
    }

    //Copy-Constructors

    /**
     * Copy-Konstruktor mit Deep-Copy der Klasse Payment
     * @param c Ist das zu kopierende Objekt
     */
    public Payment(Payment c){
        super(c);
        incomingInterest = c.incomingInterest;
        outgoingInterest = c.outgoingInterest;
    }

    //Methods

    /**
     * Gibt alle Werte der Zahlung auf der Konsole aus
     */
    public void printObject(){
        System.out.println("**************** " + this.getClass().getSimpleName() + " ****************");
        System.out.println("Date: " + get_date());
        System.out.println("Amount: "+ get_amount());
        System.out.println("Description: " + get_description());
        System.out.println("Incoming Interest: " + incomingInterest);
        System.out.println("Outgoing Interest: " + outgoingInterest);
        System.out.println("*****************************************");
    }

    /**
     * Methode zur Ausgabe der Werte in der Zahlung als String
     * @return Gibt einen String mit den Werten der Zahlung zurück
     */
    public String toString(){
        return "**************** " + this.getClass().getSimpleName() + " ****************\n"+
                super.toString() +
                "Incoming Interest: " + get_incomingInterest() +"\n" +
                "Outgoing Interest: " + get_outgoingInterest() + "\n" +
                "*****************************************\n";
    }

    /**
     * Methode die den neuen Betrag anhand des Ein/- und Ausgangszinssatzes berechnet
     * @return Neuer Betrag berechnet mit dem Ein/- bzw. Ausgangszinssatzes
     */
    public double calculate(){
        if(get_amount() >= 0){
            return get_amount() - get_amount() * get_incomingInterest();
        }
        else{
            return get_amount()+get_amount()*get_outgoingInterest();
        }
    }

    /**
     * Prüft ob zwei Zahlungen die gleichen Werte haben
     * @param a Zu prüfende Zahlung
     * @return Gibt einen Wahrheitswert zurück ob die Werte der geprüften Zahlungen identisch sind
     */
    public boolean equals(Payment a){
       return (super.equals(a) && incomingInterest == a.incomingInterest && outgoingInterest == a.outgoingInterest);
    }
}
