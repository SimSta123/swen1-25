package at.technikum.application;

import at.technikum.application.common.*;
import at.technikum.application.mrp.leaderboard.*;
import at.technikum.application.mrp.rating.*;
import at.technikum.application.mrp.media.*;
import at.technikum.application.mrp.route_not_found.NotFoundController;
import at.technikum.application.mrp.user.*;
import at.technikum.application.todo.exception.*;
import at.technikum.server.http.*;

public class MRPApplication implements Application {

    private final Router router;
    private final ExceptionMapper exceptionMapper;

    public MRPApplication() {
        this.router = new Router();
        router.setFallback(new NotFoundController());

        router.addRoute("/api/user", new UserController(new UserService(new UserRepositoryC())));
        router.addRoute("/api/media", new MediaController(new MediaService(new MediaRepositoryC())));
        router.addRoute("/api/rating", new RatingController(new RatingService(new RatingRepositoryC())));
        router.addRoute("/api/leaderboard", new LeaderboardController(new LeaderboardService(new LeaderboardRepositoryC())));


        this.exceptionMapper = new ExceptionMapper();
        this.exceptionMapper.register(EntityNotFoundException.class, Status.NOT_FOUND);
        this.exceptionMapper.register(NotJsonBodyException.class, Status.BAD_REQUEST);
        this.exceptionMapper.register(JsonConversionException.class, Status.INTERNAL_SERVER_ERROR);
    }

    @Override
    public Response handle(Request request) {
        try {
            Controller controller = router.findController(request.getPath())
                    .orElseThrow(() -> new EntityNotFoundException("No route found for " + request.getPath()));

            return controller.handle(request);
        } catch (Exception ex) {
           Response r = new Response();
           r.setBody(ex.getMessage());
            return r;
        }
        //return null;
    }
}

