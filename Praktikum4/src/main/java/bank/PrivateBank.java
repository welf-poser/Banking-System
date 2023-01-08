package bank;
import bank.exceptions.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.nio.file.Files.deleteIfExists;

/**
 * Die Klasse soll eine Bank repräsentieren
 */
public class PrivateBank implements Bank{

    /**
     * Name der Bank
     */
    private String name;

    /**
     * Zinssatz der Eingangszahlungen
     */
    private double incomingInterest;

    /**
     * Zinssatz der Ausgangszahlungen
     */
    private double outgoingInterest;

    /**
     * Eine Map wo einem Account eine Liste von Transaktionen zugewiesen werden kann
     */
    private Map<String, List<Transaction>> accountToTransactions = new HashMap<String, List<Transaction>>();

    /**
     * Directory wo die JSON-Datein für diese Instanz abgelegt sind
     */
    private String directoryName;

    //Getter###########################################################################################################

    /**
     * Gibt den Namen der Bank aus
     * @return Name der Bank
     */
    public String get_name(){return name;}

    /**
     * Gibt den Eingangszinssatz aus
     * @return Eingangszinssatz
     */
    public double get_incomingInterest(){return incomingInterest;}

    /**
     * Gibt den Ausgangszinssatz aus
     * @return Ausgangszinssatz
     */
    public double get_outgoingInterest(){return outgoingInterest;}

    /**
     * Gibt die Directory zurück wo die JSON-Datein für diese Bankinstanz abgelegt sind
     * @return Directory für JSON-Datein
     */
    public String get_directoryName(){return directoryName;}

    /**
     * Gibt eine Liste der angelegten Accounts dieser Bank zurück
     * @return Liste der angelegten Accounts
     */
    public ArrayList<String> get_accountNameList(){

        ArrayList<String> accounts = new ArrayList<String>();
        for(String key: accountToTransactions.keySet()){
            accounts.add(key);
        }
        return accounts;
    }

    //Setter###########################################################################################################

    /**
     * Bestimmt den Namen der Bank
     * @param name Name der Bank
     */
    public void set_name(String name){this.name = name;}

    /**
     * Setzt den Eingangszinssatz
     * @param incomingInterest Eingangszinssatz
     * @throws TransactionAttributeException Falls der Eingangszinssatz kleiner als null oder größer als 1 ist, wird eine Exception geworfen
     */
    public void set_incomingInterest(double incomingInterest) throws TransactionAttributeException, IOException {
        if (incomingInterest < 0 || incomingInterest > 1) {
            throw new TransactionAttributeException("Falsche Eingabe fuer incomingInterest");
        } else {
            this.incomingInterest = incomingInterest;
        }

        for(String key: accountToTransactions.keySet()){
            writeAccounts(key);
        }
    }

    /**
     * Setzt den Ausgangszinssatz
     * @param outgoingInterest Ausgangszinssatz
     * @throws TransactionAttributeException Falls der Ausgangszinssatz kleiner als null oder größer als 1 ist, wird eine Exception geworfen
     */
    public void set_outgoingInterest(double outgoingInterest) throws TransactionAttributeException, IOException {
        if (outgoingInterest < 0 || outgoingInterest > 1) {
            throw new TransactionAttributeException("Falsche Eingabe fuer outgoingInterest");
        } else {
            this.outgoingInterest = outgoingInterest;
        }
        for(String key: accountToTransactions.keySet()){
            writeAccounts(key);
        }
    }

    /**
     * Setzt Direktory für Ablage der JSON-Datein
     * @param directory Pfad als String
     */
    public void set_directoryName(String directory){this.directoryName = directory;}

    //Constructor######################################################################################################

    /**
     * Konstruktor der Klasse PrivateBank
     * @param name Name der Bank
     * @param incomingInterest Eingangszinssatz
     * @param outgoingInterest Ausgangszinssatz
     * @throws TransactionAttributeException Diese Exception wird geworfen wenn der Ein-/ oder Ausgangszinssatz mit falschen Werten belegt ist
     */
    public PrivateBank(String name, double incomingInterest, double outgoingInterest, String directoryName) throws TransactionAttributeException, TransactionAlreadyExistException, AccountAlreadyExistsException, IOException {
        this.name = name;
        set_incomingInterest(incomingInterest);
        set_outgoingInterest(outgoingInterest);
        this.directoryName = directoryName;

        File theDir = new File(directoryName);
        if (!theDir.exists()){
            theDir.mkdirs();
        }
        readAccounts();
    }

