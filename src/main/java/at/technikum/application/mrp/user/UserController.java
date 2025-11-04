package at.technikum.application.mrp.user;

import at.technikum.application.common.Controller;
import at.technikum.application.mrp.UrlID;
import at.technikum.server.http.*;
import java.util.List;
import java.util.Map;


public class UserController extends Controller {

    private final UserService userService;
    public UserController(UserService userService) { this.userService = userService; }

    @Override
    public Response handle(Request request) {

        //Dass zu einer Switch-Case machen --> Geht nicht, switch(boolean) muss JDK 23 haben.
        if (request.getMethod().equals(Method.GET.getVerb())) {
            if (request.getPath().equals("/api/users")) {
                return readAll();
            }
            System.out.println("GET: "+UrlID.urlID(request.getPath()));
            if (request.getPath().equals("/api/users/"+UrlID.urlID(request.getPath())+"/profile")) {
                //Hier soll die ID von der JSON oder von dem URL Path??
                return read(UrlID.urlID(request.getPath()));
                //return json("doesn't exist yet",Status.NOT_FOUND);
            }
            if (request.getPath().equals("/api/users/"+UrlID.urlID(request.getPath())+"/ratings")) {
                return json("doesn't exist yet",Status.NOT_FOUND);
            }
            if (request.getPath().equals("/api/users/"+UrlID.urlID(request.getPath())+"/favorites")) {
                return json("doesn't exist yet",Status.NOT_FOUND);
            }
            if (request.getPath().equals("/api/users/"+UrlID.urlID(request.getPath())+"/recommendations")) {
                return json("doesn't exist yet",Status.NOT_FOUND);
            }
            return json("doesn't exist yet, User.GET",Status.NOT_FOUND);
        }

        if (request.getMethod().equals(Method.POST.getVerb())) {
            if (request.getPath().equals("/api/users/register")) {
                System.out.println("try to go to create");
                return create(request);
            }
            //Braucht man hier ein request zum login??? Wegen login daten??
            if (request.getPath().equals("/api/users/login")) {
                System.out.println("try to go to login");
                return logIn(request);
            }
            return json("doesn't exist yet, User.POST",Status.NOT_FOUND);
        }

        if (request.getMethod().equals(Method.PUT.getVerb())) {
            String path = "/api/users/"+UrlID.urlID(request.getPath())+"/profile";
            if (request.getPath().equals(path)) {
                return json("doesn't exist yet",Status.NOT_FOUND);
                //return read(UrlID.urlID(request.getPath()));
            }
            return json("doesn't exist yet",Status.NOT_FOUND);
        }

        if (request.getMethod().equals(Method.DELETE.getVerb())) {
            return json("doesn't exist yet, user.DELETE",Status.NOT_FOUND);
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
        return json(response, Status.OK);
        //return text(user.toString());
    }

    //Braucht man hier ein request zum login??? Wegen login daten??
    private Response read(int ID) {
        User gUser = new User();
        Response response = new Response();
        try {
            //gUser = userService.findByUsername(user.getUsername());
            System.out.println("in READ");
            gUser = userService.findByID(ID);
            response.setStatus(Status.OK);
            response.setContentType(ContentType.TEXT_PLAIN);
            response.setBody("User found: " + gUser.getUsername());
            return json(response, Status.OK);
        } catch (Exception e) {
            return json(e.getMessage(), Status.BAD_REQUEST);
        }
    }

    private Response create(Request request) {
        User user = toObject(request.getBody(), User.class);

        try {
            user = userService.create(user);
        } catch (Exception e) {
            return json(e.getMessage(), Status.BAD_REQUEST);
        }

        Response response = new Response();
        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody("user registered");
        return json(response, Status.CREATED);
        //Rückgeben "User Created";

        /*
        User user = toObject(request.getBody(), User.class);
        user = todoService.create(user);
        return json(user, Status.CREATED);
         */
        //return null;

    }

    private Response logIn(Request request) {
        //ganz in den AuthService
        User user = toObject(request.getBody(), User.class);
        try {
            boolean logedIn = userService.logIn(user);
            Response response = new Response();
            response.setStatus(Status.OK);
            response.setContentType(ContentType.TEXT_PLAIN);
            if(logedIn==true) {
                response.setBody("user logged in"+" {token: "+ userService.getToken(user)+"}");
            }
            else {
                response.setBody("user log in failed");
            }
            return json(response, Status.OK);
        } catch (Exception e) {
            return json(e.getMessage(), Status.BAD_REQUEST);
        }


    }

    private Response update() {
        return null;
    }

    private Response delete() {
        return null;
    }
}

/*
public class UserController extends Controller {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Response handle(Request request) {
        String method = request.getMethod();
        String path = request.getPath();

        try {
            if (method.equals(Method.GET.getVerb())) {
                if (path.equals("/api/users")) return readAll();
                if (path.matches("/api/users/\\d+/profile"))
                    return read(UrlID.urlID(path));
            }

            if (method.equals(Method.POST.getVerb())) {
                if (path.equals("/api/users/register")) return create(request);
                if (path.equals("/api/users/login")) return logIn(request);
            }

            if (method.equals(Method.PUT.getVerb()) && path.matches("/api/users/\\d+/profile")) {
                json("doesn't exist yet",Status.NOT_FOUND);
                //return updateProfile(request);
            }

            if (method.equals(Method.DELETE.getVerb()) && path.matches("/api/users/\\d+")) {
                json("doesn't exist yet",Status.NOT_FOUND);
                return delete(UrlID.urlID(path));
            }

            // Standard: Nicht gefunden
            return json("Endpoint not found", Status.NOT_FOUND);

        } catch (Exception e) {
            return json(e.getMessage(), Status.BAD_REQUEST);
        }
    }

    private Response readAll() {
        return json(userService.getAll(), Status.OK);
    }

    private Response read(int id) {
        User user = userService.findByID(id);
        return json(user, Status.OK);
    }

    private Response create(Request request) {
        User user = toObject(request.getBody(), User.class);
        user = userService.create(user);
        return json(user, Status.CREATED);
    }

    private Response logIn(Request request) {
        User user = toObject(request.getBody(), User.class);
        boolean loggedIn = userService.logIn(user);

        if (loggedIn) {
            return json(Map.of("message", "user logged in", "token", userService.getToken(user)), Status.OK);
        } else {
            return json("Invalid credentials", Status.UNAUTHORIZED);
        }
    }

    private Response updateProfile(Request request) {
        return json("not implemented yet", Status.NOT_FOUND);
    }

    private Response delete(int id) {
        userService.delete(id);
        return json("User deleted", Status.NO_CONTENT);
    }
}
*/

