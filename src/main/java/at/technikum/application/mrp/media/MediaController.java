package at.technikum.application.mrp.media;

import at.technikum.application.common.Controller;
import at.technikum.application.mrp.UrlID;
import at.technikum.application.mrp.authentification.AuthService;
import at.technikum.application.mrp.rating.Rating;
import at.technikum.application.todo.exception.DuplicateAlreadyExistsException;
import at.technikum.application.todo.exception.EntityNotFoundException;
import at.technikum.application.todo.exception.NotAuthorizedException;
import at.technikum.server.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;


public class MediaController extends Controller {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService, AuthService authService) {
        super(authService);
        this.mediaService = mediaService;
    }

    @Override
    public Response handle(Request request) {

        int id = UrlID.urlID(request.getPath());
        String s = request.getUri();
        System.out.println("Uri: "+s);
        if (request.getMethod().equals(Method.GET.getVerb())) {
            if(request.getPath().equals("/api/media")&&request.getUri().contains("/api/media?")) {
                return filter(request);
            }
             else if (request.getPath().equals("/api/media")&&request.getUri().contains("?")==false) {
                //return json("doesn't exist yet",Status.NOT_FOUND);
                return readAll();
            } else if (request.getPath().equals("/api/media/"+id)) {
                //return json("doesn't exist yet",Status.NOT_FOUND);
                return read(id);
            } //else if(request.getPath().equals("/api/media?")){
              //  return filter(request);
            else {
                return json("doesn't exist yet", Status.NOT_FOUND);
            }
        } else if (request.getMethod().equals(Method.POST.getVerb())) {
            if (request.getPath().equals("/api/media")) {
                return create(request);
                //return json("doesnt exist yet", Status.NOT_FOUND);
            } else if(request.getPath().equals("/api/media/"+id+"/favorite")) {
                return fav(id, request);
            } else if(request.getPath().equals("/api/media/"+id+"/rate")) {
                return rate(request,id);
            } else {
                return json("doesn't exist yet", Status.NOT_FOUND);
            }
        } else if (request.getMethod().equals(Method.PUT.getVerb())) {
            String path = "/api/media/"+id;
            if (request.getPath().equals(path)) {
                //return json("doesn't exist yet",Status.NOT_FOUND);
                return update(request, id);
            } else {
                return json("doesn't exist yet",Status.NOT_FOUND);
            }
        }

        if (request.getMethod().equals(Method.DELETE.getVerb())) {
            if (request.getPath().equals("/api/media/"+id)) {
                //return json("doesn't exist yet",Status.NOT_FOUND);
                return delete(request, id);
            } if(request.getPath().equals("/api/media/"+id+"/favorite")) {
                return favDelete(id, request);
            } else {
                return json("doesn't exist yet", Status.NOT_FOUND);
            }
        }
        return json("doesn't exist yet",Status.NOT_FOUND);
    }

    private Response readAll() {
        System.out.println("readAll------------------------------");
        try {
            List<Media> media = mediaService.getAll();
            Response response = new Response();
            response.setStatus(Status.OK);
            //response.setContentType(ContentType.TEXT_PLAIN);
            response.setContentType(ContentType.APPLICATION_JSON);
            //????
            ObjectMapper mapper = new ObjectMapper();
            String jsonBody = mapper.writeValueAsString(media);
            response.setBody(jsonBody);

            return json(response, Status.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        //return text(media.toString());
    }

    private Response read(int id) {
        Response response = new Response();
        response.setContentType(ContentType.TEXT_PLAIN);

        try {
            Media media = mediaService.get(id);

            response.setStatus(Status.OK);
            response.setBody(media.toString());
            return json(response, Status.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.setStatus(Status.NOT_FOUND);
            response.setBody(e.getMessage());
            return json(response, Status.NOT_FOUND);
        }

    }

    //Korrekte ERRORCODES!!!!!!!!!!!!!
    private Response create(Request request) {
        Response response = new Response();
        response.setContentType(ContentType.TEXT_PLAIN);
        try {
            Media media = toObject(request.getBody(), Media.class);
            //System.out.println("Auth:"+authService.tokenExists(request.getHeader("Authorization"),true));
            //if(!authService.tokenExists(request.getHeader("Authorization"),true)) throw new Exception("not authorized");
            auth(request);
            media.setCreatorID(authService.getUserId(request.getHeader("Authorization")));
            mediaService.create(media);
            response.setStatus(Status.CREATED);
            response.setBody("done");
            return json(response, Status.CREATED);
        } catch(DuplicateAlreadyExistsException e) {
            response.setStatus(Status.CONFLICT);
            response.setBody("This media already exists");
            return json(response, Status.CONFLICT);
        } catch (NotAuthorizedException e){
            response.setStatus(Status.UNAUTHORIZED);
            response.setBody("User not Authorized");
            return json(response, Status.UNAUTHORIZED);
        } catch (Exception e){
            response.setStatus(Status.BAD_REQUEST);
            response.setBody(e.getMessage());
            return json(response,Status.BAD_REQUEST);
        }
    }

    private Response update(Request request, int id) {
        Response response = new Response();
        response.setContentType(ContentType.TEXT_PLAIN);
        try {
            auth(request);
            Media update = toObject(request.getBody(), Media.class);
            update.setCreatorID(getUserId(request));
            boolean done = mediaService.update(update, id);
            response.setStatus(Status.OK);
            if (done) {
                response.setBody("done: " + done + ", updated MediaID: " + id);
                return json(response, Status.OK);
            } else {
                response.setBody("done: " + done + ", tried to update MediaID: " + id);
                return json(response, Status.BAD_REQUEST);
            }
        } catch (NotAuthorizedException e) {
            response.setStatus(Status.UNAUTHORIZED);
            response.setBody("User not Authorized");
            return json(response, Status.UNAUTHORIZED);
        } catch (EntityNotFoundException e) {
            response.setStatus(Status.NOT_FOUND);
            response.setBody("MediaID to Update not found");
            return json(response, Status.NOT_FOUND);
        } catch (Exception e){
            response.setStatus(Status.OK);
            response.setBody(e.getMessage());
            System.out.println(e.getMessage());
            return json(response, Status.BAD_REQUEST);
        }
    }

    private Response delete(Request request, int mediaId) {
        Response response = new Response();
        response.setContentType(ContentType.TEXT_PLAIN);
        try{
            auth(request);
            boolean done = mediaService.deleteByID(getUserId(request), mediaId);
            response.setStatus(Status.OK);
            if(done){
                response.setBody("done: "+done+", deleted MediaID: "+mediaId);
                return json(response, Status.OK);
            } else {
                response.setBody("done: "+done+", tried to deleted MediaID: "+mediaId);
                return json(response, Status.BAD_REQUEST);
            }
        } catch (NotAuthorizedException e){
            response.setStatus(Status.UNAUTHORIZED);
            response.setBody("User not Authorized");
            return json(response, Status.UNAUTHORIZED);
        } catch (Exception e){
            response.setStatus(Status.OK);
            response.setBody(e.getMessage());
            System.out.println(e.getMessage());
            return json(response, Status.BAD_REQUEST);
        }
    }

    //Hier oder im RatingService??
    private Response rate(Request request, int mediaId) {
        Response response = new Response();
        response.setContentType(ContentType.TEXT_PLAIN);
        //try{
            auth(request);
            Rating rating = toObject(request.getBody(), Rating.class);
            rating.setCreatorId(getUserId(request));
            boolean done = mediaService.createRating(rating, mediaId);
            response.setBody("done: "+done+", rating on mediaId: "+mediaId);
            response.setStatus(Status.CREATED);
            return json(response,Status.CREATED);
            /*
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
            response.setStatus(Status.NOT_FOUND);
            response.setBody(e.getMessage());
            return json(response, Status.NOT_FOUND);
        } catch (Exception e) {
            response.setStatus(Status.BAD_REQUEST);
            response.setBody(e.getMessage());
            System.out.println(e.getMessage());
            return json(response, Status.BAD_REQUEST);
        }
        */
    }

    private Response fav(int mediaId, Request request){
        auth(request);
        int userId = getUserId(request);
        Response response = new Response();
        response.setContentType(ContentType.TEXT_PLAIN);
        try{
            boolean done = mediaService.fav(mediaId, userId);
            response.setBody("done: "+done+", like on mediaId: "+mediaId);
            response.setStatus(Status.CREATED);
            return json(response,Status.CREATED);
        } catch (EntityNotFoundException e){
            System.out.println(e.getMessage());
            response.setStatus(Status.NOT_FOUND);
            response.setBody(e.getMessage());
            return json(response, Status.NOT_FOUND);
        } catch (DuplicateAlreadyExistsException e){
            System.out.println(e.getMessage());
            response.setStatus(Status.CONFLICT);
            response.setBody(e.getMessage());
            return json(response, Status.CONFLICT);
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

    private Response favDelete(int mediaId, Request request){
        auth(request);
        int userId = getUserId(request);
        Response response = new Response();
        response.setContentType(ContentType.TEXT_PLAIN);
        try{
            boolean done = mediaService.favDelete(mediaId, userId);
            response.setBody("done: "+done+", deleted like on mediaId: "+mediaId);
            response.setStatus(Status.OK);
            return json(response,Status.OK);
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

    private Response filter(Request request){
        Response response = new Response();
        response.setContentType(ContentType.TEXT_PLAIN);
        System.out.println("we are in");
        System.out.println("URI working with:"+request.getUri());
        try{
            response.setStatus(Status.OK);
            response.setContentType(ContentType.APPLICATION_JSON);
            List<Media> ls = mediaService.search(request.getUri());
            System.out.println(ls.toString());
            ObjectMapper mapper = new ObjectMapper();
            String jsonBody = mapper.writeValueAsString(ls);
            response.setBody(mapper.writeValueAsString(ls));
            //response.setBody();
            //return json(response, Status.OK);
            //return response;
            return json(ls, Status.OK);
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
