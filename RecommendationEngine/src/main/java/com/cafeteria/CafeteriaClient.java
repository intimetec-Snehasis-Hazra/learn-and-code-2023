package src.main.java.com.cafeteria;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CafeteriaClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        boolean running = true;
        Scanner scanner = new Scanner(System.in);

        while (running) {
            running = login(scanner);
        }
        scanner.close();
    }

    private static boolean login(Scanner scanner) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

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
                return navigateMenu(role, scanner, in, out, socket);
            } else {
                System.out.println("Invalid credentials. Try again.");
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean navigateMenu(String role, Scanner scanner, BufferedReader in, PrintWriter out, Socket socket) throws IOException {
        while (true) {
            showCommands(role);
            String command = scanner.nextLine();
            out.println(command);

            if (command.equals("EXIT")) {
                System.out.println("Exiting the system. Goodbye!");
                return false;
            }

            if (command.equals("LOGOUT")) {
                System.out.println("Logging out. Returning to login screen.");
                return true;
            }

            switch (command) {
                case "GET_MENU":
                    handleGetMenu(in);
                    break;
                case "VIEW_FINAL_MENU":
                if ("Employee".equals(role)) {
                    handleViewFinalMenu(in,scanner);
                } else {
                    System.out.println("Invalid command for your role.");
                }
                break;
                case "GIVE_FEEDBACK":
                    if ("Employee".equals(role)) {
                        handleGiveFeedback(scanner, out, in);
                    } else {
                        System.out.println("Invalid command for your role.");
                    }
                    break;
                case "VOTE":
                    if ("Employee".equals(role)) {
                        handleVote(scanner, out, in);
                    } else {
                        System.out.println("Invalid command for your role.");
                    }
                    break;
                case "VIEW_RECOMMENDATIONS":
                    if ("Employee".equals(role)) {
                        handleViewRecommendations(in);
                    } else {
                        System.out.println("Invalid command for your role.");
                    }
                    break;
                case "ADD_MENU_ITEM":
                    if ("Admin".equals(role)) {
                        handleAddMenuItem(scanner, out, in);
                    } else {
                        System.out.println("Invalid command for your role.");
                    }
                    break;
                case "UPDATE_MENU_ITEM":
                    if ("Admin".equals(role)) {
                        handleUpdateMenuItem(scanner, out, in);
                    } else {
                        System.out.println("Invalid command for your role.");
                    }
                    break;
                case "SHOW_LOGIN_LOGOUT_HISTORY":
                    if ("Admin".equals(role)) {
                        handleShowLoginHistory(scanner, out, in);
                    } else {
                        System.out.println("Invalid command for your role.");
                    }
                    break;
                case "DELETE_MENU_ITEM":
                    if ("Admin".equals(role)) {
                        handleDeleteMenuItem(scanner, out, in);
                    } else {
                        System.out.println("Invalid command for your role.");
                    }
                    break;
                case "SEND_RECOMMENDATION":
                    if ("Chef".equals(role)) {
                        handleSendRecommendation(scanner, out, in);
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
                case "VIEW_VOTING_RESULTS":
                    if ("Chef".equals(role)) {
                        handleViewVotingResults(in);
                    } else {
                        System.out.println("Invalid command for your role.");
                    }
                    break;
                case "CHOOSE_FINAL_MENU":
                    if ("Chef".equals(role)) {
                        handleChooseFinalMenu(scanner, out, in);
                    } else {
                        System.out.println("Invalid command for your role.");
                    }
                    break;
                default:
                    System.out.println("Unknown command");
                    break;
            }

            System.out.println("Press Enter to continue...");
            scanner.nextLine(); // Pause before showing menu again
        }
    }

    private static void handleShowLoginHistory(Scanner scanner, PrintWriter out, BufferedReader in) throws IOException {
        System.out.println("Login/Logout History:");
        String historyLine;
        while (!(historyLine = in.readLine()).isEmpty()) {
            System.out.println(historyLine);
        }
    }

    private static void showCommands(String role) {
        System.out.println("\nCommands: ");
        System.out.println("GET_MENU - To get the menu items");
        System.out.println("LOGOUT - To log out and re-login");
        System.out.println("EXIT - To exit the system");
        if ("Employee".equals(role)) {
            System.out.println("GIVE_FEEDBACK - To give feedback");
            System.out.println("VOTE - To vote for items");
            System.out.println("VIEW_RECOMMENDATIONS - To view recommendations sent by the chef.");
            System.out.println("VIEW_FINAL_MENU - To view the final menu.");
        } else if ("Chef".equals(role)) {
            System.out.println("SEND_RECOMMENDATION - To send recommendations");
            System.out.println("VIEW_FEEDBACK_REPORTS - To view feedback reports");
            System.out.println("VIEW_VOTING_RESULTS - To view the voting results of menu items");
            System.out.println("CHOOSE_FINAL_MENU - To choose the final menu based on voting results");
        } else if ("Admin".equals(role)) {
            System.out.println("ADD_MENU_ITEM - To add a menu item");
            System.out.println("UPDATE_MENU_ITEM - To update a menu item");
            System.out.println("DELETE_MENU_ITEM - To delete a menu item");
            System.out.println("SHOW_LOGIN_LOGOUT_HISTORY - To view login/logout history");
        }
    }

    private static void handleGetMenu(BufferedReader in) throws IOException {
        System.out.println("Menu Items:");
        String menuItem;
        while (!(menuItem = in.readLine()).isEmpty()) {
            System.out.println(menuItem);
        }
    }

    private static void handleGiveFeedback(Scanner scanner, PrintWriter out, BufferedReader in) throws IOException {
        System.out.print("Enter Menu Item ID: ");
        int feedbackItemId = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter Comment: ");
        String feedbackComment = scanner.nextLine();

        System.out.print("Enter Rating (1-5): ");
        int feedbackRating = Integer.parseInt(scanner.nextLine());

        out.println(feedbackItemId);
        out.println(feedbackComment);
        out.println(feedbackRating);

        System.out.println("Response: " + in.readLine());
    }

    private static void handleVote(Scanner scanner, PrintWriter out, BufferedReader in) throws IOException {
        System.out.print("Enter Menu Item ID: ");
        int menuItemId = Integer.parseInt(scanner.nextLine());
        out.println(menuItemId);
        System.out.println("Response: " + in.readLine());
    }

    private static void handleViewRecommendations(BufferedReader in) throws IOException {
        System.out.println("Recommendations:");
        String recommendation;
        while (!(recommendation = in.readLine()).isEmpty()) {
            System.out.println(recommendation);
        }
    }

    private static void handleAddMenuItem(Scanner scanner, PrintWriter out, BufferedReader in) throws IOException {
        System.out.print("Enter Menu Item Name: ");
        String itemName = scanner.nextLine();
        System.out.print("Enter Menu Item Price: ");
        float itemPrice = Float.parseFloat(scanner.nextLine());
        System.out.print("Is the item available? (true/false): ");
        boolean availability = Boolean.parseBoolean(scanner.nextLine());

        out.println(itemName);
        out.println(itemPrice);
        out.println(availability);

        System.out.println("Response: " + in.readLine());
    }

    private static void handleUpdateMenuItem(Scanner scanner, PrintWriter out, BufferedReader in) throws IOException {
        System.out.print("Enter Menu Item ID: ");
        int itemId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter New Menu Item Name: ");
        String newItemName = scanner.nextLine();
        System.out.print("Enter New Menu Item Price: ");
        float newItemPrice = Float.parseFloat(scanner.nextLine());
        System.out.print("Is the item available? (true/false): ");
        boolean newAvailability = Boolean.parseBoolean(scanner.nextLine());

        out.println(itemId);
        out.println(newItemName);
        out.println(newItemPrice);
        out.println(newAvailability);

        System.out.println("Response: " + in.readLine());
    }

    private static void handleDeleteMenuItem(Scanner scanner, PrintWriter out, BufferedReader in) throws IOException {
        System.out.print("Enter Menu Item ID: ");
        int deleteItemId = Integer.parseInt(scanner.nextLine());
        out.println(deleteItemId);
        System.out.println("Response: " + in.readLine());
    }

    private static void handleSendRecommendation(Scanner scanner, PrintWriter out, BufferedReader in) throws IOException {
        System.out.print("Enter Menu Item IDs (comma separated): ");
        String ids = scanner.nextLine();
        out.println(ids);
        System.out.println("Response: " + in.readLine());
    }

    private static void handleViewFeedbackReports(BufferedReader in) throws IOException {
        System.out.println("Feedback Reports:");
        String reportLine;
        while (!(reportLine = in.readLine()).isEmpty()) {
            System.out.println(reportLine);
        }
    }

    private static void handleViewFinalMenu(BufferedReader in,Scanner scanner) throws IOException {
        System.out.println("Final Menu:");
        String menuItem;
        while ((menuItem = in.readLine()) != null && !menuItem.isEmpty()) {
            System.out.println(menuItem);
        }
}

    private static void handleViewVotingResults(BufferedReader in) throws IOException {
        System.out.println("Voting Results:");
        String resultLine;
        while (!(resultLine = in.readLine()).isEmpty()) {
            System.out.println(resultLine);
        }
    }

    private static void handleChooseFinalMenu(Scanner scanner, PrintWriter out, BufferedReader in) throws IOException {
        System.out.print("Enter Final Menu Item IDs (comma separated): ");
        String finalMenuIds = scanner.nextLine();
        out.println(finalMenuIds);
        System.out.println("Response: " + in.readLine());
    }
}
