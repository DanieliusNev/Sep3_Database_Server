package model;

import java.util.ArrayList;
import java.util.List;

public class SharedPost {
    private int sharedPostId;
    private String sharedDate;
    private String comment;
    private String sharedBy;
    private List<SharedExercise> exerciseTitles;

    public SharedPost(int sharedPostId, String sharedDate, String comment, String sharedBy) {
        this.sharedPostId = sharedPostId;
        this.sharedDate = sharedDate;
        this.comment = comment;
        this.sharedBy = sharedBy;
        this.exerciseTitles = new ArrayList<>();
    }

    public void addExerciseTitle(SharedExercise exerciseTitle) {
        exerciseTitles.add(exerciseTitle);
    }

    public int getSharedPostId() {
        return sharedPostId;
    }

    public void setSharedPostId(int sharedPostId) {
        this.sharedPostId = sharedPostId;
    }

    public String getSharedDate() {
        return sharedDate;
    }

    public void setSharedDate(String sharedDate) {
        this.sharedDate = sharedDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSharedBy() {
        return sharedBy;
    }

    public void setSharedBy(String sharedBy) {
        this.sharedBy = sharedBy;
    }

    public List<SharedExercise> getExerciseTitles() {
        return exerciseTitles;
    }

    public void setExerciseTitles(List<SharedExercise> exerciseTitles) {
        this.exerciseTitles = exerciseTitles;
    }

    @Override
    public String toString() {
        return "SharedPost{" +
                "sharedPostId=" + sharedPostId +
                ", sharedDate='" + sharedDate + '\'' +
                ", comment='" + comment + '\'' +
                ", sharedBy='" + sharedBy + '\'' +
                ", exerciseTitles=" + exerciseTitles +
                '}';
    }
}