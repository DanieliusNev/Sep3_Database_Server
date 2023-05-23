import server.DatabaseServer;

public class Main {
    public static void main(String[] args) {
        DatabaseServer databaseServer = new DatabaseServer();

        try {
            databaseServer.startServer();
        } finally {
            databaseServer.stopServer();
        }
    }
}
