package TeamHydraSpring2026FX;
public class Puzzle {

    // Jahmeeks Pottinger

    private int puzzleID;
    private int allowedAttempts;
    private int remainingAttempts;
    private boolean solved;
    private String successResult;
    private String description;
    private String answer;
    private String failMessage;
    private String winMessage;
    private String hintMessage;
    private String penalty;
    private String reward;
    private java.util.ArrayList<Items> rewardItems;

    private Room currentRoom;  // Reference to the current room (set later)
    private Player player;     // Reference to the player (set later)

    public Puzzle(String description, String answer, int allowedAttempts) {
        this.description = description;
        this.answer = answer;
        this.allowedAttempts = allowedAttempts;
        this.remainingAttempts = allowedAttempts;
        this.solved = false;
        this.rewardItems = new java.util.ArrayList<>();
    }

    // Set the current room and player before entering puzzle mode
    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    // Access puzzle (e.g., show puzzle to player)
    public void accessPuzzle() {
        System.out.println("\n=== Puzzle ===");
        System.out.println(description);
        System.out.println("Attempts remaining: " + remainingAttempts);
    }

    // New method: Enter Puzzle Mode with no arguments
    public void enterPuzzleMode() {
        System.out.println("\n=== Enter Puzzle Mode ===");

        if (solved) {
            System.out.println("This puzzle has already been solved.");
            return;
        }

        // Show puzzle description and remaining attempts
        accessPuzzle();

        // Give the player an option to solve, exit, or get a hint
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        boolean playerExiting = false;

        while (remainingAttempts > 0 && !solved && !playerExiting) {
            System.out.println("\nOptions: ");
            System.out.println("1. Try to solve the puzzle");
            System.out.println("2. Get a hint");
            System.out.println("3. Exit puzzle mode");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    solvePuzzle();  // Try to solve the puzzle
                    break;

                case "2":
                    giveHint();  // Provide a hint
                    break;

                case "3":
                    exitPuzzle();  // Exit puzzle mode
                    playerExiting = true;
                    break;

                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }

        if (remainingAttempts <= 0 && !solved) {
            penalize();  // Apply penalty if attempts run out and puzzle is unsolved
        }
    }

    // Solve puzzle - returns true if solved
    public boolean solvePuzzle() {
        if (remainingAttempts <= 0) {
            System.out.println(failMessage);
            return false;
        }

        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.print("Enter your answer: ");
        String playerAnswer = scanner.nextLine().trim().toLowerCase();

        remainingAttempts--;

        if (playerAnswer.equals(answer.toLowerCase())) {
            solved = true;
            System.out.println(winMessage);
            System.out.println(successResult);

            // Display reward items
            if (rewardItems != null && !rewardItems.isEmpty()) {
                System.out.println("\n--- REWARDS ---");
                for (Items item : rewardItems) {
                    System.out.println("+ " + item.getItemName());
                }
            }

            return true;
        } else {
            System.out.println(failMessage);
            return false;
        }
    }

    // Exit puzzle interaction
    public void exitPuzzle() {
        System.out.println("Exiting puzzle...");
    }

    // Give hint for a puzzle
    public String giveHint() {
        System.out.println("Hint: " + hintMessage);
        return hintMessage;
    }

    // Reward for solving puzzle
    public void reward() {
        if (isSolved()) {
            System.out.println("Puzzle solved! " + successResult);
        }
    }

    // Penalize for failing puzzle
    public void penalize() {
        if (!isSolved() && remainingAttempts <= 0) {
            System.out.println("You failed the puzzle. You have been penalized!");
        }
    }


    /**
     * JavaFX-friendly solve method.
     * Avoids Scanner/System.in so GUI controllers can submit answers from a TextField.
     */
    public boolean solvePuzzle(String playerAnswer) {
        if (solved) {
            return true;
        }
        if (remainingAttempts <= 0) {
            return false;
        }

        remainingAttempts--;
        if (playerAnswer != null && playerAnswer.trim().equalsIgnoreCase(answer)) {
            solved = true;
            return true;
        }
        return false;
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    // Getters and setters
    public boolean isSolved() {
        return solved;
    }

    public int getPuzzleID() {
        return puzzleID;
    }

    public void setPuzzleID(int puzzleID) {
        this.puzzleID = puzzleID;
    }

    public int getAllowedAttempts() {
        return allowedAttempts;
    }

    public void setAllowedAttempts(int allowedAttempts) {
        this.allowedAttempts = allowedAttempts;
    }

    public boolean isWinCondition() {
        return solved;
    }

    public void setWinCondition(boolean winCondition) {
        this.solved = winCondition;
    }

    public String getSuccessResult() {
        return successResult;
    }

    public void setSuccessResult(String successResult) {
        this.successResult = successResult;
    }

    public String getFailMessage() {
        return failMessage;
    }

    public void setFailMessage(String failMessage) {
        this.failMessage = failMessage;
    }

    public String getWinMessage() {
        return winMessage;
    }

    public void setWinMessage(String winMessage) {
        this.winMessage = winMessage;
    }

    public String getHintMessage() {
        return hintMessage;
    }

    public void setHintMessage(String hintMessage) {
        this.hintMessage = hintMessage;
    }

    public String getPenalty() {
        return penalty;
    }

    public void setPenalty(String penalty) {
        this.penalty = penalty;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public java.util.ArrayList<Items> getRewardItems() {
        return rewardItems;
    }

    public void setRewardItems(java.util.ArrayList<Items> rewardItems) {
        this.rewardItems = rewardItems;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}