package rest.server.resource;

import jade.agent.Constants;
import jade.agent.ContentBroker;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;

public class SimplePostResource extends ServerResource {

    @Post
    public void update(Representation entity) {
        ContentBroker cb = new ContentBroker(getParams(entity));
        /*
          Do something
		 */
        String content = cb.get(Constants.CONTENT);
        String answer = content != null ? content + "; Post received"
                : "no content :(";
        formatAnswer(answer, Status.SUCCESS_OK);
    }

    Map<String, Object> getParams(Representation entity) {
        String sd;
        Map<String, Object> params = null;
        try {
            sd = URLDecoder.decode(entity.getText(), "utf-8");
            System.out.println("Rest server entity: " + sd);
            ContentBroker cb = new ContentBroker(sd);
            params = cb.getMap();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return params;
    }

    @SuppressWarnings("SameParameterValue")
    void error(String message) {
        formatAnswer(message, Status.CLIENT_ERROR_BAD_REQUEST);
    }

    void formatAnswer(String content, Status status) {
        StringRepresentation answerEntity = new StringRepresentation("{\""
                + Constants.MAIN_ATTRIBUTE + "\" : \"" + content + "\" }",
                MediaType.TEXT_PLAIN);
        System.out.println("--> resource received: " + content);
        getResponse().setEntity(answerEntity);
        getResponse().setStatus(status);
    }
}
