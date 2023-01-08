package ui;

        import javafx.fxml.FXMLLoader;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.stage.Stage;

        import java.io.IOException;

public class FxApplication extends javafx.application.Application{

    public static void main(String[] args){ launch(args);}

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent anwendung =(Parent) new FXMLLoader(getClass().getClassLoader().getResource("mainview.fxml")).load();
            primaryStage.setScene(new Scene(anwendung));
            primaryStage.setTitle("Mainview");
            primaryStage.show();
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
