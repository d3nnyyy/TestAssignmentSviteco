package ua.dtsebulia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);
            System.out.println("Connected to the server.");

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String userInputString;
            while (true) {
                System.out.print("Enter a command or message (type 'exit' to quit): ");
                userInputString = userInput.readLine();

                if (userInputString.equalsIgnoreCase("exit")) {
                    break;
                }

                out.println(userInputString);
                String serverResponse = in.readLine();
                System.out.println("Server response: " + serverResponse);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

