package rest.server.resource;

import jade.agent.Constants;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.ControllerException;
import jade.wrapper.gateway.JadeGateway;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;

import java.util.Map;

public class NativeJadeGatewayResource extends SimplePostResource {

    @Post
    public void update(Representation entity) {
        Map<String, Object> attributes = getRequest().getAttributes();
        String agentId = (String) attributes.get(Constants.AGENT_ID);
        if (agentId == null) {
            error("no agent aid");
            return;
        }
        /*
          Crée l'agent local qui va envoyer un message au destinataire
         */
        activeAgent(this, agentId, getParams(entity));
    }

    private void activeAgent(NativeJadeGatewayResource res, String aid,
                             Map<String, Object> params) {
        String login = (String) params.get(Constants.LOGIN);
        String agentName = login;
        JadeGateway.init(null, null, null);
        try {
            ProcessBehaviour behaviour = new ProcessBehaviour(aid, params);
            JadeGateway.execute(behaviour);

            formatAnswer(behaviour.getAnswer(), behaviour.getStatus());
        } catch (ControllerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class ProcessBehaviour extends Behaviour {
        String aid;
        Map<String, Object> params;
        private boolean stop = false;
        int step = 0;
        String convId;
        Status status;
        String answer;

        ProcessBehaviour(String aid, Map<String, Object> params) {
            super();
            this.aid = aid;
            this.params = params;
            convId = String.valueOf(System.currentTimeMillis());
        }

        Status getStatus() {
            return status;
        }

        String getAnswer() {
            return answer;
        }

        @Override
        public void action() {
            switch (step) {
                case 0:
                    String perfs = (String) params.get(Constants.PERFORMATIVE);
                    if (perfs != null) {
                        int perf = Constants.PERFORMATIVES.get(perfs);
                        ACLMessage message = new ACLMessage(perf);
                        message.addReceiver(new AID(aid, AID.ISLOCALNAME));
                        message.setContent((String) params.get(Constants.CONTENT));
                        message.setConversationId(convId);
                        myAgent.send(message);
                        step = 1;
                    } else {
                        stopProcess(Status.CLIENT_ERROR_BAD_REQUEST, "No performative");
                    }
                    break;
                case 1:
                    MessageTemplate mt = MessageTemplate.and(
                            MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                            MessageTemplate.MatchConversationId(convId));
                    ACLMessage answer = myAgent.receive(mt);
                    if (answer != null) {
                        stopProcess(Status.SUCCESS_OK,
                                convId + " - " + answer.getContent());
                    } else
                        block();
                    break;
            }
        }

        private void stopProcess(Status st, String ans) {
            status = st;
            answer = ans;
            stop = true;
        }

        @Override
        public boolean done() {
            return stop;
        }
    }
}
