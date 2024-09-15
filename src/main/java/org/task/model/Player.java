package org.task.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

/**
 * The {@code Player} class is responsible for client-side communication with the {@code PlayerServer}.
 * It connects to the server, sends user input, and receives responses from the server.
 * <p>
 * It manages the client connection lifecycle and ensures proper cleanup of resources.
 */
public class Player {

    public static void main(String[] args) throws IOException {
        Player player = new Player("localhost", 12345);
        player.start();
    }

    protected Socket clientSocket;
    protected PrintWriter out;
    protected BufferedReader in;

    /**
     * Constructs a {@code Player} and attempts to connect to the specified server at the given host and port.
     * Sets up the I/O streams for communication with the server.
     *
     * @param host The host or IP address of the server.
     * @param port The port on which the server is listening.
     */
    public Player(String host, int port) throws IOException {
        try {
            this.clientSocket = new Socket(host, port);
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (ConnectException e) {
            System.err.println("Could not connect to " + host + ":" + port);
        }
    }

    /**
     * Starts the client, allowing the user to send messages to the server via the console.
     * The client continuously reads user input and sends it to the server,
     * displaying the server's responses until the user decides to terminate the session.
     */
    public void start() throws IOException {
        try (BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.print("Client: ");
                String userMessage = userInput.readLine();
                if (userMessage.isEmpty()) {
                    out.println("bye");
                    break;
                }
                out.println(userMessage);
                String response = in.readLine();
                if (response.isEmpty()) {
                    break;
                }
                System.out.println("Response from a server: " + response + ". PID: " + ProcessHandle.current().pid());
            }
        } catch (IOException e) {
            System.out.println("Client disconnected.");
            e.printStackTrace();
        } finally {
            stop();
        }
    }

    /**
     * Stops the client by closing the socket connection and releasing resources.
     */
    public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}