package src.main.java.com.cafeteria;
import java.sql.*;
import java.util.*;

public class FoodRecommendation {
    private int id;
    private java.util.Date recommendationDate;
    private List<MenuItem> recommendations;
    private Map<MenuItem, List<Feedback>> feedbackMap;

    public FoodRecommendation(int id, java.util.Date recommendationDate, List<MenuItem> recommendations) {
        this.id = id;
        this.recommendationDate = recommendationDate;
        this.recommendations = recommendations;
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
}
