package rest.server;

import jade.agent.Constants;
import org.restlet.Component;
import org.restlet.data.Protocol;

public class TestRestJadeMainServer {

    public static void main(String[] args) {
        startTestRestJadeServer();
    }

    private static void startTestRestJadeServer() {
        try {
            // Create a new Component.
            Component component = new Component();

            // Add a new HTTP server listening on port 8182.
            component.getServers().add(Protocol.HTTP, Constants.REST_SERVER_PORT);
            component.getDefaultHost().attach(new TestRestJadeApplication());

            // Start the component.
            component.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
