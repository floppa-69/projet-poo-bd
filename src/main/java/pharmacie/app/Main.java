package pharmacie.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("Démarrage de l'application...");
        try {
            Parent root = loadFXML("login");
            System.out.println("FXML de connexion chargé.");
            scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.setTitle("Système de Gestion de Pharmacie");
            stage.show();
            System.out.println("Fenetre affichée.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        System.out.println("Lancement de JavaFX...");
        launch();
    }
}
