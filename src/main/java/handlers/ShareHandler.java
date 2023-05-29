package handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.SharedExercise;
import model.SharedPost;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class ShareHandler {
    private Connection connection;

    public ShareHandler(Connection connection) {
        this.connection = connection;
    }

    public String handleRequest(String action, JsonNode requestDataNode) {
        switch (action) {
            case "shareExercises":
                String userIdString = requestDataNode.get("sharedBy").asText();
                int userId = parseInt(userIdString);
                System.out.println(userId);
                List<Integer> exerciseIds = extractExerciseIds(requestDataNode);
                String comment = requestDataNode.get("comment").asText();
                return shareExercises(userId, exerciseIds, comment);
            case "getSharedPostsByUser":
                System.out.println("reached case");
                return getSharedPosts();
            default:
                return "Invalid action";
        }
    }

    /*public String shareExercises(int userId, List<Integer> exerciseIds, String comment) {
        String sharedPostsQuery = "INSERT INTO shared_posts (shared_by, comment) VALUES (?, ?)";
        String sharedPostExercisesQuery = "INSERT INTO shared_post_exercises (shared_post_id, exercise_id) VALUES (?, ?)";

        try (PreparedStatement sharedPostsStatement = connection.prepareStatement(sharedPostsQuery, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement sharedPostExercisesStatement = connection.prepareStatement(sharedPostExercisesQuery)) {

            sharedPostsStatement.setInt(1, userId);
            sharedPostsStatement.setString(2, comment);
            sharedPostsStatement.executeUpdate();

            ResultSet generatedKeys = sharedPostsStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int sharedPostId = generatedKeys.getInt(1);

                for (int exerciseId : exerciseIds) {
                    sharedPostExercisesStatement.setInt(1, sharedPostId);
                    sharedPostExercisesStatement.setInt(2, exerciseId);
                    sharedPostExercisesStatement.executeUpdate();
                }
            }

            return "Exercises shared successfully";
        } catch (SQLException e) {
            throw new RuntimeException("Failed to share exercises", e);
        }
    }*/
    public String shareExercises(int userId, List<Integer> exerciseIds, String comment) {
        String sharedPostsQuery = "INSERT INTO shared_posts (shared_by, comment) VALUES (?, ?)";
        String sharedPostExercisesQuery = "INSERT INTO shared_post_exercises (shared_post_id, exercise_id) VALUES (?, ?)";

        try (PreparedStatement sharedPostsStatement = connection.prepareStatement(sharedPostsQuery, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement sharedPostExercisesStatement = connection.prepareStatement(sharedPostExercisesQuery)) {

            sharedPostsStatement.setInt(1, userId);
            sharedPostsStatement.setString(2, comment);
            sharedPostsStatement.executeUpdate();

            ResultSet generatedKeys = sharedPostsStatement.getGeneratedKeys();
            int sharedPostId;
            if (generatedKeys.next()) {
                sharedPostId = generatedKeys.getInt(1);
                System.out.println("postid generated one: " + sharedPostId);
                for (int exerciseId : exerciseIds) {
                    sharedPostExercisesStatement.setInt(1, sharedPostId);
                    System.out.println("postid generated2: " + sharedPostId);
                    sharedPostExercisesStatement.setInt(2, exerciseId);
                    System.out.println("exID: " + exerciseId);
                    sharedPostExercisesStatement.executeUpdate();
                }
            } else {
                throw new SQLException("Failed to retrieve generated keys for shared_posts");
            }

            return "Exercises shared successfully";
        } catch (SQLException e) {
            throw new RuntimeException("Failed to share exercises", e);
        }
    }




    public String getSharedPosts() {
        List<SharedPost> sharedPosts = new ArrayList<>();
        /*String query = "SELECT sp.id AS shared_post_id, sp.shared_date, sp.comment, u.username, e.title " +
                "FROM shared_posts sp " +
                "JOIN user_profile u ON sp.shared_by = u.id " +
                "JOIN shared_post_exercises sed ON sp.id = sed.shared_post_id " +
                "JOIN exercises e ON sed.exercise_id = e.id " +
                "ORDER BY sp.shared_date DESC";*/
        String query = "SELECT sp.id AS shared_post_id, sp.shared_date, sp.comment, u.username, e.title " +
                "FROM shared_posts sp " +
                "JOIN user_profile u ON sp.shared_by = u.id " +
                "JOIN shared_post_exercises sed ON sp.id = sed.shared_post_id " +
                "JOIN exercises e ON sed.exercise_id = e.id " +
                "ORDER BY sp.shared_date DESC";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int sharedPostId = resultSet.getInt("shared_post_id");
                String sharedDate = resultSet.getString("shared_date");
                String comment = resultSet.getString("comment");
                String sharedBy = resultSet.getString("username");
                String exerciseTitle = resultSet.getString("title");

                SharedPost sharedPost = sharedPosts.stream()
                        .filter(post -> post.getSharedPostId() == sharedPostId)
                        .findFirst()
                        .orElse(null);

                if (sharedPost == null) {
                    sharedPost = new SharedPost(sharedPostId, sharedDate, comment, sharedBy);
                    sharedPosts.add(sharedPost);
                }

                SharedExercise sharedExercise = new SharedExercise(exerciseTitle);
                sharedPost.addExerciseTitle(sharedExercise);
            }
            System.out.println("trying to return posts");
            return sharedPostsToString(sharedPosts);
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving shared posts", e);
        }
    }



    private List<Integer> extractExerciseIds(JsonNode requestDataNode) {
        List<Integer> exerciseIds = new ArrayList<>();
        JsonNode exercisesNode = requestDataNode.get("exerciseTitles");
        if (exercisesNode != null && exercisesNode.isArray()) {
            for (JsonNode exerciseNode : exercisesNode) {
                int exerciseId = exerciseNode.get("exerciseId").asInt();
                exerciseIds.add(exerciseId);
            }
        }
        return exerciseIds;
    }

    private String sharedPostsToString(List<SharedPost> sharedPosts) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(sharedPosts);
        } catch (Exception e) {
            throw new RuntimeException("Error converting shared posts to JSON", e);
        }
    }

}