    //Copy-Constructor#################################################################################################

    /**
     * Kopiert eine PrivateBank-Instanz mit Ausnahme von den Accounts
     * @param c Die zu kopierende Klasse
     */
    public PrivateBank(PrivateBank c){
        this.name = c.name;
        this.incomingInterest = c.incomingInterest;
        this.outgoingInterest = c.outgoingInterest;
        //Warum wird dieses Objekt nicht auch mitkopiert?
        //this.accountToTransactions = c.accountToTransactions;
        this.directoryName = c.directoryName;
    }

    //Methoden#########################################################################################################
    /**
     * Liest alle JSONS im angegebenen Dateipfad der Bankklasse ein und dazu Accounts mit den Transaktionen der JSONs
     * @throws IOException Fehler beim einlesen der Files
     * @throws TransactionAlreadyExistException Transaktion war doppelt vorhanden im JSON
     * @throws AccountAlreadyExistsException Account war doppelt vorhanden im JSON
     * @throws TransactionAttributeException Ein Attribut war ungültig
     */
    private void readAccounts() throws IOException, TransactionAlreadyExistException, AccountAlreadyExistsException, TransactionAttributeException {
        //Deserialize

            //alle vorhandenen JSONS einlesen
            File[] files = new File(directoryName).listFiles();
            //Prüfen ob directory nicht leer
            if(files.length > 0){
                //Traversiert durch Directory
                for(File file:files){

                    //Speichert Accountname aus der Json-Bezeichnung
                    String accountName = file.getName().replaceAll(".json","");

                    //JSON einlesen
                    String fileContent = "";

                    try {
                        byte[] bytes = Files.readAllBytes(Paths.get(this.directoryName + "/" + accountName + ".json"));
                        fileContent = new String (bytes);
                    } catch (IOException e) {
                        //handle exception
                    }

                    Type type = new TypeToken<ArrayList<Transaction>>() {}.getType();

                    //String aus JSON in List parsen
                    Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(Transaction.class, new PersistenceHandler()).setPrettyPrinting().create();
                    List<Transaction> outputList = gson.fromJson(fileContent, type);

                    //Account mit Liste von JSON erstellen
                    createAccount(accountName,outputList);
                }
            }
    }

    /**
     * Serialisiert die Transaktionen eines Accounts in einer JSON
     * @param account Accountname
     * @throws IOException Fehler beim Schreiben der JSON
     */
    private void writeAccounts(String account) throws IOException {
        //Serialize
        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(Transaction.class, new PersistenceHandler()).setPrettyPrinting().create();

        FileWriter file = new FileWriter(this.directoryName + "/" + account + ".json");
        file.write(gson.toJson(this.accountToTransactions.get(account)));
        file.close();
    }

    /**
     * Löscht einen vorhandenen Account aus einer Bank
     * @param account Der zu löschende Account
     * @throws AccountDoesNotExistException Wird geworfen falls der Account nicht existiert
     */
    public void deleteAccount(String account) throws AccountDoesNotExistException, IOException{
        if(!accountToTransactions.containsKey(account)){
            throw new AccountDoesNotExistException("Konto existiert nicht");
        }
        accountToTransactions.remove(account);

        deleteIfExists(Paths.get(get_directoryName() + "\\" + account + ".json"));
    }

    /**
     * Gibt die Klasse auf der Konsole aus
     */
    public void printObject(){
        System.out.println("**************** " + this.getClass().getSimpleName() + " ****************");
        System.out.println("Name: " + get_name());
        System.out.println("Incoming Interest: " + incomingInterest);
        System.out.println("Outgoing Interest: " + outgoingInterest);
        System.out.println("*****************************************");
    }

    /**
     * Methode zur Ausgabe der Eigenschaften der Bank als String
     * @return Gibt einen String mit den Werten der Zahlung zurück
     */
    @Override
    public String toString(){
        return "**************** " + this.getClass().getSimpleName() + " ****************\n"+
                "Name: " + name + "\n" +
                "Incoming Interest: " + get_incomingInterest() +"\n" +
                "Outgoing Interest: " + get_outgoingInterest() + "\n" +
                "Directory: " + get_directoryName() + "\n" +
                "*****************************************\n";
    }

