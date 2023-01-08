package bank;
import bank.exceptions.TransactionAttributeException;

/**
 * Klasse die einen Transfer wiederspieglt
 */
public class Transfer extends Transaction{

    //Attributes
    /**
     * Speichert den Sender des Transfers
     */
    private String sender;
    /**
     * Speichert den Empfänger des Transfers
     */
    private String recipient;

    //Setter

    /**
     * Belegt den Wert amount der Klasse Transaction neu
     * @param amount Der neue Wert für amount der Transaktion
     */
    public void set_amount(double amount)throws TransactionAttributeException {
        if(amount <= 0){
            throw new TransactionAttributeException("Der Betrag darf nicht negativ sein");
        } else {
            this.amount = amount;
        }
    }

    /**
     * Belegt den Wert sender der Klasse Transfer neu
     * @param sender Der neue Wert für sender
     */
    public void set_sender(String sender){this.sender = sender;}

    /**
     * Belegt den Wert recipient für die Klasse Transfer neu
     * @param recipient Der neue Wert füt recipient
     */
    public void set_recipient(String recipient){this.recipient = recipient;}

    //Getter

    /**
     * Gibt den Wert sender zurück
     * @return Rückgabewert sender
     */
    public String get_sender(){return sender;}

    /**
     * Gibt den wert recipient zurück
     * @return Rückgabewert recipient
     */
    public String get_recipient(){return recipient;}

    //Constructors
    /**
     * Konstruktor der Klasse Transfer
     * @param date Datum des Transfers
     * @param amount Betrag des Transfers
     * @param description Beschreibung des Transfers
     * @param sender Sender des Transfers
     * @param recipient Empfänger des Transfers
     */
    public Transfer(String date, double amount, String description, String sender, String recipient){
        super(date, amount, description);
        this.sender = sender;
        this.recipient = recipient;
    }

    //Copy-Constructors

    /**
     * Copy-Konstruktor mit Deep-Copy der Klasse Transfer
     * @param c Ist das zu kopierende Objekt
     */
    public Transfer(Transfer c){
        super(c);
        sender = c.sender;
        recipient = c.recipient;
    }

    //Methods

    /**
     * Gibt alle Werte der Zahlung auf der Konsole aus
     */
    public void printObject(){

        System.out.println("**************** " + this.getClass().getSimpleName() + " ****************");
        System.out.println("Date: "+ get_date());
        System.out.println("Amount: "+ get_amount());
        System.out.println("Description: "+ get_description());
        System.out.println("Sender: "+sender);
        System.out.println("Recipient:"+recipient);
        System.out.println("*****************************************");
    }

    /**
     * Methode zur Ausgabe der Werte in dem Transfer als String
     * @return Gibt einen String mit allen Werten des Transfers zurück
     */
    public String toString(){
        return "**************** " + this.getClass().getSimpleName() + " ****************\n" +
                super.toString() +
                "Sender: "+ get_sender() + "\n" +
                "Recipient: "+ get_recipient() + "\n" +
                "*****************************************\n";
    }

    /**
     * Prüft ob zwei Transfers die gleichen Werte haben
     * @param a Zu prüfender Transfer
     * @return Gibt einen Wahrheitswert zurück ob die Werte der geprüften Transfers identisch sind
     */
    public boolean equals(Transfer a){
        return (super.equals(a) && sender == a.sender && recipient == a.recipient);
    }

    /**
     * Macht basically nichts
     * @return Gibt den Betrag des Transfers zurück
     */
    public double calculate(){
        return get_amount();
    }
}
