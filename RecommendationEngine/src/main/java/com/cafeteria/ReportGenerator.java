package src.main.java.com.cafeteria;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ReportGenerator {

    public static void generateMonthlyReport() {
        Map<Integer, ReportEntry> reportEntries = new HashMap<>();

        try {
            // Fetch feedback data
            String feedbackQuery = "SELECT menuItemId, COUNT(*) as feedbackCount, AVG(rating) as averageRating " +
                                   "FROM Feedback " +
                                   "WHERE MONTH(feedbackDate) = MONTH(CURRENT_DATE()) " +
                                   "GROUP BY menuItemId";
            ResultSet feedbackRs = Database.executeQuery(feedbackQuery);
            while (feedbackRs.next()) {
                int menuItemId = feedbackRs.getInt("menuItemId");
                int feedbackCount = feedbackRs.getInt("feedbackCount");
                double averageRating = feedbackRs.getDouble("averageRating");
                reportEntries.put(menuItemId, new ReportEntry(feedbackCount, averageRating));
            }

            // Fetch sentiment data
            String sentimentQuery = "SELECT menuItemId, sentiment, COUNT(*) as sentimentCount " +
                                    "FROM Sentiments " +
                                    "WHERE MONTH(sentimentDate) = MONTH(CURRENT_DATE()) " +
                                    "GROUP BY menuItemId, sentiment";
            ResultSet sentimentRs = Database.executeQuery(sentimentQuery);
            while (sentimentRs.next()) {
                int menuItemId = sentimentRs.getInt("menuItemId");
                String sentiment = sentimentRs.getString("sentiment");
                int sentimentCount = sentimentRs.getInt("sentimentCount");

                ReportEntry entry = reportEntries.get(menuItemId);
                if (entry != null) {
                    entry.addSentiment(sentiment, sentimentCount);
                }
            }

            // Print the report
            System.out.println("Monthly Feedback Report:");
            for (Map.Entry<Integer, ReportEntry> entry : reportEntries.entrySet()) {
                System.out.println("Menu Item ID: " + entry.getKey());
                System.out.println("  Feedback Count: " + entry.getValue().getFeedbackCount());
                System.out.println("  Average Rating: " + entry.getValue().getAverageRating());
                System.out.println("  Sentiments: " + entry.getValue().getSentimentsSummary());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class ReportEntry {
    private int feedbackCount;
    private double averageRating;
    private Map<String, Integer> sentiments;

    public ReportEntry(int feedbackCount, double averageRating) {
        this.feedbackCount = feedbackCount;
        this.averageRating = averageRating;
        this.sentiments = new HashMap<>();
    }

    public void addSentiment(String sentiment, int count) {
        sentiments.put(sentiment, sentiments.getOrDefault(sentiment, 0) + count);
    }

    public int getFeedbackCount() {
        return feedbackCount;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public String getSentimentsSummary() {
        StringBuilder summary = new StringBuilder();
        for (Map.Entry<String, Integer> entry : sentiments.entrySet()) {
            summary.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
        }
        return summary.toString();
    }
}
