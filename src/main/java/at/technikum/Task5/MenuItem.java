package at.technikum.Task5;

public class MenuItem {
    private String id;
    private String label;

    public MenuItem() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    @Override
    public String toString() {
        return "MenuItem{id='" + id + "', label='" + label + "'}";
    }
}
