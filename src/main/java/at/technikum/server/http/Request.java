package at.technikum.server.http;

import java.util.HashMap;
import java.util.Map;

public class Request {

    private Method method;

    private String path;

    private String body;

    private String uri;

    private Map<String, String> header;

    public Request() {
        this.header = new HashMap<>();
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

    public void setHeader(String key, String value) {
        header.put(key.toLowerCase(), value);
    }

    public String getHeader(String key) {
        return header.get(key.toLowerCase());
    }

    public boolean hasHeader(String key) {
        return header.containsKey(key.toLowerCase());
    }
}
