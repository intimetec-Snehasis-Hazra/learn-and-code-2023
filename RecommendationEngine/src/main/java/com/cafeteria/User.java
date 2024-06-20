package src.main.java.com.cafeteria;
public class User {
    private String employeeId;
    private String name;
    private String role;

    public User(String employeeId, String name) {
        this.employeeId = employeeId;
        this.name = name;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    // public boolean login(String employeeId, String name) {
    //     // Validate user credentials by checking with the database
    //     return Database.validateUser(employeeId, name);
    // }
}
