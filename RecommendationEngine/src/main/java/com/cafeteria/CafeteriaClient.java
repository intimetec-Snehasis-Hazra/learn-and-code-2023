package src.main.java.com.cafeteria;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class CafeteriaClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to the Cafeteria Recommendation System");
            System.out.println("1. Login");
            System.out.println("2. Exit");
            System.out.print("Please choose an option: ");
            String choice = scanner.nextLine();

            if (choice.equals("2")) {
                System.out.println("Exiting the system. Goodbye!");
                break;
            } else if (choice.equals("1")) {
                if (!login(scanner)) {
                    break;
                }
            } else {
                System.out.println("Invalid choice. Please enter 1 to Login or 2 to Exit.");
            }
        }
        scanner.close();
    }

    private static boolean login(Scanner scanner) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

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
                        handleViewFinalMenu(in, scanner);
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
            System.out.println("SHOW_LOGIN_LOGOUT_HISTORY - To show login/logout history");
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
        int itemId = Integer.parseInt(scanner.nextLine());
        out.println(itemId);
        System.out.println("Response: " + in.readLine());
    }

    private static void handleShowLoginHistory(Scanner scanner, PrintWriter out, BufferedReader in) throws IOException {
        out.println("SHOW_LOGIN_LOGOUT_HISTORY");
        System.out.println("Login/Logout History:");
        String historyLine;
        while (!(historyLine = in.readLine()).isEmpty()) {
            System.out.println(historyLine);
        }
    }

    private static void handleSendRecommendation(Scanner scanner, PrintWriter out, BufferedReader in) throws IOException {
        System.out.print("Enter Recommendation: ");
        String recommendation = scanner.nextLine();
        out.println(recommendation);
        System.out.println("Response: " + in.readLine());
    }

    private static void handleViewFeedbackReports(BufferedReader in) throws IOException {
        System.out.println("Feedback Reports:");
        String report;
        while (!(report = in.readLine()).isEmpty()) {
            System.out.println(report);
        }
    }

    private static void handleViewVotingResults(BufferedReader in) throws IOException {
        System.out.println("Voting Results:");
        String result;
        while (!(result = in.readLine()).isEmpty()) {
            System.out.println(result);
        }
    }

    private static void handleChooseFinalMenu(Scanner scanner, PrintWriter out, BufferedReader in) throws IOException {
        System.out.println("Choosing Final Menu:");
        String menuItem;
        while (!(menuItem = in.readLine()).isEmpty()) {
            System.out.println(menuItem);
        }

        System.out.print("Enter the IDs of the chosen menu items (comma-separated): ");
        String chosenItems = scanner.nextLine();
        out.println(chosenItems);

        System.out.println("Response: " + in.readLine());
    }

    private static void handleViewFinalMenu(BufferedReader in, Scanner scanner) throws IOException {
        System.out.println("Final Menu:");
        String menuItem;
        while (!(menuItem = in.readLine()).isEmpty()) {
            System.out.println(menuItem);
        }
    }
}
