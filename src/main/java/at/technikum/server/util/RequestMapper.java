package at.technikum.server.util;

import at.technikum.server.http.Method;
import at.technikum.server.http.Request;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class RequestMapper {

    public Request fromExchange(HttpExchange exchange) throws IOException {
        Request request = new Request();
        request.setMethod(Method.valueOf(exchange.getRequestMethod()));
        System.out.println("getMethod: "+request.getMethod());
        request.setPath(exchange.getRequestURI().getPath());
        System.out.println("URI: "+exchange.getRequestURI());
        System.out.println("Path: "+request.getPath());
        request.setUri(String.valueOf(exchange.getRequestURI()));
        System.out.println("Request URI: "+request.getUri());
        exchange.getRequestHeaders().forEach((key, values) -> {
            if (!values.isEmpty()) {
                request.setHeader(key, values.get(0));
            }
        });

        InputStream is = exchange.getRequestBody();

        if (is == null) {
            return request;
        }

        byte[] buf = is.readAllBytes();
        request.setBody(new String(buf, StandardCharsets.UTF_8));

        return request;
    }
}
