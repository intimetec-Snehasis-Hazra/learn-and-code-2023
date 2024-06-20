package src.main.java.com.cafeteria;
import java.sql.*;
import java.util.List;

public class Chef extends User {
    public Chef(String employeeId, String name) {
        super(employeeId, name);
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

    public String viewFeedbackReports() {
        StringBuilder report = new StringBuilder();
        try {
            List<Feedback> feedbackList = Database.getAllFeedbacks();
            if (feedbackList.isEmpty()) {
                report.append("No feedback available.\n");
            } else {
                for (Feedback feedback : feedbackList) {
                    MenuItem menuItem = Database.getMenuItemById(feedback.getMenuItemId());
                    report.append("Menu Item: ").append(menuItem.getName()).append("\n");
                    report.append("Comment: ").append(feedback.getComment()).append("\n");
                    report.append("Rating: ").append(feedback.getRating()).append("\n");
                    report.append("Sentiment: ").append(feedback.getSentiment()).append("\n");
                    //report.append("Date: ").append(feedback.getDate()).append("\n");
                    report.append("-----------\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            report.append("Error retrieving feedback reports.\n");
        }
        return report.toString();
    }

    public void selectItemsForVoting(List<MenuItem> recommendedItems) throws SQLException {
        for (MenuItem item : recommendedItems) {
            Database.addItemForVoting(item.getId());
        }
        NotificationManager.notifyUsers("Vote for tomorrow's menu items!");
    }

    public void finalizeMenu() throws SQLException {
        List<MenuItem> votedItems = Database.getItemsWithMostVotes();
        Database.storeFinalMenu(votedItems);
        NotificationManager.notifyUsers("Tomorrow's menu has been finalized!");
    }
}
