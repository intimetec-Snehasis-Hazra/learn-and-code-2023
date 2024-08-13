package src.main.java.com.cafeteria;

import java.sql.SQLException;
import java.util.List;

public class Employee extends User {

    public Employee(String id, String name) {
        super(id, name);
    }

    public void giveFeedback(int menuItemId, String comment, int rating) throws SQLException {
        String sentiment = SentimentAnalyzer.analyzeSentiment(comment);
        Database.storeFeedback(this.getEmployeeId(), menuItemId, comment, rating, sentiment);
    }

    public void voteForItems(List<Integer> menuItemIds) throws SQLException {
        for (Integer menuItemId : menuItemIds) {
            Database.storeVote(this.getEmployeeId(), menuItemId);
        }
    }
}
