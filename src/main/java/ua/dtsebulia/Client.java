package ua.dtsebulia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A class to handle communication with a client.
 */
public class Client {
    /**
     * Main method to run the client application.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345)){

            // Print a message that the connection was successful.
            System.out.println("Connected to the server.");

            // Create a BufferedReader to read user input from the console.
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            // Create a PrintWriter to send messages to the server.
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Create a BufferedReader to read messages from the server.
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String userInputString;

            // Loop until the user types "exit".
            while (true) {

                // Prompt the user to enter a command or message.
                System.out.print("Enter a command or message (type 'exit' to quit): ");

                // Read a line of user input.
                userInputString = userInput.readLine();

                // If the user entered "exit", then exit the loop.
                if (userInputString.equalsIgnoreCase("exit")) {
                    break;
                }

                // Send the user input to the server.
                writer.println(userInputString);

                // Read a response from the server.
                String serverResponse = reader.readLine();

                // Print the response to the console.
                System.out.println("Server response: " + serverResponse);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
