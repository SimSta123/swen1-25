package at.technikum.Task5;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParseDemo {
    public static void main(String[] args) {
        String jsonString = """
        {
          "items": [
            {"id": "Open"},
            {"id": "OpenNew", "label": "Open New"},
            {"id": "ZoomIn", "label": "Zoom In"},
            {"id": "ZoomOut", "label": "Zoom Out"},
            {"id": "OriginalView", "label": "Original View"},
            {"id": "Quality"},
            {"id": "Pause"},
            {"id": "Mute"},
            {"id": "Find", "label": "Find..."},
            {"id": "FindAgain", "label": "Find Again"},
            {"id": "Copy"},
            {"id": "CopyAgain", "label": "Copy Again"},
            {"id": "CopySVG", "label": "Copy SVG"},
            {"id": "ViewSVG", "label": "View SVG"},
            {"id": "ViewSource", "label": "View Source"},
            {"id": "SaveAs", "label": "Save As"},
            {"id": "Help"},
            {"id": "About", "label": "About Adobe SVG Viewer..."},
            {"label": "nur label"}
          ]
        }
        """; //""" = Textblock, mehrzeiliger String ohne /n Escapesequenze

        try {
            //Mapper reads, converts JSON <-> Java
            ObjectMapper mapper = new ObjectMapper();
            //mapper.write/readValue(file,obj)

            // JSON → Java-Objekt
            Menu menu = mapper.readValue(jsonString, Menu.class);
            //readValue kann Exceptions werfen.

            System.out.println("=== Geparste Menü-Elemente ===");
            for (MenuItem item : menu.getItems()) {
                System.out.println(item.toString());
                //Jackson gibt automatisch allen var die kein Wert haben ein null
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
