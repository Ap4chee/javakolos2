package com.example.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AdministratorHandler implements Runnable {
    private Socket clientSocket;

    public AdministratorHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            // Weryfikacja hasła
            out.println("Enter admin password:");
            String passwordAttempt = in.readLine();

            if (passwordAttempt.equals(Server.getAdminPassword())) {
                out.println("Welcome, administrator!");
                // Tutaj można dodać logikę do wykonania przez administratora po autoryzacji
            } else {
                out.println("Incorrect password. Disconnecting...");
                clientSocket.close();
            }

        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }
}
