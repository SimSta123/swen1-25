package at.technikum.application.mrp.media;

import at.technikum.application.common.Controller;
import at.technikum.application.mrp.UrlID;
import at.technikum.application.mrp.user.User;
import at.technikum.server.http.*;

import javax.sound.midi.SysexMessage;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
            } if(request.getPath().equals("/api/media"+id+"/favorite")) {
                return json("doesnt exist yet", Status.NOT_FOUND);
            } else {
                return json("doesn't exist yet", Status.NOT_FOUND);
            }
        }
        return json("doesn't exist yet",Status.NOT_FOUND);
    }

    private Response readAll() {
        List<Media> media = mediaService.getAll();

        Response response = new Response();
        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody(
                media.stream()
                        .map(Media::toString)
                        .collect(Collectors.joining("\n"))
        );
        return json(response, Status.OK);
        //return text(media.toString());
    }

    private Response read(int id) {
        //Optional<Object> media = mediaService.get(id);
        return null;

    }

    private Response create(Request request) {
        System.out.println("in MediaController");
        Media media = toObject(request.getBody(), Media.class);
        mediaService.create(media);
        System.out.println("after save");
        Response response = new Response();
        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        response.setBody("done");
        return json(response, Status.OK);
    }

    private Response update(Request request, int id) {
        System.out.println("in MediaUpdate");
        try{
            Media update = toObject(request.getBody(), Media.class);
            System.out.println("media creatorID:"+update.getCreatorID());
            boolean done = mediaService.update(update, id);
            Response response = new Response();
            response.setStatus(Status.OK);
            response.setContentType(ContentType.TEXT_PLAIN);
            if(done){
                response.setBody("done: "+done+", updated MediaID: "+id);
                return json(response, Status.OK);
            } else {
                response.setBody("done: "+done+", tried to update MediaID: "+id);
                return json(response, Status.BAD_REQUEST);
            }
        } catch (Exception e){
            Response response = new Response();
            response.setStatus(Status.OK);
            response.setContentType(ContentType.TEXT_PLAIN);
            response.setBody(e.getMessage());
            System.out.println(e.getMessage());
            return json(response, Status.BAD_REQUEST);
        }
    }

    private Response delete(Request request, int mediaId) {
        try{
            Media media = toObject(request.getBody(), Media.class);
            boolean done = mediaService.deleteByID(media.getCreatorID(), mediaId);
            Response response = new Response();
            response.setStatus(Status.OK);
            response.setContentType(ContentType.TEXT_PLAIN);
            if(done){
                response.setBody("done: "+done+", deleted MediaID: "+mediaId);
                return json(response, Status.OK);
            } else {
                response.setBody("done: "+done+", tried to deleted MediaID: "+mediaId);
                return json(response, Status.BAD_REQUEST);
            }
        } catch (Exception e){
            Response response = new Response();
            response.setStatus(Status.OK);
            response.setContentType(ContentType.TEXT_PLAIN);
            response.setBody(e.getMessage());
            System.out.println(e.getMessage());
            return json(response, Status.BAD_REQUEST);
        }
    }
}
