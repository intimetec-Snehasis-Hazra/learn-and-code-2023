package src.main.java.com.cafeteria;
import java.util.LinkedList;
import java.util.Queue;

public class NotificationManager {
    private static Queue<Notification> notifications = new LinkedList<>();

    public static void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public static Queue<Notification> getNotifications() {
        return notifications;
    }

    public static void notifyUsers(String message) {
        Notification notification = new Notification(0, message, new java.util.Date(), null);
        addNotification(notification);
        // Logic to send notification to all users (e.g., using sockets)
    }
}
