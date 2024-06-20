package src.main.java.com.cafeteria;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class CafeteriaClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Welcome to the Cafeteria Recommendation System");
            System.out.print("Enter Employee ID: ");
            String employeeId = scanner.nextLine();
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();

            out.println(employeeId);
            out.println(name);

            String serverResponse = in.readLine();
            System.out.println(serverResponse);

            if (serverResponse.startsWith("Login successful")) {
                String role = serverResponse.split(" ")[3];
                showCommands(role);

                while (true) {
                    String command = scanner.nextLine();
                    out.println(command);

                    switch (command) {
                        case "ADD_MENU_ITEM":
                            if ("Admin".equals(role)) {
                                handleAddMenuItem(scanner, out);
                            } else {
                                System.out.println("Invalid command for your role.");
                            }
                            break;
                        case "UPDATE_MENU_ITEM":
                            if ("Admin".equals(role)) {
                                handleUpdateMenuItem(scanner, out);
                            } else {
                                System.out.println("Invalid command for your role.");
                            }
                            break;
                        case "DELETE_MENU_ITEM":
                            if ("Admin".equals(role)) {
                                handleDeleteMenuItem(scanner, out);
                            } else {
                                System.out.println("Invalid command for your role.");
                            }
                            break;
                        case "SEND_RECOMMENDATION":
                            if ("Chef".equals(role)) {
                                handleSendRecommendation(scanner, out);
                            } else {
                                System.out.println("Invalid command for your role.");
                            }
                            break;
                        case "VIEW_FEEDBACK_REPORTS":
                            if ("Chef".equals(role)) {
                                handleViewFeedbackReports(in);
                            } else {
                                System.out.println("Invalid command for your role.");
                            }
                            break;
                        case "GIVE_FEEDBACK":
                            if ("Employee".equals(role)) {
                                handleGiveFeedback(scanner, out);
                            } else {
                                System.out.println("Invalid command for your role.");
                            }
                            break;
                        case "GET_MENU":
                            // No additional input needed
                            break;
                        case "VOTE":
                            if ("Employee".equals(role)) {
                                handleVote(scanner, out);
                            } else {
                                System.out.println("Invalid command for your role.");
                            }
                            break;
                        default:
                            System.out.println("Unknown command");
                            break;
                    }

                    String response;
                    while ((response = in.readLine()) != null && !response.isEmpty()) {
                        System.out.println(response);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showCommands(String role) {
        System.out.println("Commands: ");
        System.out.println("GET_MENU - To get the menu items");
        if ("Employee".equals(role)) {
            System.out.println("GIVE_FEEDBACK - To give feedback");
            System.out.println("VOTE - To vote for items");
        } else if ("Chef".equals(role)) {
            System.out.println("SEND_RECOMMENDATION - To send recommendations");
            System.out.println("VIEW_FEEDBACK_REPORTS - To view feedback reports");
        } else if ("Admin".equals(role)) {
            System.out.println("ADD_MENU_ITEM - To add a menu item");
            System.out.println("UPDATE_MENU_ITEM - To update a menu item");
            System.out.println("DELETE_MENU_ITEM - To delete a menu item");
        }
    }

    private static void handleAddMenuItem(Scanner scanner, PrintWriter out) {
        System.out.print("Enter Menu Item Name: ");
        String itemName = scanner.nextLine();
        System.out.print("Enter Menu Item Price: ");
        float itemPrice = scanner.nextFloat();
        scanner.nextLine(); // Consume newline
        System.out.print("Is the item available? (true/false): ");
        boolean availability = scanner.nextBoolean();
        scanner.nextLine(); // Consume newline

        out.println(itemName);
        out.println(itemPrice);
        out.println(availability);
    }

    private static void handleUpdateMenuItem(Scanner scanner, PrintWriter out) {
        System.out.print("Enter Menu Item ID: ");
        int itemId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter New Menu Item Name: ");
        String newItemName = scanner.nextLine();
        System.out.print("Enter New Menu Item Price: ");
        float newItemPrice = scanner.nextFloat();
        scanner.nextLine(); // Consume newline
        System.out.print("Is the item available? (true/false): ");
        boolean newAvailability = scanner.nextBoolean();
        scanner.nextLine(); // Consume newline

        out.println(itemId);
        out.println(newItemName);
        out.println(newItemPrice);
        out.println(newAvailability);
    }

    private static void handleDeleteMenuItem(Scanner scanner, PrintWriter out) {
        System.out.print("Enter Menu Item ID: ");
        int deleteItemId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        out.println(deleteItemId);
    }

    private static void handleSendRecommendation(Scanner scanner, PrintWriter out) {
        System.out.print("Enter Menu Item IDs (comma separated): ");
        String ids = scanner.nextLine();
        out.println(ids);
    }

    private static void handleViewFeedbackReports(BufferedReader in) throws IOException {
        System.out.println("Feedback Reports:");
        String reportLine;
        while (!(reportLine = in.readLine()).isEmpty()) {
            System.out.println(reportLine);
        }
    }

    private static void handleGiveFeedback(Scanner scanner, PrintWriter out) {
        System.out.print("Enter Menu Item ID: ");
        int feedbackItemId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter Comment: ");
        String feedbackComment = scanner.nextLine();

        System.out.print("Enter Rating (1-5): ");
        int feedbackRating = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        out.println(feedbackItemId);
        out.println(feedbackComment);
        out.println(feedbackRating);
    }

    private static void handleVote(Scanner scanner, PrintWriter out) {
        System.out.print("Enter Menu Item ID: ");
        int menuItemId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        out.println(menuItemId);
    }
}
