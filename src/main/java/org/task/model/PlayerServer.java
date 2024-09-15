package org.task.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The {@code PlayerServer} class is responsible for handling server-side communication with clients.
 * It listens on a specified port for client connections and facilitates a message exchange
 * with the connected client. The server limits the client to a maximum of 10 messages.
 * <p>
 * It manages the lifecycle of the server including resource management and cleanup.
 */
public class PlayerServer {

    public static void main(String[] args) throws IOException {
        PlayerServer playerServer = new PlayerServer(12345);
        playerServer.start();
    }

    private final ServerSocket serverSocket;
    private final Socket clientSocket;
    private final PrintWriter out;
    private final BufferedReader in;

    /**
     * Constructs a {@code PlayerServer} and binds it to the specified port.
     * The server listens for a client connection and sets up I/O streams.
     *
     * @param port The port on which the server will listen for connections.
     */
    public PlayerServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.clientSocket = serverSocket.accept();
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    /**
     * Starts the server, enabling it to receive and process messages from the client.
     * The server limits the client to sending 10 messages.
     */
    public void start() throws IOException {
        System.out.println("Server started");
        int counter = 0;
        while (true) {
            if (counter >= 10) {
                out.println("You are out of limit (10 messages)");
                break;
            }
            String inputLine = in.readLine();
            if (inputLine == null || inputLine.isEmpty()) {
                break;
            }
            System.out.println("Received from a client: " + inputLine + ". PID: " + ProcessHandle.current().pid());
            out.println(inputLine + " " + ++counter);
        }
        stop();
    }

    /**
     * Stops the server by closing sockets and releasing resources.
     */
    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }
}