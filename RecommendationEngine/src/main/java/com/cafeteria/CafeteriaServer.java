package src.main.java.com.cafeteria;

import java.lang.Class;
import java.io.*;
import java.sql.SQLException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.net.ServerSocket;

public class CafeteriaServer {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Cafeteria server is running...");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected from " + socket.getInetAddress());
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Changed to read employeeId and name only
            String employeeId = in.readLine();
            String name = in.readLine();

            // Validate user and retrieve role from the database
            User user = Database.validateUser(employeeId, name);
            Class role = user.getClass();
            if (user != null) {
                out.println("Login successful as " + role.getSimpleName());

                String command;
                while ((command = in.readLine()) != null) {
                    processCommand(user, command);
                }
            } else {
                out.println("Invalid credentials");
            }
        } catch (IOException e) {
            // Handle IOException
            e.printStackTrace(); // Example: Print stack trace for debugging
        } catch (SQLException e) {
            // Handle SQLException
            e.printStackTrace(); // Example: Print stack trace for debugging
        } catch (Exception e) {
            // Catch any other unanticipated exceptions
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processCommand(User user, String command) throws IOException {
        System.out.println(user.getClass());
        try {
            switch (command) {
                case "GET_MENU":
                    List<MenuItem> menuItems = Database.getAllMenuItems();
                    for (MenuItem item : menuItems) {
                        out.println(item.getId() + ". " + item.getName() + " - $" + item.getPrice() + " - "
                                + (item.isAvailable() ? "Available" : "Not Available"));
                    }
                    break;
                case "GIVE_FEEDBACK":
                    if (user instanceof Employee) {
                        out.println("Enter Menu Item ID: ");
                        int menuItemId = Integer.parseInt(in.readLine());
                        out.println("Enter Comment: ");
                        String comment = in.readLine();
                        out.println("Enter Rating (1-5): ");
                        int rating = Integer.parseInt(in.readLine());
                        ((Employee) user).giveFeedback(menuItemId, comment, rating);
                        out.println("Feedback submitted.");
                    } else {
                        out.println("Only employees can give feedback.");
                    }
                    break;
                case "ADD_MENU_ITEM":
                    if (user instanceof Admin) {
                        out.println("Enter Menu Item Name: ");
                        String name = in.readLine();
                        out.println("Enter Menu Item Price: ");
                        float price = Float.parseFloat(in.readLine());
                        out.println("Is the item available? (true/false): ");
                        boolean availability = Boolean.parseBoolean(in.readLine());
                        ((Admin) user).addMenuItem(name, price, availability);
                        out.println("Menu item added.");
                    } else {
                        out.println("Only admin can add menu items.");
                    }
                    break;
                case "UPDATE_MENU_ITEM":
                    if (user instanceof Admin) {
                        out.println("Enter Menu Item ID: ");
                        int menuItemId = Integer.parseInt(in.readLine());
                        out.println("Enter New Menu Item Name: ");
                        String name = in.readLine();
                        out.println("Enter New Menu Item Price: ");
                        float price = Float.parseFloat(in.readLine());
                        out.println("Is the item available? (true/false): ");
                        boolean availability = Boolean.parseBoolean(in.readLine());
                        ((Admin) user).updateMenuItem(menuItemId, name, price, availability);
                        out.println("Menu item updated.");
                    } else {
                        out.println("Only admin can update menu items.");
                    }
                    break;
                case "DELETE_MENU_ITEM":
                    if (user instanceof Admin) {
                        out.println("Enter Menu Item ID: ");
                        int menuItemId = Integer.parseInt(in.readLine());
                        ((Admin) user).deleteMenuItem(menuItemId);
                        out.println("Menu item deleted.");
                    } else {
                        out.println("Only admin can delete menu items.");
                    }
                    break;
                case "SEND_RECOMMENDATION":
                    if (user instanceof Chef) {
                        out.println("Enter Menu Item IDs (comma separated): ");
                        String[] ids = in.readLine().split(",");
                        List<MenuItem> recommendedItems = new ArrayList<MenuItem>();
                        for (String id : ids) {
                            recommendedItems.add(Database.getMenuItemById(Integer.parseInt(id)));
                        }
                        ((Chef) user).sendRecommendation(recommendedItems);
                        out.println("Recommendation sent.");
                    } else {
                        out.println("Only chefs can send recommendations.");
                    }
                    break;
                case "VIEW_FEEDBACK_REPORTS":
                    if (user instanceof Chef) {
                        String report = ((Chef) user).viewFeedbackReports();
                        out.println(report);
                    } else {
                        out.println("Only chefs can view feedback reports.");
                    }
                    out.println(); // Indicate end of feedback report
                    break;
                case "GET_RECOMMENDATIONS":
                    if (user instanceof Chef) {
                        List<MenuItem> recommendedItems = RecommendationEngine.getRecommendedItems();
                        for (MenuItem item : recommendedItems) {
                            out.println(item.getId() + ". " + item.getName() + " - $" + item.getPrice() + " - "
                                    + (item.isAvailable() ? "Available" : "Not Available"));
                        }
                    } else {
                        out.println("Only chefs can get recommendations.");
                    }
                    break;
                case "VOTE":
                    if (user instanceof Employee) {
                        out.println("Enter Menu Item ID: ");
                        int menuItemId = Integer.parseInt(in.readLine());
                        Database.storeVote(user.getEmployeeId(), menuItemId);
                        out.println("Vote submitted.");
                    } else {
                        out.println("Only employees can vote.");
                    }
                    break;
                default:
                    out.println("Unknown command");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
