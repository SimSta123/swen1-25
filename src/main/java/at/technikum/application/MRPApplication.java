package at.technikum.application;

import at.technikum.application.common.*;
import at.technikum.application.mrp.authentification.AuthRepositoryC;
import at.technikum.application.mrp.leaderboard.*;
import at.technikum.application.mrp.rating.*;
import at.technikum.application.mrp.media.*;
import at.technikum.application.mrp.route_not_found.NotFoundController;
import at.technikum.application.mrp.user.*;
import at.technikum.application.mrp.authentification.*;
import at.technikum.application.todo.exception.*;
import at.technikum.server.http.*;

public class MRPApplication implements Application {

    private final Router router;
    private final ExceptionMapper exceptionMapper;
    private final ConnectionPool connectionPool;
    AuthService authService;


    public MRPApplication() {
        this.router = new Router();
        router.setFallback(new NotFoundController(authService));

        this.connectionPool = new ConnectionPool(
                "postgresql",
                "localhost",
                5432,
                "swen1user",
                "swen1db", // secretManager.get("DB_PW")
                "mrpdb"
        );

        authService = new AuthService(new AuthRepositoryC(connectionPool));

        router.addRoute("/api/user", new UserController(new UserService(new UserRepositoryC(connectionPool)), authService));
        router.addRoute("/api/media", new MediaController(new MediaService(new MediaRepositoryC(connectionPool), new MediaSearchFilterRepository(connectionPool)), authService));
        router.addRoute("/api/rating", new RatingController(new RatingService(new RatingRepositoryC(connectionPool)), authService));
        router.addRoute("/api/leaderboard", new LeaderboardController(new LeaderboardService(new LeaderboardRepositoryC(connectionPool)), authService));


        this.exceptionMapper = new ExceptionMapper();
        this.exceptionMapper.register(EntityNotFoundException.class, Status.NOT_FOUND);
        this.exceptionMapper.register(NotJsonBodyException.class, Status.BAD_REQUEST);
        this.exceptionMapper.register(JsonConversionException.class, Status.INTERNAL_SERVER_ERROR);
        this.exceptionMapper.register(DuplicateAlreadyExistsException.class, Status.CONFLICT);
        this.exceptionMapper.register(RouteNotFoundException.class, Status.NOT_FOUND);
        this.exceptionMapper.register(NotAuthorizedException.class, Status.UNAUTHORIZED);
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

