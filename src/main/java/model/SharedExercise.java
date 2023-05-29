package model;

public class SharedExercise {
    private String exerciseTitle;

    public SharedExercise(String exerciseTitle) {
        this.exerciseTitle = exerciseTitle;
    }

    public String getExerciseTitle() {
        return exerciseTitle;
    }

    public void setExerciseTitle(String exerciseTitle) {
        this.exerciseTitle = exerciseTitle;
    }

    @Override
    public String toString() {
        return "SharedExercise{" +
                "exerciseTitle='" + exerciseTitle + '\'' +
                '}';
    }
}

