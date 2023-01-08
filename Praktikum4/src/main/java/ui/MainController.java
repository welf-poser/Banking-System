package ui;

import bank.PrivateBank;
import bank.exceptions.AccountAlreadyExistsException;
import bank.exceptions.AccountDoesNotExistException;
import bank.exceptions.TransactionAlreadyExistException;
import bank.exceptions.TransactionAttributeException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private Scene scene;

    private PrivateBank Bank = new PrivateBank("Sparkasse", 0.2, 0.1, "C:\\Users\\info-\\IdeaProjects\\Praktikum4\\src\\main\\resources\\sparkasse");
    private ObservableList<String> KontoListe = FXCollections.observableArrayList();

    @FXML
    private Text TextBankName;
    @FXML
    private ListView<String> ListViewKonten; //Nach Aufgabenstellung

    public MainController() throws TransactionAlreadyExistException, AccountAlreadyExistsException, TransactionAttributeException, IOException {
    }

    /**
     * Funktion zum Wechsel zur zweiten Scene (Ausgewähltes Konto)
     * @throws IOException falls FXML nicht geladen werden kann.
     */
    @FXML
    public void switchToScene2() throws IOException {
        //Kontoauswahl speichern
        UserHolder userInstance = UserHolder.getInstance();
        String userName = ListViewKonten.getSelectionModel().getSelectedItem();
        PrivateBank bankName = Bank;

        //Instanzen für Szeneübergabe setzen
        userInstance.setUserName(userName);
        userInstance.setBank(bankName);

        Parent root = new FXMLLoader(getClass().getClassLoader().getResource("accountview.fxml")).load();

        //Stage stage = StageHolder.getInstance().getStage();
        Stage stage= (Stage) TextBankName.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Zeigt Dialogfenster und löscht das nach Bestätigung das ausgewählte Konto
     * gibt im Dialogfenster ggf. Exception-Fehlermeldungen aus.
     */
    @FXML
    private void kontoLoeschen() {
        String gewähltesKonto = ListViewKonten.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konto Löschen");
        alert.setHeaderText("Konto Löschen:");
        alert.setContentText("Soll \"" + gewähltesKonto + "\" gelöscht werden?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK){
            try {
                Bank.deleteAccount((gewähltesKonto));
                updateListView();
            } catch (AccountDoesNotExistException | IOException e) {
                e.printStackTrace();
                fehlermeldung(e.getMessage());
            }
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    /**
     * Zeigt Dialogfenster und fügt nach Eingabe ein neues Konto hinzu.
     * gibt im Dialogfenster ggf. Exception-Fehlermeldungen aus.
     */
    @FXML
    private void kontohinzufuegen() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Neues Bankkonto");
        dialog.setHeaderText("Bitte neues Bankkonto eingeben: ");
        dialog.setContentText("Name: ");
        dialog.getDialogPane().setMinWidth(300);

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (Objects.equals(result.get(), "")) {
                fehlermeldung("Ungültige Eingabe!");
                kontohinzufuegen();
            }else{
                try {
                    Bank.createAccount(result.get());
                    updateListView();
                } catch (AccountAlreadyExistsException | IOException e) {
                    e.printStackTrace();
                    fehlermeldung(e.getMessage());
                }
            }
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
     * Aktualisierung der Listeview (Aufzählung der Konten in der Bank)
     */
    private void updateListView() {
        KontoListe.clear();
        KontoListe.addAll(Bank.get_accountNameList());
        ListViewKonten.setItems(KontoListe);
    }

    /**
     * Initialisierung des controllers
     * aktualisiert die Kontoliste beim Start und setzt Namen der Bank
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateListView();
        TextBankName.setText(Bank.get_name());
    }
}
