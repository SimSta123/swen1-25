package at.technikum.application.mrp;

import java.util.regex.*;

public class UrlID {

    public static int urlID(String path) {
        System.out.println(path);
        // Falls Query-Parameter vorhanden sind -> abschneiden
        path = path.split("\\?")[0];

        // Prüfen, ob "register" im Pfad steht
        if (path.contains("/register")) {
            return 0;
        }

        // Regex zum Extrahieren der ID hinter /media/
        Pattern pattern = Pattern.compile(".*/(media|users|ratings)/(\\d+)");
        Matcher matcher = pattern.matcher(path);

        if (matcher.find()) {
            String resourceType = matcher.group(1); // media, users oder ratings
            int id = Integer.parseInt(matcher.group(2)); // die ID
            System.out.println("Typ: " + resourceType + ", ID: " + id);
            return id;
        }  else {
            return -1;
        }
    }
}