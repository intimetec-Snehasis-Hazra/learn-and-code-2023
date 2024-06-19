package src.main.java.com.cafeteria;
import java.sql.*;
import java.util.List;

public class Chef extends User {
    public Chef(String employeeId, String name, String role) {
        super(employeeId, name, role);
    }

    public void sendRecommendation(List<MenuItem> items) {
        try (Connection conn = Database.getConnection()) {
            for (MenuItem item : items) {
                String query = "INSERT INTO FoodRecommendations (recommendationDate, itemId) VALUES (CURRENT_DATE, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, item.getId());
                stmt.executeUpdate();
            }
            // Notify users about the new recommendation
            NotificationManager.notifyUsers("New food recommendation available for tomorrow.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayFeedback(MenuItem item) {
        try (Connection conn = Database.getConnection()) {
            String query = "SELECT comment, rating FROM Feedbacks WHERE itemId = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, item.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String comment = rs.getString("comment");
                int rating = rs.getInt("rating");
                System.out.println("Comment: " + comment + ", Rating: " + rating);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void generateReport() {
        try (Connection conn = Database.getConnection()) {
            String query = "SELECT itemId, AVG(rating) as averageRating FROM Feedbacks GROUP BY itemId";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int itemId = rs.getInt("itemId");
                double averageRating = rs.getDouble("averageRating");
                System.out.println("Item ID: " + itemId + ", Average Rating: " + averageRating);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
