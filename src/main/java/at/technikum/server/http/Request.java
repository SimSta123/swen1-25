package at.technikum.server.http;

import java.util.Map;

public class Request {

    private Method method;

    private String path;

    private String body;

    private String uri;

    public Request() {
    }

    public String getMethod() {
        return method.getVerb();
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUri() {return uri;}

    public void setUri(String uri) {this.uri = uri;}
}
