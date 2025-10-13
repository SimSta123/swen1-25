package at.technikum.application;

import at.technikum.application.common.Application;
import at.technikum.application.common.Controller;
import at.technikum.application.common.Router;
import at.technikum.application.mrp.rating.RatingController;
import at.technikum.application.mrp.rating.RatingRepositoryC;
import at.technikum.application.mrp.rating.RatingService;
import at.technikum.application.mrp.media.*;
import at.technikum.application.mrp.user.*;
import at.technikum.application.todo.exception.EntityNotFoundException;
import at.technikum.application.todo.exception.ExceptionMapper;
import at.technikum.application.mrp.user.UserController;
import at.technikum.application.todo.exception.JsonConversionException;
import at.technikum.application.todo.exception.NotJsonBodyException;
import at.technikum.server.http.ContentType;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import at.technikum.application.*;


public class MRPApplication implements Application {

    private final Router router;
    private final ExceptionMapper exceptionMapper;

    public MRPApplication() {
        this.router = new Router();

        router.addRoute("/api/user", new UserController(new UserService(new UserRepositoryC())));
        router.addRoute("/api/media", new MediaController(new MediaService(new MediaRepositoryC())));
        router.addRoute("/api/rating", new RatingController(new RatingService(new RatingRepositoryC())));

        this.exceptionMapper = new ExceptionMapper();
        this.exceptionMapper.register(EntityNotFoundException.class, Status.NOT_FOUND);
        this.exceptionMapper.register(NotJsonBodyException.class, Status.BAD_REQUEST);
        this.exceptionMapper.register(JsonConversionException.class, Status.INTERNAL_SERVER_ERROR);
    }

    @Override
    public Response handle(Request request) {
        try {
            Controller controller = router.findController(request.getPath())
                    .orElseThrow(RuntimeException::new);

            return controller.handle(request);
        } catch (Exception ex) {
            // map exception to http response
        }
        return null;
    }
}

