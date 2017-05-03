package rest.client;

import jade.agent.Constants;
import jade.agent.ContentBroker;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

class TestRestJadeClient {
    private final String postAddress = Constants.REST_URL
            + Constants.SIMPLE_POST_ADDRESS;
    private final String agentAddress = Constants.REST_URL
            + Constants.AGENT_POST_ADDRESS;

    private String login;
    private HttpClient client;

    TestRestJadeClient(String login) {
        client = HttpClients.createDefault();
        this.login = login;
    }

    /**
     * Envoie un simple message post au server adresse = /restjade/simple
     * Toutes les requêtes HTTP sont formatées avec un seul paramètre
     * main=objet json construits à partir d'une map de paramètres
     * un paramètre login est ajouté
     * ex: {"content"  : "hello Jade and Rest"}
     */
    String simpleMessage(Map<String, Object> params) {
        return formatPostMessage(postAddress, params);
    }


    /**
     * Envoie un message post au server destiné à un agent avec plusieurs paramètres
     * adresse = .../restjade/agent/aid L'url finit par le nom de l'agent
     */

    String postAgentMessage(String aid, Map<String, Object> params) {
        return formatPostMessage(agentAddress + aid, params);
    }

    /**
     * Envoie un message post au server destiné à un agent avec plusieurs paramètres
     * On teste avec la classe JadeGateway
     * adresse = .../restjadegateway/agent/aid L'url finit par le nom de l'agent
     */

    String postNativeGatewayAgentMessage(String aid, Map<String, Object> params) {
        final String agentTempAddress = Constants.REST_URL
                + Constants.AGENT_NATIVE_ADDRESS;
        return formatPostMessage(agentTempAddress + aid, params);
    }

    /**
     * Formate l'appel au serveur
     * Il n'y a qu'un paramètre dans la requête main = objet json construit sur params
     *
     * @param url route de la requête POST
     * @param params paramètres de la requête
     * @return chaîne JSON transmise dans la requête
     */
    private String formatPostMessage(String url, Map<String, Object> params) {
        HttpPost httppost = new HttpPost(url);
        String answer = "";
        ContentBroker cb = new ContentBroker(params);
        cb.put(Constants.LOGIN, login);
        StringEntity entity;
        try {
            entity = new StringEntity(cb.toJson());
            httppost.setEntity(entity);
            HttpResponse res = client.execute(httppost);
            System.out.println(res.getStatusLine().getStatusCode());
            HttpEntity answerEntity = res.getEntity();
            answer = EntityUtils.toString(answerEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }
}
