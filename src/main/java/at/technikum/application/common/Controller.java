package at.technikum.application.common;

import at.technikum.application.mrp.authentification.AuthService;
import at.technikum.application.todo.exception.JsonConversionException;
import at.technikum.application.todo.exception.NotAuthorizedException;
import at.technikum.application.todo.exception.NotJsonBodyException;
import at.technikum.server.http.ContentType;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Controller {

    public abstract Response handle(Request request);
    protected final AuthService authService;            //protected und final, damit alle danach immer die gleiche benutzen

    protected Controller(AuthService authService) {
        this.authService = authService;
    }

    protected <T> T toObject(String content, Class<T> valueType) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(content, valueType);
        } catch (Exception ex) {
            throw new NotJsonBodyException(ex);
        }
    }

    protected Response ok() {
        return status(Status.OK);
    }

    protected Response status(Status status) {
        return text(status.getMessage(), status);
    }

    protected Response text(String text) {
        return text(text, Status.OK);
    }

    protected Response text(String text, Status status) {
        return r(status, ContentType.TEXT_PLAIN, text);
    }

    protected Response json(Object o, Status status) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(o);
            return r(status, ContentType.APPLICATION_JSON, json);
        } catch (Exception ex) {
            throw new JsonConversionException(ex);
        }
    }

    private Response r(Status status, ContentType contentType, String body) {
        Response response = new Response();
        response.setStatus(status);
        response.setContentType(contentType);
        response.setBody(body);

        return response;
    }

    protected int getUserId(Request request) {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new IllegalArgumentException("No token found");
        }
        /*
        if(!authService.tokenExists(header, true)){
            throw new NotAuthorizedException("User not AUthorized");
        }
        */
        return authService.getUserId(header.substring("Bearer ".length()));
    }

    protected void auth(Request request){
        if(!authService.tokenExists(request.getHeader("Authorization"), true)) {
            throw new NotAuthorizedException("User not Authorized");
        }
    }
}
