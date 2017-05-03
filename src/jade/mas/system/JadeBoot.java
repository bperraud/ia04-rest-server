package jade.mas.system;

import jade.agent.Constants;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;

public class JadeBoot {
    @SuppressWarnings("FieldCanBeLocal")
    private static String MAIN_PROPERTIES_FILE = "properties/main.properties";

    public static void main(String[] args) {
        boot();
    }

    private static void boot() {

        Runtime rt = Runtime.instance();
        ProfileImpl p = null;
        try {
            p = new ProfileImpl(MAIN_PROPERTIES_FILE);
            jade.wrapper.AgentContainer mc = rt.createMainContainer(p);
            AgentController ac = mc.createNewAgent(Constants.REST_ADMIN_AGENT,
                    "jade.agent.RestAdminAgent", null);
            ac.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
