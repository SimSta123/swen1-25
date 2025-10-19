package at.technikum.application.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Router {

    private List<Route> routes;
    private Controller fallbackController; //Fallback-Controller für 404

    public Router() {
        this.routes = new ArrayList<>();
    }

    public Optional<Controller> findController(String path) {
        for (Route route: this.routes) {
            if (path.startsWith(route.getPath())) {
                return Optional.of(route.getController());
            }
        }
        //return Optional.empty();

        //abfangen wenn nur /api
        if (path.startsWith("/api") && fallbackController != null) {
            return Optional.of(fallbackController);
        }

        // Wenn keine Route passt → Fallback oder empty
        if (fallbackController != null) {
            return Optional.of(fallbackController);
        }
        return Optional.empty();
    }

    public void addRoute(String path, Controller controller) {
        routes.add(
                new Route(path, controller)
        );
    }
    /**
     * Setzt einen Controller, der für alle unbekannten Routen aufgerufen wird.
     */
    public void setFallback(Controller fallbackController) {
        this.fallbackController = fallbackController;
    }
}
