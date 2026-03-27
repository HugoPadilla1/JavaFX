package textgame_update;

public class Puzzle {
    private String description;
    private String correctAnswer;
    private int allowedAttempts;
    private int remainingAttempts;
    private boolean solved;
    private int roomNumber;

    public Puzzle(String description, String correctAnswer, int allowedAttempts, int roomNumber) {
        this.description = description;
        this.correctAnswer = correctAnswer;
        this.allowedAttempts = allowedAttempts;
        this.remainingAttempts = allowedAttempts;
        this.solved = false;
        this.roomNumber = roomNumber;
    }

    public String getDescription() {
        return description;
    }

    public int getAllowedAttempts() {
        return allowedAttempts;
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    public boolean isSolved() {
        return solved;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public boolean isInRoom(int roomNumber) {
        return !solved && this.roomNumber == roomNumber;
    }

    public boolean checkAnswer(String answer) {
        if (correctAnswer.equalsIgnoreCase(answer.trim())) {
            solved = true;
            return true;
        }
        remainingAttempts--;
        return false;
    }

    public boolean hasAttemptsRemaining() {
        return remainingAttempts > 0;
    }

    public void resetAttempts() {
        if (!solved) {
            remainingAttempts = allowedAttempts;
        }
    }
}
