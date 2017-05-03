package fx.application;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ConnexionToJadeApplication extends Application {
    private ContainerController containerController;
    @SuppressWarnings("FieldCanBeLocal")
    private static String SECONDARY_PROPERTIES_FILE = "properties/second.properties";

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fx/application/connexion-jade.fxml"));
        Pane root = loader.load();
        ConnexionToJadeController controller = loader.getController();
        createAgentContainer(controller);
        stage.setTitle("Connexion - Jade");
        Scene scene = new Scene(root, 500, 300);
        stage.setScene(scene);
        stage.setOnCloseRequest(evt -> stopAgentContainer());
        stage.show();
    }

    /**
     * Crée le container secondaire
     */
    private void createAgentContainer(ConnexionToJadeController controller) {
        Runtime rt = Runtime.instance();
        ProfileImpl p;
        try {
            p = new ProfileImpl(SECONDARY_PROPERTIES_FILE);
            ContainerController cc = rt.createAgentContainer(p);

            AgentController ac = cc.createNewAgent("jade",
                    "jade.agent.ConnexionJadeAgent", new Object[]{controller});
            ac.start();
            containerController = cc;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void stopAgentContainer() {
        try {
            containerController.kill();
            Thread.sleep(500);
            System.exit(0);
        } catch (StaleProxyException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
