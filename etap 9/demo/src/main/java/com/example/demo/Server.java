package com.example.demo;

import com.example.demo.controllers.ImageController;
import com.example.demo.controllers.TokenManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 9090;
    private static final String ADMIN_PASSWORD = "admin123"; // Hasło administratora
    private static final TokenManager tokenManager = new TokenManager(); // Single instance
    private static final ImageController imageController = new ImageController(); // Single instance

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Tworzenie wątku obsługującego klienta
                Thread clientThread = new Thread(new AdministratorHandler(clientSocket, tokenManager, imageController));
                clientThread.start();
            }

        } catch (IOException e) {
            System.err.println("Error in the server: " + e.getMessage());
        }
    }
}
