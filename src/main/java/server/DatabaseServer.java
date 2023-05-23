package server;

import com.fasterxml.jackson.databind.JsonNode;
import handlers.ExerciseHandler;
import handlers.UserHandler;
import model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
        import java.net.ServerSocket;
        import java.net.Socket;
        import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Handler;

public class DatabaseServer {
    private static final String DATABASE_URL = "jdbc:postgresql://localhost:5432/SEP3_WorkoutDB";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "danisnever7";
    private static final int PORT = 1234;
    private Connection connection;
    private ConnectionPool connectionPool;

    public void startServer() {
        try {
            // Create a connection pool
            connectionPool = new ConnectionPool(DATABASE_URL, USERNAME, PASSWORD);

            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Database server is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

                Thread clientThread = new Thread(() -> {
                    try {
                        handleClientRequest(clientSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClientRequest(Socket clientSocket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        String request = reader.readLine();
        System.out.println("Received request from client: " + request);

        // Parse the JSON request
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(request);

        String handlerType = jsonNode.get("handler").asText();
        String action = jsonNode.get("action").asText();
        JsonNode dataNode = jsonNode.get("data");

        String response;
        if (handlerType != null && action != null && dataNode != null) {
            try (Connection connection = connectionPool.getConnection()) {
                Handler handler;

                // Instantiate the appropriate handler based on the handler type
                if ("user".equals(handlerType)) {
                    UserHandler userHandler = new UserHandler(connection);
                    response = userHandler.handleRequest(action, dataNode);
                } else if ("exercise".equals(handlerType)) {
                    ExerciseHandler exerciseHandler = new ExerciseHandler(connection);
                    response = exerciseHandler.handleRequest(action,dataNode);
                }
                else {
                    response = "Invalid handler type";
                    writer.write(response + "\n");
                    writer.flush();
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response = "Failed to connect to the database";
            }finally {
                connectionPool.releaseConnection(connection); // Releasing the connection back to the pool,
                //bc I am using socket connection in every single http method
            }
        } else {
            response = "Invalid request format";
        }

        writer.write(response + "\n");
        writer.flush();


        System.out.println("Sent response to client: " + response);

        reader.close();
        writer.close();
        clientSocket.close();
    }




    public void stopServer() {
        if (connectionPool != null) {
            connectionPool.closeAllConnections();
        }
    }
}


