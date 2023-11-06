package ua.dtsebulia;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    /**
     * Main method to run the server application.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        try {

            // Create a new ServerSocket to listen for connections.
            ServerSocket serverSocket = new ServerSocket(12345);

            // Print a message that the server is ready to accept connections.
            System.out.println("Server started");
            createCommandsFile();

            // Listen for connections until the program is terminated.
            while (true) {

                // Accept a new client connection.
                Socket clientSocket = serverSocket.accept();

                // Print a message that a new client has connected.
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Create a new ClientHandler for the client.
                ClientHandler clientHandler = new ClientHandler(clientSocket);

                // Start the ClientHandler in a new thread.
                clientHandler.start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a "Commands" file with initial commands.
     */
    private static void createCommandsFile() {

        // Try-with-resources block to automatically close the PrintWriter.
        try (PrintWriter writer = new PrintWriter("Commands")) {

            // Add initial commands to the "Commands" file.
            writer.println("command1:This is command 1");
            writer.println("GetSet5:This is GetSet5 command");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
