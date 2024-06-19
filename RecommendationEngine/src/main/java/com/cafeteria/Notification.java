package src.main.java.com.cafeteria;
import java.util.Date;

public class Notification {
    private int id;
    private String message;
    private Date timestamp;
    private String userId;

    public Notification(int id, String message, Date timestamp, String userId) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getUserId() {
        return userId;
    }
}
