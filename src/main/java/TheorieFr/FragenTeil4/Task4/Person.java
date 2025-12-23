package TheorieFr.FragenTeil4.Task4;

public class Person {
    private String name;
    private int age;
    private double salary;

    // Standard-Konstruktor (wichtig für Jackson)
    public Person() {}

    public Person(String name, int age, double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    // Getter und Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
}