    /**
     * Prüft ob zwei Banken die gleichen Werte haben
     * @param a Zu prüfende Bank
     * @return Gibt einen Wahrheitswert zurück, ob die Werte der geprüften Bank identisch sind
     */
    public boolean equals(PrivateBank a){

            return (name.equals(a.name) &&
                    incomingInterest == a.incomingInterest &&
                    outgoingInterest == a.outgoingInterest &&
                    directoryName == a.directoryName &&
                    accountToTransactions.hashCode() == a.accountToTransactions.hashCode());
    }

    /**
     * Adds an account to the bank.
     *
     * @param account the account to be added
     * @throws AccountAlreadyExistsException if the account already exists
     */
    public void createAccount(String account) throws AccountAlreadyExistsException, IOException {

        if(accountToTransactions.containsKey(account)){
            throw new AccountAlreadyExistsException("Konto existiert schon");
        }
        else{
            accountToTransactions.put(account, new ArrayList<Transaction>());
        }

        writeAccounts(account);

    }

    /**
     * Adds an account (with specified transactions) to the bank.
     * Important: duplicate transactions must not be added to the account!
     *
     * @param account      the account to be added
     * @param transactions a list of already existing transactions which should be added to the newly created account
     * @throws AccountAlreadyExistsException    if the account already exists
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
     */
    public void createAccount(String account, List<Transaction> transactions) throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException, IOException {


        if(transactions != null){
            //Prüft ob der Account schon existiert
            if(accountToTransactions.containsKey(account)){
                throw new AccountAlreadyExistsException("Das Konto existiert bereits schon");
            }

            //Prüft ob die übergebene Liste schon einem Account zugeordnet wurde
            if (accountToTransactions.containsValue(transactions)) {
                throw new TransactionAlreadyExistException("Die Transaktionliste existiert bereits schon");
            }

            //Prüft ob sich Dublikate in der übergebenen Liste befinden
            for(int i = 0; i < transactions.size();i++){
                for(int j = 0; j < transactions.size();j++){
                    //Funktioniert das so??
                    if(i == j ){continue;}
                    if(transactions.get(i).equals(transactions.get(j))){
                        throw new TransactionAlreadyExistException("Es gibt Duplikat in der uebergebenen Transaktionsliste");
                    }
                }
            }

            //Prüft ob sich in der übergebenen Liste falsche Klassentypen befinden
            for(int i = 0; i < transactions.size() ; i++){
                if(!(transactions.get(i)instanceof Payment) && !(transactions.get(i) instanceof Transfer)){
                    throw new TransactionAttributeException("Die uebergebene Transaktionsliste enthält falsche Klassentypen");
                }
            }

            //Passt die incoming-/ und outgoing interests an
            for(int i = 0; i < transactions.size(); i++){
                if(transactions.get(i) instanceof Payment){
                    ((Payment) transactions.get(i)).set_incomingInterest(this.incomingInterest);
                    ((Payment) transactions.get(i)).set_outgoingInterest(this.outgoingInterest);
                }
            }

            accountToTransactions.put(account, transactions);
            writeAccounts(account);
        }
    }

    /**
     * Adds a transaction to an already existing account.
     *
     * @param account     the account to which the transaction is added
     * @param transaction the transaction which should be added to the specified account
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
     */
    public void addTransaction(String account, Transaction transaction)
            throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, IOException {

        if(!accountToTransactions.containsKey(account)){
            throw new AccountDoesNotExistException("Das eingegebene Konto existiert nicht");
        }
        if(accountToTransactions.get(account).contains(transaction)){
            throw new TransactionAlreadyExistException("Die Transaktion exisitiert bereits");
        }

        if(transaction instanceof Payment){
            ((Payment) transaction).set_incomingInterest(this.incomingInterest);
            ((Payment) transaction).set_outgoingInterest(this.outgoingInterest);
        }
        accountToTransactions.get(account).add(transaction);
        writeAccounts(account);
    }

