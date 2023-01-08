import bank.Payment;
import bank.PrivateBank;
import bank.Transaction;
import bank.exceptions.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrivateBankTest {
    PrivateBank deutsche_bank = new PrivateBank("Deutsche Bank", 0.1,0.2,"C:\\Users\\info-\\IdeaProjects\\Praktikum4\\test\\resources\\deutsche_bank");

    public PrivateBankTest() throws TransactionAlreadyExistException, AccountAlreadyExistsException, TransactionAttributeException, IOException {
    }

    Path sourceDirectoryAnna = Paths.get("C:\\Users\\info-\\IdeaProjects\\Praktikum4\\src\\main\\resources\\deutsche_bank\\Annas Account.json");
    Path sourceDirectoryTom = Paths.get("C:\\Users\\info-\\IdeaProjects\\Praktikum4\\src\\main\\resources\\deutsche_bank\\Toms Account.json");
    Path targetDirectoryAnna = Paths.get("C:\\Users\\info-\\IdeaProjects\\Praktikum4\\test\\resources\\deutsche_bank\\Annas Account.json");
    Path targetDirectoryTom = Paths.get("C:\\Users\\info-\\IdeaProjects\\Praktikum4\\test\\resources\\deutsche_bank\\Toms Account.json");

    @BeforeEach
    void init() throws TransactionAlreadyExistException, AccountAlreadyExistsException, TransactionAttributeException, IOException {

        //copy source to target using Files Class
        Files.copy(sourceDirectoryAnna, targetDirectoryAnna);
        Files.copy(sourceDirectoryTom, targetDirectoryTom);

        deutsche_bank = new PrivateBank("Deutsche Bank", 0.1,0.2,"C:\\Users\\info-\\IdeaProjects\\Praktikum4\\test\\resources\\deutsche_bank");
    }

    @AfterEach
    void cleanFiles() throws IOException {

        File[] files = new File("C:\\Users\\info-\\IdeaProjects\\Praktikum4\\test\\resources\\deutsche_bank").listFiles();
         for(File file: files)
            if (!file.isDirectory())
                file.delete();
    }

    @Test
    void constructorTest(){
        assertEquals(deutsche_bank.get_name(),"Deutsche Bank");
        assertEquals(deutsche_bank.get_incomingInterest(),0.1);
        assertEquals(deutsche_bank.get_outgoingInterest(),0.2);
        assertEquals(deutsche_bank.get_directoryName(),"C:\\Users\\info-\\IdeaProjects\\Praktikum4\\test\\resources\\deutsche_bank");
        assertEquals(deutsche_bank.get_accountNameList().size(),2);
    }

    @Test
    void copyConstructorTest(){
        PrivateBank vr_bank = new PrivateBank(deutsche_bank);
        assertNotNull(vr_bank);

        //Deep-Copy Test
        assertEquals(vr_bank.get_name(),deutsche_bank.get_name());
        vr_bank.set_name("VR Bank");
        assertNotEquals(vr_bank.get_name(),deutsche_bank.get_name());

        assertEquals(vr_bank.get_incomingInterest(),deutsche_bank.get_incomingInterest());
        assertEquals(vr_bank.get_outgoingInterest(),deutsche_bank.get_outgoingInterest());

        //Deep-Copy Test
        assertEquals(vr_bank.get_directoryName(),deutsche_bank.get_directoryName());
        vr_bank.set_directoryName("Nope");
        assertNotEquals(vr_bank.get_directoryName(),deutsche_bank.get_directoryName());
    }

    @Test
    void createAccountTest() throws AccountAlreadyExistsException, IOException {
        deutsche_bank.createAccount("Sofies Account");
        assertEquals(deutsche_bank.get_accountNameList().size(), 3);

        assertThrows(AccountAlreadyExistsException.class,()->deutsche_bank.createAccount("Annas Account"));
    }

    @Test
    void TransactionHandlingTest() throws TransactionAttributeException, TransactionAlreadyExistException, AccountDoesNotExistException, IOException, TransactionDoesNotExistException {
        Payment pay = new Payment("10.10.2010",100,"Wehnachtsmarkt",0.2,0.3);

        //addTransaction
        deutsche_bank.addTransaction("Annas Account", pay);
        assertTrue(deutsche_bank.containsTransaction("Annas Account", pay));

        //removeTransaction
        deutsche_bank.removeTransaction("Annas Account", pay);
        assertFalse(deutsche_bank.containsTransaction("Annas Account", pay));

        deutsche_bank.addTransaction("Annas Account", pay);
        //Exceptions
        assertThrows(TransactionAlreadyExistException.class, ()->deutsche_bank.addTransaction("Annas Account", pay));

        deutsche_bank.removeTransaction("Annas Account",pay);
        assertThrows(TransactionDoesNotExistException.class,()->deutsche_bank.removeTransaction("Annas Account",pay));

        assertThrows(AccountDoesNotExistException.class,()->deutsche_bank.addTransaction("Nope",pay));
        assertThrows(AccountDoesNotExistException.class,()->deutsche_bank.removeTransaction("Nope",pay));
    }

    @Test
    void getAccountBalanceTest(){
        assertEquals(30.20-50-50*1.2-60*1.2+10*0.9+20*0.9,deutsche_bank.getAccountBalance("Toms Account"));
    }

    @Test
    void getTransactionTest() throws AccountDoesNotExistException {
        assertThrows(AccountDoesNotExistException.class,()->deutsche_bank.getTransactions("Nope"));
        assertEquals(deutsche_bank.getTransactions("Toms Account").size(),6);
    }

    @Test
    void getTransactionSortedTest() throws AccountDoesNotExistException {
        assertThrows(AccountDoesNotExistException.class,()->deutsche_bank.getTransactions("Nope"));

        List<Transaction> list = deutsche_bank.getTransactionsSorted("Annas Account", true);
        assertTrue(list.get(0).calculate() <= list.get(1).calculate());
        assertTrue(list.get(1).calculate() <= list.get(2).calculate());
        assertTrue(list.get(2).calculate() <= list.get(3).calculate());
        assertTrue(list.get(3).calculate() <= list.get(4).calculate());
        assertTrue(list.get(4).calculate() <= list.get(5).calculate());

        list = deutsche_bank.getTransactionsSorted("Annas Account", false);
        assertTrue(list.get(0).calculate() >= list.get(1).calculate());
        assertTrue(list.get(1).calculate() >= list.get(2).calculate());
        assertTrue(list.get(2).calculate() >= list.get(3).calculate());
        assertTrue(list.get(3).calculate() >= list.get(4).calculate());
        assertTrue(list.get(4).calculate() >= list.get(5).calculate());
    }

    @Test
    void getTransactionsByTypeTest() throws AccountDoesNotExistException{
        assertThrows(AccountDoesNotExistException.class,()->deutsche_bank.getTransactions("Nope"));

        List<Transaction> list = deutsche_bank.getTransactionsByType("Toms Account", true);

        assertTrue(list.get(0).calculate() > 0);
        assertTrue(list.get(1).calculate() > 0);
        assertTrue(list.get(2).calculate() > 0);


        list = deutsche_bank.getTransactionsByType("Toms Account", false);
        assertTrue(list.get(0).calculate() < 0);
        assertTrue(list.get(1).calculate() < 0);
        assertTrue(list.get(2).calculate() < 0);
    }

    @Test
    void equalsTest(){

        assertTrue(deutsche_bank.equals(deutsche_bank));

        PrivateBank vr_bank = new PrivateBank(deutsche_bank);
        assertFalse(deutsche_bank.equals(vr_bank));
    }

    @Test
    void toStringTest(){
        assertEquals(deutsche_bank.toString(),"**************** " + deutsche_bank.getClass().getSimpleName() + " ****************\n"+
                "Name: " + deutsche_bank.get_name() + "\n" +
                "Incoming Interest: " + deutsche_bank.get_incomingInterest() +"\n" +
                "Outgoing Interest: " + deutsche_bank.get_outgoingInterest() + "\n" +
                "Directory: " + deutsche_bank.get_directoryName() + "\n" +
                "*****************************************\n");
    }


}
