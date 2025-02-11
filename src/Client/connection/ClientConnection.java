package Client.connection;

import Server.handler.ClientHandler;

import java.io.*;
import java.net.Socket;

public class ClientConnection {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    private static final String SERVER_IP = System.getenv("SERVER_IP") != null ? System.getenv("SERVER_IP") : "127.0.0.1";
    private static final int SERVER_PORT = System.getenv("SERVER_PORT") != null ? Integer.parseInt(System.getenv("SERVER_PORT")) : 5000;

    public ClientConnection() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Connected to the server.");
        } catch (IOException e) {
            System.out.println("Error connecting to the server.");
            e.printStackTrace();
        }
    }

    // for ClientServerTest
    public  boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    public String sendRequest(String request) {
        if (socket == null || !socket.isConnected()) {
            System.out.println("Not connected to the server.");
            return null;
        }

        try {
            System.out.println("Requesting server: " + request);
            output.println(request);
            return input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}