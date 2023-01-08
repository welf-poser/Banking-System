package ui;

import javafx.stage.Stage;

/** Singleton Pattern
 * Einzelnes globales Objekt zur Übergabe der Stage
 */
public class StageHolder {
    private Stage stage;
    private static StageHolder instance = new StageHolder();

    // Konstructor
    private StageHolder() {}

    /**
     * @return statisches Objekt (Stage)
     */
    public static StageHolder getInstance() {
        return instance;
    }

    /**
     * @return gibt die Stage zurück
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * @param stage setzt die Stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
