package src.main.java.com.cafeteria;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SentimentAnalysis {

    private static final Set<String> POSITIVE_WORDS = new HashSet<>(Arrays.asList(
        "good", "great", "excellent", "amazing", "fantastic", "positive", "nice", "love", "like"
    ));

    private static final Set<String> NEGATIVE_WORDS = new HashSet<>(Arrays.asList(
        "bad", "terrible", "awful", "poor", "negative", "hate", "dislike", "worse", "worst"
    ));

    public static String analyzeSentiment(String feedback) {
        int positiveCount = 0;
        int negativeCount = 0;

        String[] words = feedback.toLowerCase().split("\\s+");
        for (String word : words) {
            if (POSITIVE_WORDS.contains(word)) {
                positiveCount++;
            } else if (NEGATIVE_WORDS.contains(word)) {
                negativeCount++;
            }
        }

        if (positiveCount > negativeCount) {
            return "Positive";
        } else if (negativeCount > positiveCount) {
            return "Negative";
        } else {
            return "Neutral";
        }
    }
}
