import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected.");

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}