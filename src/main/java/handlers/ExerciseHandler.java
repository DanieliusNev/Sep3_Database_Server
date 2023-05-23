package handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Exercise;
import model.User;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExerciseHandler {
    private Connection connection;

    public ExerciseHandler(Connection connection) {
        this.connection = connection;
    }

    public String handleRequest(String action, JsonNode requestDataNode) {
        switch (action) {
            case "getExercisesByUserId":
                int userId = requestDataNode.get("userId").asInt();
                return getExercisesByUserId(userId);
            case "PostExercise":
                Exercise exercisePost = extractExerciseFromJson(requestDataNode);
                return registerExercise(exercisePost);
            default:
                return "Invalid action";
        }
    }

    public String getExercisesByUserId(int userId) {
        List<Exercise> exercises = new ArrayList<>();

        String query = "SELECT * FROM exercises WHERE id_user = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String title = resultSet.getString("title");
                Date dateNumber = resultSet.getDate("date_number");

                Exercise exercise = new Exercise(id, title, dateNumber, userId);
                exercises.add(exercise);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving exercises", e);
        }

        return exercises.toString();
    }

    public String registerExercise(Exercise exercise) {
        String query = "INSERT INTO exercises (title, date_number, id_user) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, exercise.getTitle());
            statement.setDate(2, exercise.getDateNumber());
            statement.setInt(3, exercise.getUserId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return "Exercise registered successfully";
            } else {
                return "Failed to register exercise";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to register exercise";
        }
    }

    private Exercise extractExerciseFromJson(JsonNode jsonNode) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(jsonNode, Exercise.class);
    }






}
