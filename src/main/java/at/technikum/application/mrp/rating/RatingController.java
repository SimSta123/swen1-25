package at.technikum.application.mrp.rating;

import at.technikum.application.common.Controller;
import at.technikum.server.http.*;

import java.util.List;

public class RatingController extends Controller {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Override
    public Response handle(Request request) {

        if (request.getMethod().equals(Method.GET.getVerb())) {
            if (request.getPath().equals("/rating")) {
                return readAll();
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

    private Response create(Request request) {

        return null;
    }

    private Response update() {
        return null;
    }

    private Response delete() {
        return null;
    }
}
