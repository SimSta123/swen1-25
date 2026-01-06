package at.technikum.application.mrp;

import java.util.regex.*;

public class UrlID {

    public static int urlID(String path) {
        // Falls Query-Parameter vorhanden sind -> abschneiden
        path = path.split("\\?")[0];

        // Prüfen, ob "register" im Pfad steht
        if (path.contains("/register")) {
            return 0;
        }

        // Regex zum Extrahieren der ID hinter /media/ \\damit als literal verwendet
        Pattern pattern = Pattern.compile(".*/(media|users|ratings)/(\\d+)");
        Matcher matcher = pattern.matcher(path);

        if (matcher.find()) {
            String resourceType = matcher.group(1); // media, users oder ratings
            int id = Integer.parseInt(matcher.group(2)); // die ID
            return id;
        }  else {
            return -1;
        }
    }

    // Um von einem String einen Bestimmten param zu extrahieren
    public static String extractQueryParam(String queryParams, String paramName) {
        if(queryParams.contains(paramName)) {
            int start = queryParams.indexOf(paramName + "=") + paramName.length() + 1;
            int end;
            String sub = queryParams.substring(start);
            if(sub.contains("&")){
                end = queryParams.indexOf("&", start);
            } else {
                end = queryParams.length();
            }
            return queryParams.substring(start, end);
        } else {
            return null;
        }
    }

    public static String handleMediaGenre(String queryParams) {
        String genre = extractQueryParam(queryParams, "genre");
        return genre;
    }

    public static String handlContentType(String queryParams) {
        String contentType = extractQueryParam(queryParams, "contentType");
        return contentType;
    }

    public static String handleRecType(String queryParams) {
        String contentType = extractQueryParam(queryParams, "type");
        return contentType;
    }

    // title
    public static String handleMediaTitle(String queryParams) {
        String title = extractQueryParam(queryParams, "title");
        return title;
    }

    public static String handleMediaType(String queryParams) {
        String title = extractQueryParam(queryParams, "mediaType");
        return title;
    }

    public static String handleMediaRelYear(String queryParams) {
        String releaseYear = extractQueryParam(queryParams, "releaseYear");
        if (releaseYear == null) {
            return null;
        }
        return releaseYear;
    }

    public static String handleMediaAgeRestr(String queryParams) {
        String title = extractQueryParam(queryParams, "ageRestriction");
        return title;
    }

    public static String handleMediaRating(String queryParams) {
        String title = extractQueryParam(queryParams, "rating");
        return title;
    }

    public static String handleSortBy(String queryParams) {
        String title = extractQueryParam(queryParams, "sortBy");
        return title;
    }
}