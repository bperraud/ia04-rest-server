package rest.server;

import jade.agent.Constants;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;
import rest.server.resource.NativeJadeGatewayResource;
import rest.server.resource.SimplePostResource;

public class TestRestJadeApplication extends Application {
    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach(
                Constants.AGENT_NATIVE_ADDRESS,
                SimplePostResource.class);
        router.attach(
                Constants.AGENT_NATIVE_ADDRESS_ID,
                NativeJadeGatewayResource.class);
        return router;
    }
}
