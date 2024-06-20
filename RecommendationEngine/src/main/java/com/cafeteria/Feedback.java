package src.main.java.com.cafeteria;

import java.util.Date;

public class Feedback {
    private int menuItemId;
    private String comment;
    private int rating;
    private String sentiment;
    //private Date date;

    public Feedback(int menuItemId, String comment, int rating, String sentiment) {
        this.menuItemId = menuItemId;
        this.comment = comment;
        this.rating = rating;
        this.sentiment = sentiment;
        //this.date = date;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public String getComment() {
        return comment;
    }

    public int getRating() {
        return rating;
    }

    public String getSentiment() {
        return sentiment;
    }

    // public Date getDate() {
    //     return date;
    // }
}
