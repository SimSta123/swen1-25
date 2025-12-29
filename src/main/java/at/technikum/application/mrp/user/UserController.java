package at.technikum.application.mrp.user;

import at.technikum.application.common.Controller;
import at.technikum.application.mrp.UrlID;
import at.technikum.application.mrp.authentification.AuthService;
import at.technikum.application.mrp.media.Media;
import at.technikum.application.mrp.rating.Rating;
import at.technikum.application.todo.exception.DuplicateAlreadyExistsException;
import at.technikum.application.todo.exception.EntityNotFoundException;
import at.technikum.server.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;


public class UserController extends Controller {

    private final UserService userService;
    private final AuthService authService;
    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @Override
    public Response handle(Request request) {

        //Dass zu einer Switch-Case machen --> Geht nicht, switch(boolean) muss JDK 23 haben.
        if (request.getMethod().equals(Method.GET.getVerb())) {
            if (request.getPath().equals("/api/users")) {
                return json("doesn't exist yet",Status.NOT_FOUND);
                //return readAll();
            }
            System.out.println("GET: "+UrlID.urlID(request.getPath()));
            if (request.getPath().equals("/api/users/"+UrlID.urlID(request.getPath())+"/profile")) {
                //Hier soll die ID von der JSON oder von dem URL Path??
                return read(UrlID.urlID(request.getPath()));
                //return json("doesn't exist yet",Status.NOT_FOUND);
            }
            if (request.getPath().equals("/api/users/"+UrlID.urlID(request.getPath())+"/ratings")) {
                //return json("doesn't exist yet",Status.NOT_FOUND);
                return ratingHistory(UrlID.urlID(request.getPath()));
            }
            if (request.getPath().equals("/api/users/"+UrlID.urlID(request.getPath())+"/favorites")) {
                return getFav(UrlID.urlID((request.getPath())));
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
            System.out.println("try to go to delete");
            System.out.println("DELETE: "+UrlID.urlID(request.getPath()));
            //return json("doesn't exist yet, user.DELETE",Status.NOT_FOUND);
            if (request.getPath().equals("/api/users/"+UrlID.urlID(request.getPath())+"/delete")) {
                //Hier soll die ID von der JSON oder von dem URL Path??
                System.out.println("try to start delete method");
                return delete(UrlID.urlID(request.getPath()));
            }
            return json("doesn't exist yet",Status.NOT_FOUND);
        }
        return null;
    }

    private Response readAll(int id) {
        List<User> user = userService.getAll();

        text(user.toString());
        Response response = new Response();
        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody("User");
        return json(response, Status.OK);
        //return text(user.toString());
    }

    private Response read(int ID) {
        User gUser = new User();
        Response response = new Response();
        response.setContentType(ContentType.TEXT_PLAIN);
        try {
            //gUser = userService.findByUsername(user.getUsername());
            System.out.println("in READ");
            gUser = userService.get(ID);

            response.setStatus(Status.OK);
            String body = "User found: Username: " + gUser.getUsername()+ ", PW:"+gUser.getPassword()+", ID: "+gUser.getId();
            response.setBody(body);
            return json(response, Status.OK);
        }  catch (EntityNotFoundException e){
            response.setStatus(Status.NOT_FOUND);
            response.setBody("err: Kein User mit dieser ID gefunden, "+e.getMessage());
            return json(response, Status.NOT_FOUND);
        } catch (RuntimeException e) {
            response.setStatus(Status.INTERNAL_SERVER_ERROR);
            response.setBody("err: Kein User mit dieser ID gefunden, "+e.getMessage());
            return json(response, Status.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            response.setStatus(Status.INTERNAL_SERVER_ERROR);
            response.setBody("err: "+e.getMessage());
            return json(e.getMessage(), Status.BAD_REQUEST);
        }
    }

    private Response create(Request request) {
        User user = toObject(request.getBody(), User.class);
        Response response = new Response();
        response.setContentType(ContentType.TEXT_PLAIN);

        try {
            boolean done = authService.createUser(user);
            response.setStatus(Status.CREATED);
            response.setBody("user registered, done: "+done);
            return json(response, Status.CREATED);
        } catch (DuplicateAlreadyExistsException e) {
            response.setStatus(Status.CONFLICT);
            response.setBody("err: Username already exists");
            return json(response, Status.CONFLICT);
        } catch (IllegalArgumentException e){
            response.setStatus(Status.CONFLICT);
            response.setBody("err: "+e.getMessage());
            return json(response, Status.CONFLICT);
        } catch (Exception e) {
            response.setStatus(Status.BAD_REQUEST);
            response.setBody("err: "+e.getMessage());
            return json(e.getMessage(), Status.BAD_REQUEST);
        }
    }

    private Response logIn(Request request) {
        //ganz in den AuthService
        User user = toObject(request.getBody(), User.class);
        Response response = new Response();
        response.setContentType(ContentType.TEXT_PLAIN);
        try {
            String token = authService.logIn(user);
            response.setStatus(Status.OK);
            response.setBody("user logged in"+" {token: "+ token+"}");
            response.setAuth(true);
            //Kein String token?????? SOll das irgendwo gepseichert werden?
            return json(response, Status.OK);
        } catch (Exception e) {
            return json(e.getMessage(), Status.BAD_REQUEST);
        }


    }

    private Response update(Request request, int userId) {
        Response response = new Response();
        response.setContentType(ContentType.TEXT_PLAIN);
        try{
            User user = toObject(request.getBody(), User.class);
            boolean done = userService.update(user, userId);
            response.setStatus(Status.OK);
            response.setBody("Update done: "+ done+", at userId: "+ userId);
            return json(response, Status.NOT_FOUND);
        } catch (EntityNotFoundException e){
            System.out.println(e.getMessage());
            response.setStatus(Status.NOT_FOUND);
            response.setBody(e.getMessage());
            return json(response, Status.NOT_FOUND);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            response.setStatus(Status.INTERNAL_SERVER_ERROR);
            response.setBody(e.getMessage());
            return json(response, Status.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.setStatus(Status.BAD_REQUEST);
            response.setBody(e.getMessage());
            return json(response, Status.BAD_REQUEST);
        }
    }

    private Response delete(int id) {
        try {
            System.out.println("in DELETE try to Start userService delete");
            userService.delete(id);
        } catch (Exception e) {
            return json(e.getMessage(), Status.BAD_REQUEST);
        }

        Response response = new Response();
        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody("user deleted");
        return json(response, Status.NO_CONTENT);
    }

    private Response ratingHistory(int id){
        Response response = new Response();
        response.setContentType(ContentType.TEXT_PLAIN);
        try{
            List<Rating> rl = userService.ratingHistory(id);

            response.setContentType(ContentType.APPLICATION_JSON);
            ObjectMapper mapper = new ObjectMapper();
            String jsonBody = mapper.writeValueAsString(rl);
            response.setBody(jsonBody);

            response.setStatus(Status.OK);
            return json(response, Status.OK);
        } catch (Exception e) {
            response.setStatus(Status.BAD_REQUEST);
            response.setBody("err: "+e.getMessage());
            return json(e.getMessage(), Status.BAD_REQUEST);
        }
    }

    public Response getFav(int userId){
        Response response = new Response();
        response.setContentType(ContentType.TEXT_PLAIN);
        try{
            List<Media> media = userService.getAllFavs(userId);
            response.setStatus(Status.OK);
            //response.setContentType(ContentType.TEXT_PLAIN);
            response.setContentType(ContentType.APPLICATION_JSON);
            //????
            ObjectMapper mapper = new ObjectMapper();
            String jsonBody = mapper.writeValueAsString(media);
            response.setBody(jsonBody);
            return json(response, Status.OK);
        } catch (EntityNotFoundException e){
            System.out.println(e.getMessage());
            response.setStatus(Status.NOT_FOUND);
            response.setBody(e.getMessage());
            return json(response, Status.NOT_FOUND);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            response.setStatus(Status.INTERNAL_SERVER_ERROR);
            response.setBody(e.getMessage());
            return json(response, Status.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.setStatus(Status.BAD_REQUEST);
            response.setBody(e.getMessage());
            return json(response, Status.BAD_REQUEST);
        }
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

