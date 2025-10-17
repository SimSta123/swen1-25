package at.technikum.application.mrp.user;

import at.technikum.application.common.Controller;
import at.technikum.application.mrp.UrlID;
import at.technikum.application.todo.model.Todo;
import at.technikum.server.http.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserController extends Controller {

    private final UserService userService;

    public UserController(UserService userService) { this.userService = userService; }

    @Override
    public Response handle(Request request) {

        //Dass zu einer Switch-Case machen --> Geht nicht, switch(boolean) muss JDK 23 haben.
        if (request.getMethod().equals(Method.GET.getVerb())) {
            if (request.getPath().equals("/user")) {
                return readAll();
            }

            String path = "/api/user/"+ UrlID.urlID(request.getPath())+"/profile";
            if (request.getPath().equals(path)) {
                return read(UrlID.urlID(request.getPath()));
            }
            /*
            if(teile[1].equals("api")&&teile[2].equals("user")&&teile[4].equals("profile")){
                System.out.println("in schleife");
                try{
                    int id = Integer.parseInt(teile[3]);
                    if(id>0){
                        return read(id);
                    }
                } catch(Exception e){
                    return json(e.toString(), Status.BAD_REQUEST);
                }
             */
            path = "/api/user/"+UrlID.urlID(request.getPath())+"/ratings";
            if (request.getPath().equals(path)) {
                return json("doesn't exist",Status.NOT_FOUND);
            }

            path = "/api/user/"+UrlID.urlID(request.getPath())+"/favorites";
            if (request.getPath().equals(path)) {
                return json("doesn't exist",Status.NOT_FOUND);
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
            String path = "/api/user/"+UrlID.urlID(request.getPath())+"/profile";
            if (request.getPath().equals(path)) {
                return json("doesn't exist",Status.NOT_FOUND);
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
            return json(response, Status.CREATED);
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

