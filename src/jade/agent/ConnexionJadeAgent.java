package jade.agent;

import fx.application.ConnexionToJadeController;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ConnexionJadeAgent extends Agent {
    @SuppressWarnings("FieldCanBeLocal")
    private ConnexionToJadeController controller;
    private StringProperty messageAnswer;


    public ConnexionJadeAgent() {
        super();
        messageAnswer = new SimpleStringProperty();
    }

    @Override
    protected void setup() {
        super.setup();
        controller = (ConnexionToJadeController) getArguments()[0];
        controller.setAgent(this);
        System.out.println(getLocalName() + " agent ---> " + Constants.INSTALLED);
    }

    public StringProperty messageAnswerProperty() {
        return messageAnswer;
    }

    public void sendAdminAgent(String content) {
        System.out.println(getLocalName() + " ---> send message to admin");
        addBehaviour(new ExchangeAdminBehaviour(content));
    }

    public class ExchangeAdminBehaviour extends Behaviour {
        String content;
        int step = 0;

        ExchangeAdminBehaviour(String content) {
            super();
            this.content = content;
        }

        @Override
        public void action() {

            switch (step) {
                case 0:
                    ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
                    message.addReceiver(new AID(Constants.REST_ADMIN_AGENT, AID.ISLOCALNAME));
                    message.setContent(content);
                    message.setConversationId(String.valueOf(System.currentTimeMillis()));
                    send(message);
                    step++;
                    break;
                case 1:
                    ACLMessage answer = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
                    if (answer != null) {
                        messageAnswer.setValue(answer.getContent());
                        step++;
                    } else
                        block();
            }
        }

        @Override
        public boolean done() {
            return step > 1;
        }

    }
}
