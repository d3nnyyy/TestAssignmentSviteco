package ua.dtsebulia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler extends Thread {

    private Socket clientSocket;
    private Map<String, String> commands;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.commands = loadCommandsFromFile();
    }

    private Map<String, String> loadCommandsFromFile() {
        Map<String, String> commands = new HashMap<>();
        commands.put("command1", "This is command 1");
        commands.put("GetSet5", "This is GetSet5 command");
        return commands;
    }

    private String processClientMessage(String clientMessage) {
        if (clientMessage.matches("[a-zA-Z]+")) {
            return alternateCaseAndUnderscore(clientMessage);
        } else if (clientMessage.matches("\\d+")) {
            int number = Integer.parseInt(clientMessage);
            return String.valueOf(number * 1000);
        } else {
            return processCommand(clientMessage);
        }
    }

    private String processCommand(String clientMessage) {
        return commands.getOrDefault(clientMessage, "Unknown command");
    }

    private String alternateCaseAndUnderscore(String clientMessage) {
        StringBuilder result = new StringBuilder();
        boolean upperCase = true;

        for (char c : clientMessage.toCharArray()) {
            if (Character.isLetter(c)) {
                result.append(upperCase ? Character.toUpperCase(c) : Character.toLowerCase(c));
                upperCase = !upperCase;
            } else if (Character.isWhitespace(c)) {
                result.append("_");
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    @Override
    public void run() {

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            String clientMessage;

            while ((clientMessage = reader.readLine()) != null) {

                System.out.println("Client message: " + clientMessage);
                String response = processClientMessage(clientMessage);

                writer.println("Server response: " + response);
                System.out.println("Response sent: " + response);

            }

            clientSocket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}