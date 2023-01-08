import bank.IncomingTransfer;
import bank.OutgoingTransfer;
import bank.Payment;
import bank.Transfer;
import bank.exceptions.TransactionAttributeException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferTest {
    Transfer transferTest = new Transfer("12.12.2022",300,"Test-Transaktion","Welf","Lukas");

    @BeforeEach
    void init(){
        transferTest = new Transfer("12.12.2022",300,"Test-Transaktion","Welf","Lukas");
    }

    @Test
    void testConstructor() {
        assertEquals(transferTest.get_date(),"12.12.2022");
        assertEquals(transferTest.get_amount(),300);
        assertEquals(transferTest.get_description(),"Test-Transaktion");
        assertEquals(transferTest.get_sender(),"Welf");
        assertEquals(transferTest.get_recipient(),"Lukas");
    }

    @Test
    void testCopyConstructor() {
        Transfer transfertCopy = new Transfer(transferTest);
        assertNotNull(transfertCopy);

        assertEquals(transferTest.get_date(),transfertCopy.get_date());
        assertEquals(transferTest.get_amount(),transfertCopy.get_amount());
        assertEquals(transferTest.get_description(),transfertCopy.get_description());
        assertEquals(transferTest.get_sender(),transfertCopy.get_sender());
        assertEquals(transferTest.get_recipient(),transfertCopy.get_recipient());
    }

    @Test
    void testSetter(){
        transferTest.set_date("10.10.2012");
        transferTest.set_description("Test Test Test");
        transferTest.set_sender("Lara");
        transferTest.set_recipient("Lea");

        assertEquals(transferTest.get_date(),"10.10.2012");
        assertEquals(transferTest.get_description(),"Test Test Test");
        assertEquals(transferTest.get_sender(),"Lara");
        assertEquals(transferTest.get_recipient(),"Lea");
    }

    @ParameterizedTest
    @ValueSource(doubles = {50,-50})
    void testAmount(double val) throws TransactionAttributeException {

        if(val <= 0){
            assertThrows(TransactionAttributeException.class, ()-> {transferTest.set_amount(val);});
        }
        if(val > 0) {
            transferTest.set_amount(val);
            assertEquals(transferTest.get_amount(), 50);
        }
    }

    @Test
    void testCalculate() {
        assertEquals(transferTest.calculate(),transferTest.get_amount());

    }

    @Test
    void testEquals() {

        assertTrue(transferTest.equals(transferTest));

        Transfer transferCopy = new Transfer(transferTest);
        assertTrue(transferTest.equals(transferCopy));


        transferTest.set_description("Nope");
        transferTest.set_date("10.10.2010");

        assertFalse(transferTest.equals(transferCopy));
    }

    @Test
    void testToString(){
        String check =
                "**************** " + transferTest.getClass().getSimpleName() + " ****************\n"+
                        "Date: " + transferTest.get_date() + "\n" +
                        "Amount: " + transferTest.calculate() + "\n" +
                        "Description: " + transferTest.get_description() + "\n" +
                        "Sender: " + transferTest.get_sender() +"\n" +
                        "Recipient: " + transferTest.get_recipient() + "\n" +
                        "*****************************************\n";

        assertEquals(transferTest.toString(),check);
    }
}
