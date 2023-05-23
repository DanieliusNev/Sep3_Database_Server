package handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserHandler {
    private Connection connection;

    public UserHandler(Connection connection) {
        this.connection = connection;
    }

    public String handleRequest(String action, JsonNode requestDataNode) {
        switch (action) {
            case "getUsers":
                return getUsers();
            case "registerUser":
                User user = extractUserFromJson(requestDataNode);
                return registerUser(user);
            case "loginUser":
                User userLogin = extractUserFromJson(requestDataNode);
                return loginUser(userLogin);

                default:
                return "Invalid action";
        }
    }

    private String getUsers() {
        List<User> users = new ArrayList<>();

        String query = "SELECT * FROM user_profile";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");

                User user = new User(id, username, password);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving users";
        }

        return users.toString();
    }

    private String registerUser(User user) {
        String query = "INSERT INTO user_profile (username, password) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                return "User registered successfully";
            } else {
                return "Failed to register user";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to register user";
        }
    }
    private String loginUser(User user) {
        String query = "SELECT * FROM user_profile WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // User credentials are valid
                long id = resultSet.getLong("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                User user2 = new User(id, username, password);

                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.writeValueAsString(user2);
            } else {
                // User credentials are invalid
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private User extractUserFromJson(JsonNode jsonNode) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(jsonNode, User.class);
    }
}
