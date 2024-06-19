package src.main.java.com.cafeteria;

public class Admin extends User {
    public Admin(String employeeId, String name, String role) {
        super(employeeId, name, role);
    }

    public void addMenuItem(String name, float price, boolean availability) {
        Database.addMenuItem(name, price, availability);
    }

    public void updateMenuItem(int id, String name, float price, boolean availability) {
        Database.updateMenuItem(id, name, price, availability);
    }

    public void deleteMenuItem(int id) {
        Database.deleteMenuItem(id);
    }
}
