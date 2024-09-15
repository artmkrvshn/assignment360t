package org.task;

import org.task.model.Player;
import org.task.model.PlayerServer;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    /**
     * The main method demonstrates how to use a virtual thread-based {@link ExecutorService}
     * to run both the {@link PlayerServer} and {@link Player} concurrently with the same PIDs.
     */
    public static void main(String[] args) {
        try (ExecutorService service = Executors.newVirtualThreadPerTaskExecutor()) {
            service.submit(() -> {
                try {
                    PlayerServer playerServer = new PlayerServer(12345);
                    playerServer.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            service.submit(() -> {
                try {
                    Player player = new Player("localhost", 12345);
                    player.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}