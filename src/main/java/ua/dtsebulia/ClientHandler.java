package ua.dtsebulia;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * A class to handle communication with a client.
 */
public class ClientHandler extends Thread {

    private Socket clientSocket;
    private Logger logger;
    private Map<String, String> commands;

    /**
     * Constructor for the ClientHandler class.
     *
     * @param clientSocket The client socket to handle communication with the client.
     */
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.commands = loadCommandsFromFile();
        initializeLogger();
    }

    /**
     * Initializes the logger for this ClientHandler.
     */
    private void initializeLogger() {
        logger = Logger.getLogger(ClientHandler.class.getName());

        try {

            // Create a new FileHandler to log to a file.
            FileHandler fileHandler = new FileHandler("LogFile.log", true);

            // Set the formatter for the FileHandler.
            fileHandler.setFormatter(new SimpleFormatter());

            // Add the FileHandler to the logger.
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads commands from a file and returns them as a map.
     *
     * @return A map containing the loaded commands.
     */
    private Map<String, String> loadCommandsFromFile() {

        // Create a new HashMap to store the commands.
        Map<String, String> commands = new HashMap<>();

        try {

            // Create a new BufferedReader to read the file.
            BufferedReader reader = new BufferedReader(new FileReader("Commands"));
            String line;
            while ((line = reader.readLine()) != null) {

                // Split the line into two parts separated by a colon.
                String[] parts = line.split(":");

                // If the line contains two parts, add them to the map.
                if (parts.length == 2) {
                    commands.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return commands;
    }

    /**
     * Processes the client message and returns a response.
     *
     * @param clientMessage The message received from the client.
     * @return The response to be sent back to the client.
     */
    private String processClientMessage(String clientMessage) {

            // If the message is a number, multiply it by 1000.
        if (clientMessage.matches("\\d+")) {
            int number = Integer.parseInt(clientMessage);
            return String.valueOf(number * 1000);

            // If the message contains letters or numbers, alternate the case of letters and replace whitespace with underscores.
        } else if (clientMessage.matches(".*[a-zA-Z0-9]+.*")) {
            return alternateCaseAndUnderscore(clientMessage);

            // In all other cases, process the message as a command.
        } else {
            return processCommand(clientMessage);
        }
    }

    /**
     * Processes a command from the client.
     *
     * @param clientMessage The command received from the client.
     * @return The response to the command or "Unknown command" if not found.
     */
    private String processCommand(String clientMessage) {

        // Return the response to the command or "Unknown command" if not found.
        return commands.getOrDefault(clientMessage, "Unknown command");
    }

    /**
     * Alternates the case of letters and replaces whitespace with underscores.
     *
     * @param clientMessage The input string to process.
     * @return The modified string with alternating case and underscores.
     */
    private String alternateCaseAndUnderscore(String clientMessage) {

        // Create a new StringBuilder to store the result.
        StringBuilder result = new StringBuilder();

        // Create a boolean to track whether the next letter should be upper case.
        boolean upperCase = true;

        // Loop through each character in the string.
        for (char c : clientMessage.toCharArray()) {

            // If the character is a letter, alternate the case of the letter.
            if (Character.isLetter(c)) {

                // Append the letter to the result, using the upperCase boolean to determine the case.
                result.append(upperCase ? Character.toUpperCase(c) : Character.toLowerCase(c));
                upperCase = !upperCase;

                // If the character is whitespace, append an underscore to the result.
            } else if (Character.isWhitespace(c)) {
                result.append("_");

                // If the character is not a letter or whitespace, append the character to the result.
            } else {
                result.append(c);
            }
        }

        // Return the result as a string.
        return result.toString();
    }

    /**
     * The main method to run the ClientHandler.
     */
    @Override
    public void run() {
        try {

            // Create a new BufferedReader to read messages from the client.
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Create a new PrintWriter to send messages to the client.
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            // Initialize the clientMessage variable.
            String clientMessage;

            // Loop until the client disconnects.
            while ((clientMessage = reader.readLine()) != null) {

                // Print the client message to the console.
                System.out.println("Client message: " + clientMessage);

                // Process the client message.
                String response = processClientMessage(clientMessage);

                // Send the response to the client.
                writer.println("Server response: " + response);

                // Print the response to the console.
                System.out.println("Response sent: " + response);

                // Log the client message and response.
                logger.info("Client message: " + clientMessage);
                logger.info("Server response: " + response);
            }

            // Close the client socket.
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
