package TheorieFr.FragenTeil4.Task4;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonDemo {
    public static void main(String[] args) {
        // Objekt erstellen
        Person person = new Person("Alice Johnson", 30, 55000.75);

        // ObjectMapper erstellen
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Objekt in JSON-String umwandeln
            String jsonString = mapper.writeValueAsString(person);
            //write kann Exception werfen

            // Ausgabe
            System.out.println("=== JSON-Ausgabe ===");
            System.out.println(jsonString);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
