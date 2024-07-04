package com.example.demo;

import com.example.demo.controllers.TokenManager;

import java.io.*;
import java.net.Socket;

public class AdministratorHandler implements Runnable {
    private final Socket clientSocket;
    private final TokenManager tokenManager;

    public AdministratorHandler(Socket clientSocket, TokenManager tokenManager) {
        this.clientSocket = clientSocket;
        this.tokenManager = tokenManager;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            out.println("Enter admin password:");
            String password = in.readLine();

            if (!"admin123".equals(password)) {
                out.println("Invalid password. Disconnecting...");
                clientSocket.close();
                return;
            }

            out.println("Admin connected. Enter commands:");

            String command;
            while ((command = in.readLine()) != null) {
                if (command.startsWith("ban ")) {
                    String token = command.substring(4).trim();
                    int recordsRemoved = tokenManager.banToken(token);
                    out.println("Token banned. Records removed: " + recordsRemoved);
                } else {
                    out.println("Unknown command");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
