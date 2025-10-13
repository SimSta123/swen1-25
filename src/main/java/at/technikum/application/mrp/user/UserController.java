package at.technikum.application.mrp.user;

import at.technikum.application.common.Controller;
import at.technikum.application.todo.model.Todo;
import at.technikum.server.http.*;

import java.util.List;

public class UserController extends Controller {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Response handle(Request request) {

        //Dass zu einer Switch-Case machen
        if (request.getMethod().equals(Method.GET.getVerb())) {
            if (request.getPath().equals("/user")) {
                return readAll();
            }
            if (request.getPath().equals("/user/register")) {
                return create(request);
            }
            //Braucht man hier ein request zum login??? Wegen login daten??
            if (request.getPath().equals("/api/user/login")) {
                return read();
            }
            if (request.getPath().equals("/api/user/profile")) {
                return read();
            }
            return read();
        }

        if (request.getMethod().equals(Method.POST.getVerb())) {
            return create(request);
        }

        if (request.getMethod().equals(Method.PUT.getVerb())) {
            return update();
        }

        if (request.getMethod().equals(Method.DELETE.getVerb())) {
            return delete();
        }

        return null;
    }

    private Response readAll() {
        List<User> user = userService.getAll();

        text(user.toString());
        Response response = new Response();
        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody("User");
        return response;
        //return text(user.toString());
    }

    //Braucht man hier ein request zum login??? Wegen login daten??
    private Response read() {
        List<User> user = userService.getAll();


        text(user.toString());
        Response response = new Response();
        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody("User/login_here");
        return response;
        //return null;

    }

    private Response create(Request request) {
        User user = toObject(request.getBody(), User.class);
        user = userService.create(user);
        Response response = new Response();
        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody("user registered");
        return json(response, Status.CREATED);
        //Rückgeben "User Created";

        /*
        Response response = new Response();
        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody("user/register");
        return response;

         */
        //return null;
    }

    private Response update() {
        return null;
    }

    private Response delete() {
        return null;
    }
}

