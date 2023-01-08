package bank;

import bank.exceptions.TransactionAttributeException;

/**
 * Klasse die eine Transaktion widerspiegelt
 */
public abstract class Transaction implements CalculateBill{
    //Attributes
    /**
     * Speichert das Datum der Transaktion
     */
    private String date;
    /**
     * Speichert den Wert der Transaktion
     */
    protected double amount;
    /**
     * Speichert die Beschreibung der Transaktion
     */
    private String description;


    //Setter

    /**
     * Belegt den Wert date der Klasse Transaction neu
     * @param date Der neue Wert für date der Transaktion
     */
    public void set_date(String date){this.date = date;}

    /**
     * Belegt den Wert amount der Klasse Transaction neu
     * @param amount Der neue Wert für amount der Transaktion
     */
    public void set_amount(double amount) throws TransactionAttributeException {this.amount = amount;}

    /**
     * Belegt den Wert description der Klasse Transaction neu
     * @param description Der neue Wert für description der Transaktion
     */
    public void set_description(String description){this.description = description;}


    //Getter

    /**
     * Gibt den Wert date der Transaktion zurück
     * @return Rückgabewert date
     */
    public String get_date(){return date;}

    /**
     * Gibt den Wert amount der Transaktion zurück
     * @return Rückgabewert amount
     */
    public double get_amount(){return amount;}

    /**
     * Gibt den Wert description der Transaktion zurück
     * @return Rückgabewert description
     */
    public String get_description(){return description;}


    //Constructors

    /**
     * Konstruktor der Klasse Transaction
     * @param date Datum der Transaktion
     * @param amount Betrag der Transaktion
     * @param description Beschreibung der Transaktion
     */
    public Transaction(String date, double amount, String description){
        this.date = date;
        this.amount = amount;
        this.description = description;
    }


    //Copy-Constructors

    /**
     * Copy-Konstruktor mit Deep-Copy der Klasse Transaktion
     * @param c Ist das zu kopierende Objekt
     */
    public Transaction(Transaction c){
        date = c.date;
        amount = c.amount;
        description = c.description;
    }


    //Methods

    /**
     * Abstrakte Methode zur Ausgabe die in den Kind-Klassen implementiert wird
     */
    public abstract void printObject();

    /**
     *Methode zur Ausgabe der Werte in der Transaktion als String
     * @return Gibt einen String mit den Werten der Transaktion zurück
     */
    public String toString(){
        return "Date: " + get_date() + "\n" +
        //"Amount: "+ get_amount() + "\n" +
        "Amount: " + this.calculate() + "\n" +
        "Description: " + get_description() + "\n";
    }

    /**
     * Prüft, ob zwei Transaktionen die gleichen Werte haben
     * @param a Zu prüfende Transaktion
     * @return Gibt einen Wahrheitswert zurück, ob die Werte der geprüften Transaktionen identisch sind
     */
    public boolean equals(Transaction a){
        return (this.date.equals(a.date) && this.amount == a.amount && this.description.equals(a.description));
    }
}
