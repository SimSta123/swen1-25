package at.technikum.application.mrp;

import at.technikum.server.http.Status;

public class UrlID {

    public static int urlID(String path) {
        String[] teile = path.split("/"); // Teilt nach dem Komma
        int id = -1;
        if (!teile[3].equals("register")) {
            try {
                id = Integer.parseInt(teile[3]);
                return id;
            } catch (NumberFormatException e) {
                throw new NumberFormatException("No ID given");
            }
        } else {
            return 0;
        }
    }
}
