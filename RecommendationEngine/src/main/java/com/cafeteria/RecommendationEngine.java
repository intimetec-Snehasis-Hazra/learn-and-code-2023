package src.main.java.com.cafeteria;

import java.sql.SQLException;
import java.util.*;

public class RecommendationEngine {
    private List<Feedback> feedbacks;
    private Map<Integer, List<Double>> itemRatings = new HashMap<>();
    private Map<Integer, Integer> sentimentScores = new HashMap<>();
    private Map<Integer, List<String>> itemSentiments = new HashMap<>();

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
            String sentiment = feedback.getSentiment();
            if (sentiment.equalsIgnoreCase("Positive")) {
                sentimentScore = 1;
            } else if (sentiment.equalsIgnoreCase("Negative")) {
                sentimentScore = -1;
            }
            sentimentScores.put(menuItemId, sentimentScores.getOrDefault(menuItemId, 0) + sentimentScore);

            itemSentiments.putIfAbsent(menuItemId, new ArrayList<>());
            itemSentiments.get(menuItemId).add(sentiment);
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

    public Map<Integer, List<String>> getItemSentiments() {
        return itemSentiments;
    }

    public List<MenuItem> getDiscardMenuItems() throws SQLException {
        Map<Integer, Double> averageRatings = getAverageRatings();
        List<MenuItem> discardList = new ArrayList<>();
        for (Integer menuItemId : averageRatings.keySet()) {
            double averageRating = averageRatings.get(menuItemId);
            if (averageRating <= 2) {
                discardList.add(Database.getMenuItemById(menuItemId));
            } else {
                List<String> sentiments = itemSentiments.get(menuItemId);
                if (sentiments.stream().anyMatch(s -> s.contains("Tasteless") || s.contains("extremely bad experience") || s.contains("very poor")|| s.contains("Very Bad"))) {
                    discardList.add(Database.getMenuItemById(menuItemId));
                }
            }
        }
        return discardList;
    }

    public static Map<Integer, Double> getFinalScores() throws SQLException {
        // Step 1: Get all feedbacks
        List<Feedback> feedbacks = Database.getAllFeedbacks();
        // Step 2: Get all menu items
        List<MenuItem> menuItems = Database.getAllMenuItems();

        // Step 3: Create a map to store average ratings of menu items
        Map<Integer, Double> averageRatings = new HashMap<>();
        Map<Integer, Integer> ratingCount = new HashMap<>();

        // Calculate average ratings
        for (Feedback feedback : feedbacks) {
            int menuItemId = feedback.getMenuItemId();
            double rating = feedback.getRating();

            averageRatings.put(menuItemId, averageRatings.getOrDefault(menuItemId, 0.0) + rating);
            ratingCount.put(menuItemId, ratingCount.getOrDefault(menuItemId, 0) + 1);
        }

        // Calculate the average by dividing the total ratings by the count of ratings
        for (Map.Entry<Integer, Double> entry : averageRatings.entrySet()) {
            int menuItemId = entry.getKey();
            double totalRating = entry.getValue();
            int count = ratingCount.get(menuItemId);
            averageRatings.put(menuItemId, totalRating / count);
        }

        // Step 4: Get items with most votes
        List<MenuItem> mostVotedItems = Database.getItemsWithMostVotes();

        // Step 5: Assign weight to average ratings and votes
        Map<Integer, Double> finalScores = new HashMap<>();
        for (MenuItem item : menuItems) {
            int menuItemId = item.getId();
            double averageRating = averageRatings.getOrDefault(menuItemId, 0.0);
            int voteCount = 0;
            for (MenuItem votedItem : mostVotedItems) {
                if (votedItem.getId() == menuItemId) {
                    voteCount++;
                }
            }
            double finalScore = (0.7 * averageRating) + (0.3 * voteCount);
            finalScores.put(menuItemId, finalScore);
        }

        return finalScores;
    }

    public static List<MenuItem> getRecommendedItems() throws SQLException {
        List<Feedback> feedbackList = Database.getAllFeedbacks();
        RecommendationEngine engine = new RecommendationEngine(feedbackList);
        Map<Integer, Double> finalScores = engine.getFinalScores();
        return Database.getTopRatedMenuItems(finalScores);
    }

    public static void main(String[] args) throws SQLException {
        List<Feedback> feedbackList = Database.getAllFeedbacks();
        RecommendationEngine engine = new RecommendationEngine(feedbackList);
        List<MenuItem> discardList = engine.getDiscardMenuItems();

        System.out.println("Discard Menu Item List:");
        for (MenuItem item : discardList) {
            System.out.println("Food Item: " + item.getName());
            System.out.println("Average Rating: " + engine.getAverageRatings().get(item.getId()));
            System.out.println("Sentiments: " + engine.getItemSentiments().get(item.getId()));
        }
    }
}
