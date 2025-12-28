package at.technikum.application.mrp.rating;

import at.technikum.application.common.Controller;
import at.technikum.application.mrp.UrlID;
import at.technikum.application.todo.exception.DuplicateAlreadyExistsException;
import at.technikum.server.http.*;

import java.util.DuplicateFormatFlagsException;
import java.util.List;

public class RatingController extends Controller {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Override
    public Response handle(Request request) {

        int id = UrlID.urlID(request.getPath());

        if (request.getMethod().equals(Method.GET.getVerb())) {
                return json("doesn't exist yet",Status.NOT_FOUND);
            //return read();
        }

        if (request.getMethod().equals(Method.POST.getVerb())) {
            if (request.getPath().equals("/api/ratings/"+id+"/rate")) {
                //return json("doesn't exist yet",Status.NOT_FOUND);
                return create(request,id);
            }
            if (request.getPath().equals("/api/ratings/"+id+"/like")) {
                //return json("doesn't exist yet",Status.NOT_FOUND);
                return like(id);
            }
            if (request.getPath().equals("/api/ratings/"+id+"/confirm")) {
               // return json("doesn't exist yet",Status.NOT_FOUND);
                return confirm(id);
            }
            return json("doesn't exist yet",Status.NOT_FOUND);
        }

        if (request.getMethod().equals(Method.PUT.getVerb())) {
            if (request.getPath().equals("/api/ratings/"+id)) {
                //return json("doesn't exist yet",Status.NOT_FOUND);
                return update(request, id);
            }
            return json("doesn't exist yet",Status.NOT_FOUND);
        }

        if (request.getMethod().equals(Method.DELETE.getVerb())) {
            if (request.getPath().equals("/api/ratings/"+id+"/delete")) {
                //return json("doesn't exist yet",Status.NOT_FOUND);
                return delete(request, id);
            }
            return json("doesn't exist yet",Status.NOT_FOUND);
        }
        return json("doesn't exist yet",Status.NOT_FOUND);
    }
    /*
    private Response readAll() {
        List<Rating> rating = ratingService.getAll();

        text(rating.toString());
        Response response = new Response();
        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody("Rating");
        return response;
        //return text(rating.toString());
    }
     */

    private Response read() {

        return null;

    }

    private Response create(Request request, int mediaId) {
        //Später ändern das der aktuelle Authenthifizierte user genommen wird
        Response response = new Response();
        response.setContentType(ContentType.TEXT_PLAIN);
        Rating rating = toObject(request.getBody(), Rating.class);
        try{
            boolean done = ratingService.create(rating, mediaId);
            response.setStatus(Status.OK);
            response.setBody("done: true, created rating, mediaId: "+mediaId + ", userId: "+ rating.getCreatorId());
            return json(response,Status.OK);
        } catch (DuplicateAlreadyExistsException e){
            response.setStatus(Status.CONFLICT);
            response.setBody("done: false, failed createing rating, mediaId: "+mediaId + ", userId: "+ rating.getCreatorId()+", err:"+e.getMessage());
            return json(response,Status.CONFLICT);
        } catch (Exception e){
            response.setStatus(Status.CONFLICT);
            response.setBody("done: false, failed createing rating, mediaId: "+mediaId + ", userId: "+ rating.getCreatorId()+", err:"+e.getMessage());
            return json(response,Status.CONFLICT);
        }
    }

    private Response update(Request request, int ratingId) {
        //Später durch akutellen User ersetzen
        int userId = 1;
        Response response = new Response();
        response.setContentType(ContentType.TEXT_PLAIN);
        Rating update = toObject(request.getBody(), Rating.class);
        update.setId(ratingId);
        try{
            System.out.println("blabla");
            ratingService.update(update, userId);
            response.setStatus(Status.OK);
            response.setBody("done: true, updated: ratingId: "+ratingId + ", userId: "+ userId);
            return json(response,Status.OK);
        } catch (DuplicateAlreadyExistsException e){
            response.setStatus(Status.CONFLICT);
            response.setBody("done: false, update failed: ratingId: "+ ratingId + ", userId: "+ userId+ ", err: rating does not exist");
            return json(response,Status.CONFLICT);
        } catch (Exception e){
            response.setStatus(Status.CONFLICT);
            response.setBody("done: false, update failed: ratingId: "+ratingId + ", userId: "+ userId+ ", err: " + e.getMessage());
            return json(response,Status.CONFLICT);
        }
    }

    private Response delete(Request request, int ratingId) {
        int userId = 1;
        Response response = new Response();
        response.setContentType(ContentType.TEXT_PLAIN);
        try{
            boolean done = ratingService.delete(ratingId, userId);
            System.out.println("geschaft");
            response.setStatus(Status.OK);
            response.setBody("done: "+done+", updated: ratingId: "+ratingId + ", userId: "+ userId);
            return json(response,Status.OK);
        } catch (DuplicateAlreadyExistsException e){
            response.setStatus(Status.CONFLICT);
            response.setBody("done: false, update failed: ratingId: "+ ratingId + ", userId: "+ userId+ ", err: rating does not exist");
            return json(response,Status.CONFLICT);
        } catch (Exception e){
            response.setStatus(Status.CONFLICT);
            response.setBody("done: false, update failed: ratingId: "+ratingId + ", userId: "+ userId+ ", err: " + e.getMessage());
            return json(response,Status.CONFLICT);
        }
    }

    private Response like(int ratingId){
        //später durch aktuellen authentifizierten User ersetzen
        int userId = 1;
        Response response = new Response();
        response.setContentType(ContentType.TEXT_PLAIN);
        try{
            boolean done = ratingService.like(ratingId, userId);
            response.setStatus(Status.CREATED);
            response.setBody("done: true, ratingId: "+ratingId + ", userId: "+ userId);
            return json(response,Status.CREATED);
        } catch (DuplicateAlreadyExistsException e){
            response.setStatus(Status.CONFLICT);
            response.setBody("done: false, ratingId: "+ratingId + ", userId: "+ userId+ ", err: rating does not exist");
            return json(response,Status.CONFLICT);
        } catch (Exception e){
            response.setStatus(Status.CONFLICT);
            response.setBody("done: false, ratingId: "+ratingId + ", userId: "+ userId+ ", err: " + e.getMessage());
            return json(response,Status.CONFLICT);
        }
    }

    private Response confirm(int ratingId){
        //später durch aktuellen authentifizierten User ersetzen
        int userId = 1;
        Response response = new Response();
        response.setContentType(ContentType.TEXT_PLAIN);
        try{
            boolean done = ratingService.confirm(ratingId, userId);
            response.setStatus(Status.CREATED);
            response.setBody("done: true, confirmed: ratingId: "+ratingId + ", userId: "+ userId);
            return json(response,Status.CREATED);
        } catch (DuplicateAlreadyExistsException e){
            response.setStatus(Status.CONFLICT);
            response.setBody("done: false, confirmed:  ratingId: "+ratingId + ", userId: "+ userId+ ", err: rating does not exist");
            return json(response,Status.CONFLICT);
        } catch (Exception e){
            response.setStatus(Status.CONFLICT);
            response.setBody("done: false, confirmed:  ratingId: "+ratingId + ", userId: "+ userId+ ", err: " + e.getMessage());
            return json(response,Status.CONFLICT);
        }
    }
}
