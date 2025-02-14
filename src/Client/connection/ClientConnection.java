package Client.connection;

import Client.model.PlayerModel;
import common.Response;
import exception.ConnectionException;
import exception.InvalidRequestException;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

import static common.Protocol.IP_ADDRESS;
import static common.Protocol.PORT_NUMBER;

public class ClientConnection {
    private ObjectOutputStream outputStream;
    private static ClientConnection instance;
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    private static final Logger logger = Logger.getLogger(ClientConnection.class.getName()); // for debugging purposes

    private ClientConnection() throws ConnectionException {
        try {
            socket = new Socket(IP_ADDRESS, PORT_NUMBER);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            outputStream = new ObjectOutputStream(socket.getOutputStream());

            logger.info("Connected to the server.");
        } catch (IOException e) {
            logger.severe("Error connecting to the server: " + e.getMessage());
            throw new ConnectionException("Error connecting to the server.", e);
        }
    }

    // singleton instance
    public static ClientConnection getInstance() throws ConnectionException {
        if (instance == null) {
            instance = new ClientConnection();
        }
        return instance;
    }

    public Response sendRequest(String request) throws ConnectionException, InvalidRequestException {

        if (socket == null || !socket.isConnected()) {
            throw new InvalidRequestException("Not Connected to the server.");
        }

        try {
            System.out.println("Requesting server: " + request);
            output.println(request);

            String response = input.readLine();
            if (response == null) {
                throw new InvalidRequestException("No response received.");
            }

            return new Response(true, "Request successful", response);
        } catch (IOException e) {
            throw new ConnectionException("Error connecting to the server.", e);
        }
    }

    public void close() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    public void sendLeaderboardUpdate(PlayerModel player) {
        try {
            outputStream.writeObject("UPDATE_LEADERBOARD");
            outputStream.writeObject(player);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // for ClientServerTest
    public  boolean isConnected() {
        return socket != null && socket.isConnected();
    }

}