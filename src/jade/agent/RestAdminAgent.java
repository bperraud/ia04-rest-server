package jade.agent;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

@SuppressWarnings("unused")
public class RestAdminAgent extends Agent {

    @Override
    protected void setup() {
        super.setup();
        System.out.println(getLocalName() + " : " + Constants.INSTALLED);
        addBehaviour(new WaitMessageBehaviour());
    }

    private class WaitMessageBehaviour extends CyclicBehaviour {

        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            ACLMessage message = receive(mt);
            if (message != null) {
                ACLMessage reply = message.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                System.out.println(getLocalName() + " --> received : " + message.getContent());
                reply.setContent(message.getContent() + " bien reçu avec id : " + reply.getConversationId());
                send(reply);
            } else
                block();
        }

    }
}
