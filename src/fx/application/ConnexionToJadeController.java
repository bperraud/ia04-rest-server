package fx.application;

import jade.agent.ConnexionJadeAgent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ConnexionToJadeController {
    private ConnexionJadeAgent agent;
    @FXML
    Button contact;
    @FXML
    TextField content;
    @FXML
    TextArea agentAnswers;

    public void setAgent(ConnexionJadeAgent agent) {
        this.agent = agent;
        initializeAgent();
        System.out.println("ConnexionJadeController --> ConnexionJadeGuiAgent connected");
    }

    public void initialize() {
        contact.setOnAction(evt -> sendAdminAgent(content.getText()));
    }

    private void initializeAgent() {
        agent.messageAnswerProperty().addListener((obsv, oldv, newv) -> displayAnswer(newv));
    }

    private void sendAdminAgent(String content) {
        agent.sendAdminAgent(content);
    }

    private void displayAnswer(String message) {
        agentAnswers.setText(agentAnswers.getText() + message + "\n");
    }
}
