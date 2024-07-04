package com.example.demo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 9090;
    private static final String ADMIN_PASSWORD = "admin123"; // Hasło administratora

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Tworzenie wątku obsługującego klienta
                Thread clientThread = new Thread(new AdministratorHandler(clientSocket));
                clientThread.start();
            }

        } catch (IOException e) {
            System.err.println("Error in the server: " + e.getMessage());
        }
    }

    public static String getAdminPassword() {
        return ADMIN_PASSWORD;
    }
}
