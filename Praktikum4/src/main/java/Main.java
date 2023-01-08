import bank.*;
import bank.exceptions.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

class Main{

    public static void main(String args[]) {

            try {

                //Testtransaktionen anlegen
                //####################################################################################################################################################
                IncomingTransfer inTrans1 = new IncomingTransfer("30.09.1994", 30.20,"Incoming-Test 1","Sender","Empfaenger");
                IncomingTransfer intrans2 = new IncomingTransfer("01.11.2022", 3000.12,"Incoming-Test 2","Sender","Empfaenger");
                OutgoingTransfer outTrans1 = new OutgoingTransfer("01.10.2000", 50.00,"Outgoing-Test 1","Sender","Empfaenger");
                OutgoingTransfer outTrans2 = new OutgoingTransfer("29.02.2021", 100.30,"Outgoing-Test 1","Sender","Empfaenger");

                Payment payNeg1 = new Payment("08.11.2022", -50.00, "Payment-Test Negative 1", 0.2, 0.1);
                Payment payNeg2 = new Payment("09.11.2022", -60.00, "Payment-Test Negative 2", 0.1, 0.4);
                Payment payNeg3 = new Payment("10.11.2022", -70.00, "Payment-Test Negative 3", 0.1, 0.1);
                Payment payNeg4 = new Payment("11.11.2022", -80.00, "Payment-Test Negative 4", 0.05, 0.05);

                Payment payPos1 = new Payment("08.11.2022", 10, "Payment-Test Positiv 1", 0.1, 0.1);
                Payment payPos2 = new Payment("09.11.2022", 20, "Payment-Test Positiv 2", 0.2, 0.2);
                Payment payPos3 = new Payment("10.11.2022", 30, "Payment-Test Positiv 3", 0.3, 0.3);
                Payment payPos4 = new Payment("11.11.2022", 40, "Payment-Test Positiv 4", 0.02, 0.01);


                //Testlisten anlegen
                //####################################################################################################################################################
                List<Transaction> testlist_1_PrivateBank = new ArrayList<Transaction>();
                List<Transaction> testlist_2_PrivateBank = new ArrayList<Transaction>();
                List<Transaction> testlist_loeschen = new ArrayList<Transaction>();

                //Testliste zum Löschen
                testlist_loeschen.add(intrans2);
                testlist_loeschen.add(outTrans2);
                testlist_loeschen.add(payNeg3);
                testlist_loeschen.add(payNeg4);
                testlist_loeschen.add(payPos2);
                testlist_loeschen.add(payPos3);

                //Erste Testliste für PrivateBank
                testlist_1_PrivateBank.add(inTrans1);   // 30,20
                testlist_1_PrivateBank.add(outTrans1);  //-50,00
                testlist_1_PrivateBank.add(payNeg1);    //-50,00
                testlist_1_PrivateBank.add(payNeg2);    //-60,00
                testlist_1_PrivateBank.add(payPos1);    // 10,00
                testlist_1_PrivateBank.add(payPos2);    // 20,00

                //Zweite Testliste für PrivateBank
                testlist_2_PrivateBank.add(intrans2);
                testlist_2_PrivateBank.add(outTrans2);
                testlist_2_PrivateBank.add(payNeg3);
                testlist_2_PrivateBank.add(payNeg4);
                testlist_2_PrivateBank.add(payPos3);
                testlist_2_PrivateBank.add(payPos4);


                //Testbanken anlegen
                //####################################################################################################################################################
                //PrivateBank sparkasse = new PrivateBank("Sparkasse", 0.3,0.2,"C:/Users/info-/IdeaProjects/Praktikum4/src/main/resources/sparkasse");
                PrivateBank deutsche_bank = new PrivateBank("Deutsche Bank", 0.2,0.1,"C:/Users/info-/IdeaProjects/Praktikum4/src/main/resources/deutsche_bank");


                //System.out.println("Test: Banken anlegen ##############################################################\n");
                //System.out.println(sparkasse);
                //System.out.println(deutsche_bank);


                //Test Accounts anlegen
                //####################################################################################################################################################
                //deutsche_bank.createAccount("Toms Account", testlist_1_PrivateBank);
                //deutsche_bank.createAccount("Annas Account", testlist_2_PrivateBank);
                //System.out.println(deutsche_bank.getTransactions("Toms Account"));

                //System.out.println(deutsche_bank.getTransactions("Annas Account"));
                //System.out.println(deutsche_bank.get_accountNameList());

                //deutsche_bank.createAccount("Welfs Account", testlist_loeschen);

                //deutsche_bank.deleteAccount("Welfs Account");

            }catch(AccountAlreadyExistsException e) {
                System.out.println(e);
            //}catch(AccountDoesNotExistException e){
            //    System.out.println(e);
            }catch(TransactionAlreadyExistException e){
                System.out.println(e);
            }catch(TransactionAttributeException e){
                System.out.println(e);
            //}catch(TransactionDoesNotExistException e){
            //    System.out.println(e);
            } catch (IOException e) {
                e.printStackTrace();
            }


    }
}