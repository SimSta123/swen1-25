package at.technikum.application.mrp.media;

import at.technikum.application.common.Controller;
import at.technikum.application.mrp.UrlID;
import at.technikum.application.mrp.user.User;
import at.technikum.server.http.*;

import javax.sound.midi.SysexMessage;
import java.util.List;
import java.util.Optional;


public class MediaController extends Controller {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @Override
    public Response handle(Request request) {

        int id = UrlID.urlID(request.getPath());

        if (request.getMethod().equals(Method.GET.getVerb())) {
            if (request.getPath().equals("/api/media")) {
                //return json("doesn't exist yet",Status.NOT_FOUND);
                return readAll();
            } else if (request.getPath().equals("/api/media/"+id)) {
                return json("doesn't exist yet",Status.NOT_FOUND);
                //return read(id);
            } else {
                return json("doesn't exist yet", Status.NOT_FOUND);
            }
        } else if (request.getMethod().equals(Method.POST.getVerb())) {
            if (request.getPath().equals("/api/media")) {
                return create(request);
                //return json("doesnt exist yet", Status.NOT_FOUND);
            } else if(request.getPath().equals("/api/media"+id+"/favorite")) {
                return json("doesnt exist yet", Status.NOT_FOUND);
            } else {
                return json("doesn't exist yet", Status.NOT_FOUND);
            }
        } else if (request.getMethod().equals(Method.PUT.getVerb())) {
            String path = "/api/media/"+id;
            if (request.getPath().equals(path)) {
                return json("doesn't exist yet",Status.NOT_FOUND);
                //return update();
            } else {
                return json("doesn't exist yet",Status.NOT_FOUND);
            }
        }

        if (request.getMethod().equals(Method.DELETE.getVerb())) {
            if (request.getPath().equals("/api/media/"+id)) {
                return json("doesn't exist yet",Status.NOT_FOUND);
                //return delete();
            } if(request.getPath().equals("/api/media"+id+"/favorite")) {
                return json("doesnt exist yet", Status.NOT_FOUND);
            } else {
                return json("doesn't exist yet", Status.NOT_FOUND);
            }
        }
        return json("doesn't exist yet",Status.NOT_FOUND);
    }

    private Response readAll() {
        Optional<Object> media = mediaService.getAll();

        text(media.toString());
        Response response = new Response();
        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody(media.toString());
        return response;
        //return text(media.toString());
    }

    private Response read() {

        return null;

    }

    private Response create(Request request) {
        System.out.println("in MediaController");
        try {
            Media media = toObject(request.getBody(), Media.class);
            mediaService.create(media);
        } catch (Exception e) {
            System.err.println(e.getMessage());
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
