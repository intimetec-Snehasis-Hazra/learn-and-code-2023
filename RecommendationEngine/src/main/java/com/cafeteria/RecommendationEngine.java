package src.main.java.com.cafeteria;

import java.sql.SQLException;
import java.util.*;

public class RecommendationEngine {
    private List<Feedback> feedbacks;
    private Map<Integer, List<Double>> itemRatings = new HashMap<>();
    private Map<Integer, Integer> sentimentScores = new HashMap<>();

    public RecommendationEngine(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
        analyzeFeedbacks();
    }

    private void analyzeFeedbacks() {
        for (Feedback feedback : feedbacks) {
            int menuItemId = feedback.getMenuItemId();
            itemRatings.putIfAbsent(menuItemId, new ArrayList<>());
            itemRatings.get(menuItemId).add((double) feedback.getRating());

            int sentimentScore = 0;
            if (feedback.getSentiment().equalsIgnoreCase("Positive")) {
                sentimentScore = 1;
            } else if (feedback.getSentiment().equalsIgnoreCase("Negative")) {
                sentimentScore = -1;
            }
            sentimentScores.put(menuItemId, sentimentScores.getOrDefault(menuItemId, 0) + sentimentScore);
        }
    }

    public Map<Integer, Double> getAverageRatings() {
        Map<Integer, Double> averageRatings = new HashMap<>();
        for (Map.Entry<Integer, List<Double>> entry : itemRatings.entrySet()) {
            int menuItemId = entry.getKey();
            List<Double> ratings = entry.getValue();
            double average = ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            averageRatings.put(menuItemId, average);
        }
        return averageRatings;
    }

    public Map<Integer, Double> getFinalScores() {
        Map<Integer, Double> finalScores = new HashMap<>();
        Map<Integer, Double> averageRatings = getAverageRatings();
        for (Integer menuItemId : averageRatings.keySet()) {
            double ratingScore = averageRatings.get(menuItemId);
            double sentimentScore = sentimentScores.getOrDefault(menuItemId, 0);
            finalScores.put(menuItemId, ratingScore + sentimentScore);
        }
        return finalScores;
    }

    public static List<MenuItem> getRecommendedItems() throws SQLException {
        List<Feedback> feedbackList = Database.getAllFeedbacks();
        RecommendationEngine engine = new RecommendationEngine(feedbackList);
        Map<Integer, Double> finalScores = engine.getFinalScores();
        return Database.getTopRatedMenuItems(finalScores);
    }

}
