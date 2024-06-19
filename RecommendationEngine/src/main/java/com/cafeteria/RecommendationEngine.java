package src.main.java.com.cafeteria;
import java.util.*;

public class RecommendationEngine {
    private List<Feedback> feedbacks;
    private Map<String, List<Double>> itemRatings = new HashMap<>();

    public RecommendationEngine(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
        analyzeFeedbacks();
    }

    private void analyzeFeedbacks() {
        for (Feedback feedback : feedbacks) {
            itemRatings.putIfAbsent(String.valueOf(feedback.getItemId()), new ArrayList<>());
            itemRatings.get(String.valueOf(feedback.getItemId())).add((double) feedback.getRating());
        }
    }

    public Map<String, Double> getAverageRatings() {
        Map<String, Double> averageRatings = new HashMap<>();
        for (String itemId : itemRatings.keySet()) {
            List<Double> ratings = itemRatings.get(itemId);
            double average = ratings.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            averageRatings.put(itemId, average);
        }
        return averageRatings;
    }
}
