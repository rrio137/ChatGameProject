import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private List<ClientHandler> clients;
    private PrintWriter out;

    public ClientHandler(Socket socket, List<ClientHandler> clients) {
        this.clientSocket = socket;
        this.clients = clients;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);
                broadcast(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clients.remove(this); // Remove from list on disconnect
                clientSocket.close();
                System.out.println("Client disconnected.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Send a message to this client
    public void sendMessage(String message) {
        out.println(message);
    }

    // Broadcast to all clients
    private void broadcast(String message) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != this) { // Skip sender if you want
                    client.sendMessage(message);
                }
            }
        }
    }
}
