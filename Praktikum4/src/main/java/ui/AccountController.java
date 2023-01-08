package ui;

import bank.*;
import bank.exceptions.AccountDoesNotExistException;
import bank.exceptions.TransactionAlreadyExistException;
import bank.exceptions.TransactionAttributeException;
import bank.exceptions.TransactionDoesNotExistException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AccountController implements Initializable {

    // Eine Liste, die es den ermöglicht, Änderungen zu verfolgen, wenn sie auftreten.
    private ObservableList<Transaction> Transaktionsliste = FXCollections.observableArrayList();
    private PrivateBank privatebank;
    private String account;
    private Scene scene;

    /**
     * Funktion zum Wechsel zur ersten Scene (Konto auswählen)
     * @throws IOException falls FXML nicht geladen werden kann.
     */
    @FXML
    public void switchToScene1() throws IOException {
        Parent root = new FXMLLoader(getClass().getClassLoader().getResource("mainview.fxml")).load();
        //Stage stage = StageHolder.getInstance().getStage();
        Stage stage= (Stage) BankName.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private Text BankName;
    @FXML
    private Text TextKontoName;
    @FXML
    private Text TextAmount;
    @FXML
    private ListView<Transaction> ListViewTransaktionen;
    @FXML
    private Button ButtonAufsteigend;
    @FXML
    private Button ButtonAbsteigend;
    @FXML
    private Button ButtonPositiv;
    @FXML
    private Button ButtonNegativ;

    /**
     * Löscht die ausgewählte Transkation nach Bestätigung im Dialogfenster (alert)
     * Aktualisiert die Transaktionsliste und den Kontostand nach dem löschen der Transaktion.
     */
    @FXML
    private void transaktionloeschen(){
        Transaction gewählteTransaktion = ListViewTransaktionen.getSelectionModel().getSelectedItem();

        //TRANSAKTION LÖSCHEN
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Transaktion Löschen");
        alert.setHeaderText("Transaktion Löschen:");
        alert.setContentText("Soll die folgende Transaktion gelöscht werden? \n" + gewählteTransaktion);
        alert.getDialogPane().setStyle(" -fx-max-width:500px; -fx-max-height: 200px; -fx-pref-width: 500px; -fx-pref-height: 200px;");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                privatebank.removeTransaction(account, gewählteTransaktion);
                updateListView(privatebank.getTransactions(account));
                TextAmount.setText(String.format("%1.2f",privatebank.getAccountBalance(account)) + " €");
            } catch (TransactionDoesNotExistException e) {
                e.printStackTrace();
                fehlermeldung(e.getMessage());
            } catch (AccountDoesNotExistException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    /**
     * Öffnet ein Dialogfenster zum erstellen einer neuen Transaktion.
     * Nutzer hat hier die Auswahlmöglichkeit was für eine Transaktion(Überweisung, Ein-/Auszahlung) erstellt werden soll.
     * Je nach Auswahl (weitere Funktion) wird ein weiteres Dialogfenster aufgerufen.
     */
    @FXML
    public void newTransaction() throws AccountDoesNotExistException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Neue Transaktion erstellen:");
        alert.setHeaderText("Neue Transaktion erstellen:");
        alert.setContentText("Was soll erstellt werden?");

        ButtonType ueberweisung = new ButtonType("Überweisung");
        ButtonType einauszahlung = new ButtonType("Ein-/Auszahlung");
        ButtonType abbrechen = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(ueberweisung, einauszahlung, abbrechen);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ueberweisung){
            newTransfer();
        } else if (result.isPresent() && result.get() == einauszahlung) {
            newPayment();
        }
    }

    /**
     * Zeigt Dialogfenster zur Eingabe einer neuen Überweisung an.
     * Erst werden die Felder über ein Grid erstellt und danach wird überprüft ob die Eingaben korrekt sind.
     * Je nach Eingabe von Sender und Empfänger wird eine Überweisung (Ein- oder Ausgehend) erstellt und die Transaktionlsiste so wie der Kontostand aktualisiert.
     * Exceptions werden ggf. in einem Dialogfenster ausgegeben.
     */
    private void newTransfer() throws AccountDoesNotExistException {
        //DIALOG FENSTER
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Überweisung anlegen");
        dialog.setHeaderText("Neue Überweisung anlegen:");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResizable(false);

        //GRID BAUEN
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        TextField date = new TextField();
        TextField amount = new TextField();
        TextField description = new TextField();
        TextField sender = new TextField();
        TextField recipient = new TextField();

        grid.add(new Label("Datum:"), 0, 0);        grid.add(date, 1, 0);
        grid.add(new Label("Betrag:"), 0, 1);       grid.add(amount, 1, 1);
        grid.add(new Label("Beschreibung:"), 0, 2); grid.add(description, 1, 2);
        grid.add(new Label("Sender:"), 0, 3);       grid.add(sender, 1, 3);
        grid.add(new Label("Empfänger:"), 0, 4);    grid.add(recipient, 1, 4);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();

        //ABFRAGE
        if (result.get() == ButtonType.OK) {
            if((date.getText().equals("")||
                    amount.getText().equals("") ||
                    description.getText().equals("") ||
                    sender.getText().equals("") ||
                    recipient.getText().equals(""))
            ){
                fehlermeldung("Ungültige Eingabe, bitte erneut versuchen!");
                newTransfer();
            }else if(Double.parseDouble(amount.getText())<0){
                fehlermeldung("Negative Überweisungen nicht möglich!");
                newTransfer();
            }else if(sender.getText().equals(recipient.getText())){
                fehlermeldung("Sender und Empfänger dürfen nicht gleich sein!");
                newTransfer();
            }else if(!sender.getText().equals(account)&&!recipient.getText().equals(account)){
                fehlermeldung("Sender oder Empfänger muss Kontoinhaber sein!");
                newTransfer();
            }else if(account.equals(sender.getText())){ //Wenn Sender = Kontoinhaber
                try {
                    privatebank.addTransaction(account, new OutgoingTransfer(date.getText(),Double.parseDouble(amount.getText()),description.getText(),account,recipient.getText()));
                } catch (TransactionAlreadyExistException | AccountDoesNotExistException | IOException e) {
                    fehlermeldung(e.getMessage());
                } catch (TransactionAttributeException e) {
                    throw new RuntimeException(e);
                }
            }else { //Wenn Empfänger = Kontoinhaber
                try {
                    privatebank.addTransaction(account, new IncomingTransfer(date.getText(), Double.parseDouble(amount.getText()), description.getText(), sender.getText(), account));
                } catch (TransactionAlreadyExistException | AccountDoesNotExistException | IOException e) {
                    fehlermeldung(e.getMessage());
                } catch (TransactionAttributeException e) {
                    throw new RuntimeException(e);
                }
            }
            //LISTVIEW UND KONTOSTAND AKTUALISIEREN
            updateListView(privatebank.getTransactions(account));
            TextAmount.setText(String.format("%1.2f",privatebank.getAccountBalance(account)) + " €");
        }
    }

    /**
     * Zeigt Dialogfenster zur Eingabe einer neuen Ein-/Auszahlung an.
     * Erst werden die Felder über ein Grid erstellt und danach wird überprüft ob die Eingaben korrekt sind.
     * Je nach Auswahl über die Buttons wird eine Überweisung (Ein- oder Ausgehend) erstellt und die Transaktionlsiste so wie der Kontostand aktualisiert.
     * Exceptions werden ggf. in einem Dialogfenster ausgegeben.
     */
    private void newPayment() throws AccountDoesNotExistException {
        //DIALOG FENSTER
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Ein-/Auszahlung anlegen");
        alert.setHeaderText("Neue Ein-/Auszahlung anlegen:");
        alert.setResizable(false);

        ButtonType einzahlung = new ButtonType("Einzahlung");
        ButtonType auszahlung = new ButtonType("Auszahlung");
        ButtonType abbrechen = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(einzahlung, auszahlung, abbrechen);

        //GRID BAUEN
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        TextField date = new TextField();
        TextField amount = new TextField();
        TextField description = new TextField();
        TextField incomingInterest = new TextField();
        TextField outgoingInterest = new TextField();

        incomingInterest.setEditable(false);
        outgoingInterest.setEditable(false);
        incomingInterest.setText(String.format("%1.2f",privatebank.get_incomingInterest()) + " %");
        outgoingInterest.setText(String.format("%1.2f",privatebank.get_outgoingInterest()) + " %");

        grid.add(new Label("Datum:"), 0, 0);            grid.add(date, 1, 0);
        grid.add(new Label("Betrag:"), 0, 1);           grid.add(amount, 1, 1);
        grid.add(new Label("Beschreibung:"), 0, 2);     grid.add(description, 1, 2);
        grid.add(new Label("Gebühren Eingang:"), 0, 3); grid.add(incomingInterest, 1, 3);
        grid.add(new Label("Gebühren Ausgang:"), 0, 4); grid.add(outgoingInterest, 1, 4);

        alert.getDialogPane().setContent(grid);

        Optional<ButtonType> result = alert.showAndWait();

        //ABFRAGE
        int auswahl=1;
        if (result.isPresent() && result.get() == auszahlung) auswahl=-1;

        if (result.isPresent() && result.get() == einzahlung || result.isPresent() && result.get() == auszahlung) {
            if(date.getText().equals("")||
                    amount.getText().equals("") ||
                    description.getText().equals("")
            ){
                fehlermeldung("Ungültige Eingabe, bitte erneut versuchen!");
                newPayment();
            }else {
                try {
                    privatebank.addTransaction(account, new Payment(date.getText(),
                            Double.parseDouble(amount.getText())*auswahl,description.getText(),
                            privatebank.get_incomingInterest(),privatebank.get_outgoingInterest()));
                } catch (AccountDoesNotExistException | TransactionAlreadyExistException | IOException e) {
                    fehlermeldung(e.getMessage());
                } catch (TransactionAttributeException e) {
                    throw new RuntimeException(e);
                }
            }

            //LISTVIEW UND KONTOSTAND AKTUALISIEREN
            updateListView(privatebank.getTransactions(account));
            TextAmount.setText(String.format("%1.2f",privatebank.getAccountBalance(account)) + " €");
        }
    }

    /**
     * Funktion zum Aufrufen einer Fehlermeldung.
     *
     * @param ursache Grund der Fehlermeldung
     */
    public void fehlermeldung(String ursache) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText("Fehler");
        alert.setContentText(ursache);
        alert.showAndWait();
    }

    /**
     * Aktualisierung der Listeview (Aufzählung der Transaktionen in der Bank)
     */
    private void updateListView(List<Transaction> listTransaction) {
        Transaktionsliste.clear();
        Transaktionsliste.addAll(listTransaction);
        ListViewTransaktionen.setItems(Transaktionsliste);
    }

    /**
     * Initialisierung des controllers
     * aktualisiert die Transaktionslisteliste beim Start
     * setzt Namen der Bank sowie das vorher ausgewählte Konto und den Kontostand
     * aktualisiert je nach Button die Sortierung der Transaktionsliste
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //ÜBERGABE VON SCENE 1
        privatebank = UserHolder.getInstance().getBank();
        account = UserHolder.getInstance().getUserName();

        BankName.setText(privatebank.get_name());
        TextKontoName.setText(account);
        TextAmount.setText(String.format("%1.2f",privatebank.getAccountBalance(account)) + " €");

        try {
            updateListView(privatebank.getTransactions(account));
        } catch (AccountDoesNotExistException e) {
            e.printStackTrace();
        }

        //SORTIERUNGEN
        ButtonAufsteigend.setOnAction(event -> {
            try {
                updateListView(privatebank.getTransactionsSorted(account, true));
            } catch (AccountDoesNotExistException e) {
                e.printStackTrace();
            }
        });
        ButtonAbsteigend.setOnAction(event -> {
            try {
                updateListView(privatebank.getTransactionsSorted(account, false));
            } catch (AccountDoesNotExistException e) {
                e.printStackTrace();
            }
        });
        ButtonPositiv.setOnAction(event -> {
            try {
                updateListView(privatebank.getTransactionsByType(account, true));
            } catch (AccountDoesNotExistException e) {
                e.printStackTrace();
            }
        });
        ButtonNegativ.setOnAction(event -> {
            try {
                updateListView(privatebank.getTransactionsByType(account, false));
            } catch (AccountDoesNotExistException e) {
                e.printStackTrace();
            }
        });
    }
}
