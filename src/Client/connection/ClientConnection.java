package Client.connection;

import common.Response;
import common.model.QuestionModel;
import exception.ConnectionException;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

import static common.Protocol.IP_ADDRESS;
import static common.Protocol.PORT_NUMBER;

public class ClientConnection {
    private static ClientConnection instance;
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private static final Logger logger = Logger.getLogger(ClientConnection.class.getName());

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    private ClientConnection() throws ConnectionException {
        // to not connect immediately
    }

    public static ClientConnection getInstance() throws ConnectionException {
        if (instance == null) {
            instance = new ClientConnection();
        }
        return instance;
    }

    public void connect() throws ConnectionException {
        if (socket == null || socket.isClosed()) {
            try {
                socket = new Socket(IP_ADDRESS, PORT_NUMBER);
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                logger.info("Connected to the server.");
            } catch (IOException e) {
                logger.severe("Error connecting to the server: " + e.getMessage());
                throw new ConnectionException("Error connecting to the server.", e);
            }
        }
    }

    public QuestionModel getQuestion(String category) throws IOException, ClassNotFoundException {
        sendObject("GET_QUESTION:" + category);

        Response response = (Response) receiveObject();

        if (response.isSuccess()) {
            return (QuestionModel) response.getData();
        } else {
            System.err.println("Error fetching question: " + response.getMessage());
            return null;
        }
    }

    public void sendObject(Object obj) throws IOException {
        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();
    }

    public Object receiveObject() throws IOException, ClassNotFoundException {
        System.out.println("DEBUG: Waiting to receive object from server...");

        Object obj = objectInputStream.readObject();

        System.out.println("DEBUG: Received object: " + obj);
        return obj;
    }
    public void close() {
        try {
            if (socket != null) socket.close();
            if (objectInputStream != null) objectInputStream.close();
            if (objectOutputStream != null) objectOutputStream.close();
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
