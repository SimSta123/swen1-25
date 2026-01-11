package at.technikum.application.mrp.leaderboard;

import at.technikum.application.common.Controller;
import at.technikum.application.mrp.UrlID;
import at.technikum.application.mrp.authentification.AuthService;
import at.technikum.server.http.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.sound.midi.SysexMessage;
import java.util.List;


public class LeaderboardController extends Controller {

    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService, AuthService authService) {
        super(authService);
        this.leaderboardService = leaderboardService;
    }

    @Override
    public Response handle(Request request) {

        int id = UrlID.urlID(request.getPath());

        if (request.getMethod().equals(Method.GET.getVerb())) {
            if (request.getPath().equals("/api/leaderboard")) {
                return readAll();
                //return json("doesn't exist yet",Status.NOT_FOUND);
            } else {
                return json("doesn't exist yet", Status.NOT_FOUND);
            }
        } else if (request.getMethod().equals(Method.POST.getVerb())) {
            if (request.getPath().equals("/api/leaderboard")) {
                return json("doesnt exist yet", Status.NOT_FOUND);
            } else {
                return json("doesn't exist yet", Status.NOT_FOUND);
            }
        } else if (request.getMethod().equals(Method.PUT.getVerb())) {
            if (request.getPath().equals("/api/leaderboard"+id)) {
                return json("doesn't exist yet",Status.NOT_FOUND);
                //return update();
            } else {
                return json("doesn't exist yet",Status.NOT_FOUND);
            }
        }
        if (request.getMethod().equals(Method.DELETE.getVerb())) {
            if (request.getPath().equals("/api/leaderboard")) {
                return json("doesn't exist yet",Status.NOT_FOUND);
            } else {
                return json("doesn't exist yet", Status.NOT_FOUND);
            }
        }
        return json("doesn't exist yet",Status.NOT_FOUND);
    }

    private Response readAll() {
        Response response = new Response();
        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);
        try {
            System.out.println("in readAll");
            List<Leaderboard> leaderB = leaderboardService.getAll();
            ObjectMapper mapper = new ObjectMapper();
            String jsonBody = mapper.writeValueAsString(leaderB);
            response.setBody(jsonBody);
            System.out.println("XX");
            return json(response, Status.OK);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
