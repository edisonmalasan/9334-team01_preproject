package Client.connection;

import java.io.*;
import java.net.Socket;

public class ClientConnection {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 5000;

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

    public String sendRequest(String request) {
        try {
            System.out.println("requesting server: " + request);
            output.println(request);
            return input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
