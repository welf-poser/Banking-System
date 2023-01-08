package bank;

public class IncomingTransfer extends Transfer{

    /**
     * Konstruktor
     * @param date Datum des eingehenden Transfers
     * @param amount Betrag des eingehenden Transfers
     * @param description Beschreibung des transfers
     * @param sender Name des Senders
     * @param recipient Name des Empfängers
     */
    public IncomingTransfer(String date, double amount, String description, String sender, String recipient){
        super(date,amount,description,sender,recipient);
    }

    /**
     * Copy-Konstruktor
     * @param c Das zu kopierende Element
     */
    //Copy-Constructor
    public IncomingTransfer(IncomingTransfer c){
        super(c);
    }

    //calculation Implementation in der Superklasse ist Valide
}
