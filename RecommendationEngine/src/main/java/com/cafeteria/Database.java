package src.main.java.com.cafeteria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public static User validateUser(String employeeId, String name) throws SQLException {
        String query = "SELECT * FROM Users WHERE employeeId = ? AND name = ?";
        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, employeeId);
            preparedStatement.setString(2, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String role = resultSet.getString("role");
                    switch (role) {
                        case "Admin":
                            return new Admin(employeeId, name);
                        case "Chef":
                            return new Chef(employeeId, name);
                        case "Employee":
                            return new Employee(employeeId, name);
                        default:
                            return null;
                    }
                }
            }
        }
        return null;
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
            String query = "INSERT INTO MenuItems (name, price, isAvailable) VALUES (?, ?, ?)";
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
            String query = "UPDATE MenuItems SET name = ?, price = ?, isAvailable = ? WHERE id = ?";
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

    public static void storeFeedback(String employeeId, int menuItemId, String comment, int rating, String sentiment) {
        try (Connection conn = getConnection()) {
            String query = "INSERT INTO Feedback (menuItemId, employeeId, comment, rating, feedbackDate,sentiment) VALUES (?, ?, ?, ?, ?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, menuItemId);
            stmt.setString(2, employeeId);
            stmt.setString(3, comment);
            stmt.setInt(4, rating);
            stmt.setDate(5, new java.sql.Date(System.currentTimeMillis()));
            stmt.setString(6, sentiment);
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

    public static List<Feedback> getAllFeedbacks() throws SQLException {
        List<Feedback> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM Feedback";
        try (Connection connection = getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int menuItemId = resultSet.getInt("menuItemId");
                String comment = resultSet.getString("comment");
                int rating = resultSet.getInt("rating");
                String sentiment = resultSet.getString("sentiment");
                // Date date = resultSet.getDate("date");

                Feedback feedback = new Feedback(menuItemId, comment, rating, sentiment);
                feedbackList.add(feedback);
            }
        }
        return feedbackList;
    }

    public static List<MenuItem> getTopRatedMenuItems(Map<Integer, Double> averageRatings) throws SQLException {
        List<MenuItem> topRatedItems = new ArrayList<>();
        String query = "SELECT * FROM MenuItems WHERE id = ?";

        for (Map.Entry<Integer, Double> entry : averageRatings.entrySet()) {
            int menuItemId = entry.getKey();
            try (Connection connection = getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, menuItemId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        MenuItem menuItem = new MenuItem(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getFloat("price"),
                                resultSet.getBoolean("isAvailable"));
                        topRatedItems.add(menuItem);
                    }
                }
            }
        }
        topRatedItems.sort((a, b) -> Double.compare(averageRatings.get(b.getId()), averageRatings.get(a.getId())));
        return topRatedItems.subList(0, Math.min(topRatedItems.size(), 5)); // Return top 5 items
    }

    public static List<VotingResult> getVotingResults() throws SQLException {
        List<VotingResult> results = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT menuItemId, COUNT(*) as voteCount FROM Votes GROUP BY menuItemId ORDER BY voteCount DESC")) {
            while (rs.next()) {
                VotingResult result = new VotingResult(
                        rs.getInt("menuItemId"),
                        getMenuItemName(rs.getInt("menuItemId")),
                        rs.getInt("voteCount")
                );
                results.add(result);
            }
        }
        return results;
    }

    public static void storeVote(String employeeId, int menuItemId) throws SQLException {
        String query = "INSERT INTO Votes (employeeId, menuItemId, vote_date) VALUES (?, ?, NOW())";
        try (Connection connection = getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, employeeId);
            preparedStatement.setInt(2, menuItemId);
            preparedStatement.executeUpdate();
        }
    }

    public static void addItemForVoting(int menuItemId) throws SQLException {
        String query = "INSERT INTO VotingItems (menuItemId) VALUES (?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, menuItemId);
            preparedStatement.executeUpdate();
        }
    }

    public static List<String> getRecommendations() {
        List<String> recommendations = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM FoodRecommendations WHERE recommendationDate = CURDATE()")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int itemId = rs.getInt("itemId");
                recommendations.add(getMenuItemName(itemId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recommendations;
    }

    private static String getMenuItemName(int itemId) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT name FROM MenuItems WHERE id = ?")) {
            stmt.setInt(1, itemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown Item";
    }

    public static List<MenuItem> getItemsWithMostVotes() throws SQLException {
        String query = "SELECT menuItemId, COUNT(*) AS voteCount FROM Votes GROUP BY menuItemId ORDER BY voteCount DESC";
        List<MenuItem> menuItems = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int menuItemId = resultSet.getInt("menuItemId");
                MenuItem menuItem = getMenuItemById(menuItemId);
                if (menuItem != null) {
                    menuItems.add(menuItem);
                }
            }
        }
        return menuItems;
    }

    public static void storeFinalMenu(List<MenuItem> finalMenuItems) throws SQLException {
        try (Connection conn = getConnection()) {
            // Clear the previous final menu
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DELETE FROM FinalMenu");
            }

            // Store the new final menu
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO FinalMenu (menuItemId) VALUES (?)")) {
                for (MenuItem item : finalMenuItems) {
                    stmt.setInt(1, item.getId());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
        }
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

}


