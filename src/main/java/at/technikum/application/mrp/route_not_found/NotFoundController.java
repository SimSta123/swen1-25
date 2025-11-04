package at.technikum.application.mrp.route_not_found;

import at.technikum.application.common.Controller;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;

public class NotFoundController extends Controller {

    public Response handle(Request request) {
        return json("404 - This endpoint does not exist", Status.NOT_FOUND);
    }
}
