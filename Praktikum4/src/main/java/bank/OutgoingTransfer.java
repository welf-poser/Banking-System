package bank;

public class OutgoingTransfer extends Transfer{

    /**
     * Konstruktor
     * @param date Datum des ausgehaenden Transfers
     * @param amount Betrag des ausgehaenden Transfers
     * @param description Beschreibung des transfers
     * @param sender Name des Senders
     * @param recipient Name des Empfängers
     */
    public OutgoingTransfer(String date, double amount, String description, String sender, String recipient){
        super(date,amount,description,sender,recipient);
    }

    /**
     * Copy-Konstruktor
     * @param c Das zu kopierende Element
     */
    public OutgoingTransfer(OutgoingTransfer c){
        super(c);
    }

    /**
     * Berechnet den korrekten Betrag
     * @return Gibt den negativen Betrag aus da es sich um eine ausgehende Transaktion handelt
     */
    public double calculate(){
        return -super.calculate();
    }
}
