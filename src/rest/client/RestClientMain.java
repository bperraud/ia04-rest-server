package rest.client;

import jade.agent.Constants;

import java.util.HashMap;
import java.util.Map;

public class RestClientMain {

    public static void main(String[] args) {
//        test1();
//        test2();
//        test3();
        test6();
    }

    private static void test1() {
        TestRestJadeClient client = new TestRestJadeClient("rest");
        /*
          Envoie un message post simple au service rest
         */
        Map<String, Object> params1 = new HashMap<>();
        params1.put(Constants.CONTENT, "hello Jade and Rest");
        String answer1 = client.simpleMessage(params1);
        System.out.println(answer1);

        /*
          Envoie un message post avec plusieurs paramètres au service rest
         */
        Map<String, Object> params2 = new HashMap<>();
        params2.put(Constants.CONTENT, "hello Rest");
        params2.put("name", "cm");
        params2.put("total", 10000);
        String answer2 = client.simpleMessage(params2);
        System.out.println(answer2);
    }

    /**
     * Envoie un message destiné à l'agent admin
     * Les paramètres sont corrects
     */
    private static void test2() {
        TestRestJadeClient client = new TestRestJadeClient("rest");
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.CONTENT, "hello Rest");
        params.put(Constants.PERFORMATIVE, Constants.REQUEST);
        // Le client ajoutera le paramère login
        String answer = client.postAgentMessage(Constants.REST_ADMIN_AGENT, params);
        System.out.println(answer);
    }

    /**
     * Envoie un message destiné à l'agent admin
     * Manque le paramètres performatif
     */
    private static void test3() {
        TestRestJadeClient client = new TestRestJadeClient("rest");
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.CONTENT, "Error Rest :(");
        //params.put(Constants.PERFORMATIVE, Constants.REQUEST);
        // Le client ajoutera le paramère login
        String answer = client.postAgentMessage(Constants.REST_ADMIN_AGENT, params);
        System.out.println(answer);
    }

    /**
     * Envoie un message destiné à l'agent admin
     * Les paramètres sont corrects
     * Le récepteur est la classe JadeGateway native
     */
    private static void test6() {
        TestRestJadeClient client = new TestRestJadeClient("cm");
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.CONTENT, "Test native Gateway");
        params.put(Constants.PERFORMATIVE, Constants.REQUEST);
        // Le client ajoutera le paramère login
        String answer = client.postNativeGatewayAgentMessage(Constants.REST_ADMIN_AGENT, params);
        System.out.println(answer);
    }
}
