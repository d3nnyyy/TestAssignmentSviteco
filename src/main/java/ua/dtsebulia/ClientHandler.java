package ua.dtsebulia;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ClientHandler extends Thread {

    private Socket clientSocket;
    private Logger logger;
    private Map<String, String> commands;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.commands = loadCommandsFromFile();
        initializeLogger();
    }

    private void initializeLogger() {
        logger = Logger.getLogger(ClientHandler.class.getName());

        try {
            FileHandler fileHandler = new FileHandler("LogFile.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> loadCommandsFromFile() {
        Map<String, String> commands = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("Commands"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    commands.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return commands;
    }


    private String processClientMessage(String clientMessage) {


        if (clientMessage.matches("\\d+")) {
            int number = Integer.parseInt(clientMessage);
            return String.valueOf(number * 1000);
        } else if (clientMessage.matches(".*[a-zA-Z0-9]+.*")) {
            return alternateCaseAndUnderscore(clientMessage);
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

                logger.info("Client message: " + clientMessage);
                logger.info("Server response: " + response);
            }

            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
