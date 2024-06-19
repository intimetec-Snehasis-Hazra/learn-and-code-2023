package src.main.java.com.cafeteria;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CafeteriaServer {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Cafeteria server is running...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");
                new ClientHandler(clientSocket).start();
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

            // Handle login and commands
            String userRole = in.readLine();
            String employeeId = in.readLine();
            String name = in.readLine();

            User user = null;
            if ("Admin".equalsIgnoreCase(userRole)) {
                user = new Admin(employeeId, name, "Admin");
            } else if ("Chef".equalsIgnoreCase(userRole)) {
                user = new Chef(employeeId, name, "Chef");
            } else if ("Employee".equalsIgnoreCase(userRole)) {
                user = new Employee(employeeId, name, "Employee");
            }

            if (user != null && user.login(employeeId, name)) {
                out.println("Login successful as " + user.getRole());
                String command;
                while ((command = in.readLine()) != null) {
                    if (command.equals("EXIT")) {
                        break;
                    }
                    processCommand(user, command);
                }
            } else {
                out.println("Invalid credentials");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processCommand(User user, String command) throws IOException {
        switch (command) {
            case "GET_MENU":
                List<MenuItem> menuItems = Database.getAllMenuItems();
                for (MenuItem item : menuItems) {
                    out.println(item.getId() + ". " + item.getName() + " - $" + item.getPrice() + " - " + (item.isAvailable() ? "Available" : "Not Available"));
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
            default:
                out.println("Unknown command");
                break;
        }
    }
}
