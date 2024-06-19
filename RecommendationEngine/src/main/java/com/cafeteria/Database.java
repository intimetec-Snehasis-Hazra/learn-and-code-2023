package src.main.java.com.cafeteria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/cafeteria";
    private static final String USER = "root";
    private static final String PASSWORD = "Test0342!";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load JDBC driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static ResultSet executeQuery(String query) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        return stmt.executeQuery();
    }
    
    public static boolean validateUser(String employeeId, String name) {
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM Users WHERE employeeId = ? AND name = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, employeeId);
            stmt.setString(2, name);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static MenuItem getMenuItemById(int id) {
        MenuItem menuItem = null;
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM MenuItems WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                boolean isAvailable = rs.getBoolean("isAvailable");
                menuItem = new MenuItem(id, name, price, isAvailable);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItem;
    }

    public static List<MenuItem> getAllMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM MenuItems";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                boolean isAvailable = rs.getBoolean("isAvailable");
                menuItems.add(new MenuItem(id, name, price, isAvailable));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItems;
    }

    public static void addMenuItem(String name, float price, boolean availability) {
        try (Connection conn = getConnection()) {
            String query = "INSERT INTO MenuItems (name, price, availability) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setFloat(2, price);
            stmt.setBoolean(3, availability);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateMenuItem(int id, String name, float price, boolean availability) {
        try (Connection conn = getConnection()) {
            String query = "UPDATE MenuItems SET name = ?, price = ?, availability = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setFloat(2, price);
            stmt.setBoolean(3, availability);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteMenuItem(int id) {
        try (Connection conn = getConnection()) {
            String query = "DELETE FROM MenuItems WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void storeFeedback(int menuItemId, String employeeId, String comment, int rating) {
        try (Connection conn = getConnection()) {
            String query = "INSERT INTO Feedback (menuItemId, employeeId, comment, rating, feedbackDate) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, menuItemId);
            stmt.setString(2, employeeId);
            stmt.setString(3, comment);
            stmt.setInt(4, rating);
            stmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void storeSentiment(int menuItemId, String sentiment) {
        try (Connection conn = getConnection()) {
            String query = "INSERT INTO Sentiments (menuItemId, sentiment) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, menuItemId);
            stmt.setString(2, sentiment);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getUserRole(String employeeId, String name) {
        String role = null;
        try (Connection conn = getConnection()) {
            String query = "SELECT role FROM Users WHERE employeeId = ? AND name = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, employeeId);
            stmt.setString(2, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                role = rs.getString("role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }

}
