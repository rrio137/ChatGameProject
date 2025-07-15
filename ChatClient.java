import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 12345;

        try (Socket socket = new Socket(serverAddress, port)) {
            System.out.println("Connected to the server.");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            Thread readerThread = new Thread(() -> {
                String serverMessage;
                try {
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println("\n[Message from server]: " + serverMessage);
                        System.out.print("You: ");
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            });

            readerThread.start();

            String userMessage;
            while (true) {
                System.out.print("You: ");
                userMessage = userInput.readLine();
                out.println(userMessage);

                if (userMessage.equalsIgnoreCase("/exit")) {
                    System.out.println("Goodbye!");
                    socket.close();
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}