package at.technikum.application.mrp.media;

import at.technikum.application.common.Controller;
import at.technikum.server.http.*;

import java.util.List;


public class MediaController extends Controller {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @Override
    public Response handle(Request request) {

        if (request.getMethod().equals(Method.GET.getVerb())) {
            if (request.getPath().equals("/media")) {
                return readAll();
            }
            if (request.getPath().equals("/media/register")) {
                return create(request);
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
        List<Media> media = mediaService.getAll();

        text(media.toString());
        Response response = new Response();
        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody("Media");
        return response;
        //return text(media.toString());
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
