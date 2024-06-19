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
            System.out.println("1. Login as Admin");
            System.out.println("2. Login as Chef");
            System.out.println("3. Login as Employee");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            String role = "";
            switch (choice) {
                case 1:
                    role = "Admin";
                    break;
                case 2:
                    role = "Chef";
                    break;
                case 3:
                    role = "Employee";
                    break;
                default:
                    System.out.println("Invalid choice");
                    return;
            }

            System.out.print("Enter Employee ID: ");
            String employeeId = scanner.nextLine();
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();

            out.println(role);
            out.println(employeeId);
            out.println(name);

            String serverResponse = in.readLine();
            System.out.println(serverResponse);

            if (serverResponse.startsWith("Login successful")) {
                System.out.println("Commands: ");
                System.out.println("GET_MENU - To get the menu items");
                System.out.println("GIVE_FEEDBACK - To give feedback");

                while (true) {
                    String command = scanner.nextLine();
                    out.println(command);

                    if ("GIVE_FEEDBACK".equals(command)) {
                        System.out.print("Enter Menu Item ID: ");
                        int menuItemId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        System.out.print("Enter Comment: ");
                        String comment = scanner.nextLine();

                        System.out.print("Enter Rating (1-5): ");
                        int rating = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        out.println(menuItemId);
                        out.println(comment);
                        out.println(rating);
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
}
