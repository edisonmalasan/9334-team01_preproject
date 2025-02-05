package Server.handler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            input = new ObjectInputStream(clientSocket.getInputStream());
            output = new ObjectOutputStream(clientSocket.getOutputStream());

            // Communication logic here
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}