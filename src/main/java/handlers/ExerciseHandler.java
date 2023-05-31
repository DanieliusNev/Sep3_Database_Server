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
            case "getExercisesByUserIdAndDateRange":
                int userId2 = requestDataNode.get("userId").asInt();
                Date startTime = Date.valueOf(requestDataNode.get("startDate").asText());
                Date endDate = Date.valueOf(requestDataNode.get("endDate").asText());
                return getExercisesByUserIdAndDateRange(userId2,startTime, endDate);
            case "updateExercise":
                Exercise exerciseUpdate = extractExerciseFromJson(requestDataNode);
                return updateExercise(exerciseUpdate);
            case "deleteExercise":
                long exerciseId = requestDataNode.get("exerciseId").asLong();
                return deleteExercise(exerciseId);
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

    public String getExercisesByUserIdAndDateRange(int userId, Date startDate, Date endDate) {
        List<Exercise> exercises = new ArrayList<>();

        String query = "SELECT * FROM exercises WHERE id_user = ? AND date_number BETWEEN ? AND ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setDate(2, startDate);
            statement.setDate(3, endDate);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String title = resultSet.getString("title");
                Date dateNumber = resultSet.getDate("date_number");
                String weights = resultSet.getString("weights");
                String amount = resultSet.getString("amount");
                int categoryId = resultSet.getInt("category_id");

                Exercise exercise = new Exercise(id, title, dateNumber, weights, amount, categoryId, userId);
                exercises.add(exercise);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving exercises", e);
        }

        return exercises.toString();
    }

    public String updateExercise(Exercise exercise) {
        String query = "UPDATE exercises SET title = ?, date_number = ?, weights = ?, amount = ?, category_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, exercise.getTitle());
            statement.setDate(2, exercise.getDateNumber());
            statement.setString(3, exercise.getWeights());
            statement.setString(4, exercise.getAmount());
            statement.setInt(5, exercise.getCategoryId());
            statement.setLong(6, exercise.getId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return "Exercise updated successfully";
            } else {
                return "Failed to update exercise";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to update exercise";
        }
    }

    public String registerExercise(Exercise exercise) {
        String query = "INSERT INTO exercises (title, date_number, weights, amount, category_id, id_user) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, exercise.getTitle());
            statement.setDate(2, exercise.getDateNumber());
            statement.setString(3, exercise.getWeights());
            statement.setString(4, exercise.getAmount());
            statement.setInt(5, exercise.getCategoryId());
            statement.setInt(6, exercise.getUserId());

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


    public String deleteExercise(long exerciseId) {
        String query = "DELETE FROM exercises WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, exerciseId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                return "Exercise deleted successfully";
            } else {
                return "Failed to delete exercise";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failed to delete exercise";
        }
    }

    private Exercise extractExerciseFromJson(JsonNode jsonNode) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(jsonNode, Exercise.class);
    }


}
