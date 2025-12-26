package at.technikum.application.mrp.rating;

import at.technikum.application.common.Controller;
import at.technikum.application.mrp.UrlID;
import at.technikum.server.http.*;

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
                return json("doesn't exist yet",Status.NOT_FOUND);
                //return create(request);
            }
            if (request.getPath().equals("/api/ratings/"+id+"/confirm")) {
                return json("doesn't exist yet",Status.NOT_FOUND);
            }
            return json("doesn't exist yet",Status.NOT_FOUND);
        }

        if (request.getMethod().equals(Method.PUT.getVerb())) {
            if (request.getPath().equals("/api/ratings/"+id)) {
                return json("doesn't exist yet",Status.NOT_FOUND);
            }
            return json("doesn't exist yet",Status.NOT_FOUND);
        }

        if (request.getMethod().equals(Method.DELETE.getVerb())) {
            if (request.getPath().equals("/api/ratings/"+id)) {
                return json("doesn't exist yet",Status.NOT_FOUND);
            }
            return json("doesn't exist yet",Status.NOT_FOUND);
        }
        return json("doesn't exist yet",Status.NOT_FOUND);
    }

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

    private Response read() {

        return null;

    }

    private Response create(Request request, int mediaId) {
        //Später ändern das der aktuelle Authenthifizierte user genommen wird
        try{
            Rating rating = toObject(request.getBody(), Rating.class);
            boolean done = ratingService.create(rating, mediaId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private Response update() {
        return null;
    }

    private Response delete() {
        return null;
    }
}