    /**
     * Removes a transaction from an account. If the transaction does not exist, an exception is
     * thrown.
     *
     * @param account     the account from which the transaction is removed
     * @param transaction the transaction which is removed from the specified account
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionDoesNotExistException if the transaction cannot be found
     */
    public void removeTransaction(String account, Transaction transaction)
            throws AccountDoesNotExistException, TransactionDoesNotExistException, IOException {
        if(!accountToTransactions.containsKey(account)){
            throw new AccountDoesNotExistException("Das Konto existiert nicht");
        }
        if(!accountToTransactions.get(account).contains(transaction)){
            throw new TransactionDoesNotExistException("Die Transaktion existiert nicht");
        }
        accountToTransactions.get(account).remove(transaction);
        writeAccounts(account);
    }

    /**
     * Checks whether the specified transaction for a given account exists.
     *
     * @param account     the account from which the transaction is checked
     * @param transaction the transaction to search/look for
     */
    public boolean containsTransaction(String account, Transaction transaction){
        return accountToTransactions.get(account).contains(transaction);
    }

    /**
     * Calculates and returns the current account balance.
     *
     * @param account the selected account
     * @return the current account balance
     */
    public double getAccountBalance(String account){
        double balance=0;

        List<Transaction> list = accountToTransactions.get(account);

        for(Transaction item:list){
            balance += item.calculate();
        }

        return balance;
    }

    /**
     * Returns a list of transactions for an account.
     *
     * @param account the selected account
     * @return the list of all transactions for the specified account
     */
    public List<Transaction> getTransactions(String account) throws AccountDoesNotExistException {
        if(!accountToTransactions.containsKey(account)){
            throw new AccountDoesNotExistException("Das eingegebene Konto existiert nicht");
        }
        return accountToTransactions.get(account);
    }

    /**
     * Returns a sorted list (-> calculated amounts) of transactions for a specific account. Sorts the list either in ascending or descending order
     * (or empty).
     *
     * @param account the selected account
     * @param asc     selects if the transaction list is sorted in ascending or descending order
     * @return the sorted list of all transactions for the specified account
     */
    public List<Transaction> getTransactionsSorted(String account, boolean asc) throws AccountDoesNotExistException {

        if(!accountToTransactions.containsKey(account)){
            throw new AccountDoesNotExistException("Das eingegebene Konto existiert nicht");
        }
        //Case leere Liste
        if(accountToTransactions.get(account).size() == 0){
            List<Transaction> list = new ArrayList<>();
            return list;
        }

        //Deep-Copy Transaktionsliste und calculate-Implementation
        List<Transaction> list = new ArrayList<Transaction>();
        for(Transaction item: accountToTransactions.get(account)){
            list.add(item);
        }

        //Liste sortieren
        List<Transaction> sorted_list = new ArrayList<>();

        while(list.size() != 0){

            double min = list.get(0).calculate();
            int index = 0;

            for(int i = 0; list.size() > i; i++){
                if(min > list.get(i).calculate()){
                    min = list.get(i).calculate();
                    index = i;
                }
            }
            sorted_list.add(list.get(index));
            list.remove(index);
        }

        if(asc)
            return sorted_list;

        //Liste ggf. umsortieren für absteigende Liste
        List<Transaction> list_desc = new ArrayList<>();

        while(sorted_list.size() !=0 ){
            int index = sorted_list.size()-1;
            list_desc.add(sorted_list.get(index));
            sorted_list.remove(index);
        }
        return list_desc;
    }

    /**
     * Returns a list of either positive or negative transactions (-> calculated amounts).
     *
     * @param account  the selected account
     * @param positive selects if positive or negative transactions are listed
     * @return the list of all transactions by type
     */
    public List<Transaction> getTransactionsByType(String account, boolean positive) throws AccountDoesNotExistException {
        if(!accountToTransactions.containsKey(account)){
            throw new AccountDoesNotExistException("Das eingegebene Konto existiert nicht");
        }

        List<Transaction> list = accountToTransactions.get(account);
        List<Transaction> list_out = new ArrayList<>();

        if(positive){
            for(int i = 0; i < list.size(); i++){
                if(list.get(i).calculate() > 0)
                    list_out.add(list.get(i));
            }
        }
        else{
            for(int i = 0; i < list.size(); i++){
                    if(list.get(i).calculate() < 0)
                    list_out.add(list.get(i));
            }
        }

        return list_out;
    }
}
