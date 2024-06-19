package src.main.java.com.cafeteria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Employee extends User {

    public Employee(String id, String name, String role) {
        super(id, name, role);
    }

    public void giveFeedback(int menuItemId, String comment, int rating) {
        String sentiment = SentimentAnalysis.analyzeSentiment(comment);
        try (Connection conn = Database.getConnection()) {
            String query = "INSERT INTO Feedback (menuItemId, comment, rating, sentiment) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, menuItemId);
            stmt.setString(2, comment);
            stmt.setInt(3, rating);
            stmt.setString(4, sentiment);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
