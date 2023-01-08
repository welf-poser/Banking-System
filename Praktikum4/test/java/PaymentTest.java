import bank.Payment;
import bank.exceptions.TransactionAttributeException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

    Payment paymentTest = new Payment("12.12.2022",1000.0,"Test-Einzahlung",0.5,0.2);

    public PaymentTest() throws TransactionAttributeException {
    }

    /**
     * Initialisiere payment_test vor jedem Test neu
     */
    @BeforeEach
    void init() throws TransactionAttributeException {
        paymentTest = new Payment("12.12.2022",1000.0,"Test-Einzahlung",0.5,0.2);
    }

    @Test
    void testConstructor() {
        assertEquals(paymentTest.get_date(),"12.12.2022");
        assertEquals(paymentTest.get_amount(),          1000.0);
        assertEquals(paymentTest.get_description(),     "Test-Einzahlung");
        assertEquals(paymentTest.get_incomingInterest(),0.5);
        assertEquals(paymentTest.get_outgoingInterest(),0.2);
    }

    @Test
    void testCopyConstructor() {
        Payment paymentCopy = new Payment(paymentTest);
        assertNotNull(paymentCopy);

        assertEquals(paymentTest.get_date(),paymentCopy.get_date());
        assertEquals(paymentTest.get_amount(),paymentCopy.get_amount());
        assertEquals(paymentTest.get_description(),paymentCopy.get_description());
        assertEquals(paymentTest.get_incomingInterest(),paymentCopy.get_incomingInterest());
        assertEquals(paymentTest.get_outgoingInterest(),paymentCopy.get_outgoingInterest());
    }

    @ParameterizedTest
    @ValueSource(doubles = {50,-50})
    void testCalculate(double val) throws TransactionAttributeException {
        paymentTest.set_amount(val);

        if(val > 0){
            assertEquals(paymentTest.calculate(),25);
        }
        if(val < 0){
            assertEquals(paymentTest.calculate(),-60);
        }
    }

    @Test
    void testEquals() {

        assertTrue(paymentTest.equals(paymentTest));

        Payment paymentCopy = new Payment(paymentTest);
        assertTrue(paymentTest.equals(paymentCopy));


        paymentCopy.set_description("Nope");
        assertFalse(paymentTest.equals(paymentCopy));
    }

    @Test
    void testToString(){
        String check =
                        "**************** " + paymentTest.getClass().getSimpleName() + " ****************\n"+
                        "Date: " + paymentTest.get_date() + "\n" +
                        "Amount: " + paymentTest.calculate() + "\n" +
                        "Description: " + paymentTest.get_description() + "\n" +
                        "Incoming Interest: " + paymentTest.get_incomingInterest() +"\n" +
                        "Outgoing Interest: " + paymentTest.get_outgoingInterest() + "\n" +
                        "*****************************************\n";

        assertEquals(paymentTest.toString(),check);
    }

    @ParameterizedTest
    @ValueSource(doubles= {1.1,-0.1})
    void testIncoming_OutgoingInterest(double val){
        assertThrows(TransactionAttributeException.class,()->paymentTest.set_outgoingInterest(val) );
        assertThrows(TransactionAttributeException.class,()->paymentTest.set_incomingInterest(val));
    }
}

