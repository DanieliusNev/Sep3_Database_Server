package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
    private final String url;
    private final String username;
    private final String password;

    private final List<Connection> connections;

    public ConnectionPool(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;

        this.connections = new ArrayList<>();
    }

    public synchronized Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            connections.add(connection);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized void releaseConnection(Connection connection) {
        if (connection != null) {
            connections.remove(connection);
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void closeAllConnections() {
        for (Connection connection : connections) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        connections.clear();
    }
}
