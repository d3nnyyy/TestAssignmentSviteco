# TCP/IP Server-Client Communication

This project implements a simple TCP/IP server-client communication system. The server receives messages from the client and performs various operations on the received data, including transforming strings, executing predefined commands, and sending periodic messages to the client.

## Server Application

The server application is responsible for handling client connections and processing messages. It includes the following functionalities:

- Accepts connections from multiple clients.
- Loads a set of predefined commands from a "Commands" file.
- Processes client messages, which can be one of the following:
  - A number: Multiplies the number by 1000 and sends it back to the client.
  - A string: Alternates between converting letters to uppercase and lowercase, replaces spaces with underscores, and sends the modified string to the client.
  - A command: Looks up the corresponding response in the "Commands" file and sends it back to the client.
  - Unknown command: If the command is not found, it sends "Unknown command" to the client.
- Logs received messages and server responses in a "LogFile.log" file.
- Periodically sends messages to clients every 10 seconds, with a counter and the current timestamp.

## Client Application

The client application is responsible for connecting to the server and sending messages. It includes the following functionalities:

- Connects to the server using a hostname and port (localhost and 12345 by default).
- Accepts user input for messages to send to the server.
- Sends user input to the server.
- Receives and displays responses from the server.
- Supports the "exit" command to disconnect from the server.

## Usage

### Server Application

1. Run the `Server` class to start the server.
2. The server will listen for incoming client connections.
3. Predefined commands are loaded from the "Commands" file.
4. The server processes client messages and sends responses.

### Client Application

1. Run the `Client` class to start the client.
2. The client connects to the server.
3. Enter a command or message (type "exit" to quit) in the client console.
4. The client sends the input to the server and displays the server's response.
5. Periodic messages from the server will also be displayed.

## Files

- `Client.java`: Client application code.
- `Server.java`: Server application code.
- `ClientHandler.java`: Class for handling client connections and processing messages on the server.
- `Commands`: File containing predefined commands and their responses.
- `LogFile.log`: Log file for recording received messages and server responses.
