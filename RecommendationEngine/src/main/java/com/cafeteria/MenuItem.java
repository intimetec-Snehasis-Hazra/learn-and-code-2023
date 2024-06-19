package src.main.java.com.cafeteria;

public class MenuItem {
    private int id;
    private String name;
    private double price;
    private boolean availability;

    public MenuItem(int id, String name, double price, boolean availability) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.availability = availability;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return availability;
    }
}
