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
    private String requiredItemCode;
    private String requiredItemName;

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

    /**
     * JavaFX/controller-friendly reward handling.
     * Moves this responsibility out of HydraGameController and into Puzzle.
     */
    public java.util.ArrayList<String> grantRewardsToRoom(Room room) {
        java.util.ArrayList<String> messages = new java.util.ArrayList<>();
        if (!solved) {
            messages.add("Puzzle is not solved yet.");
            return messages;
        }
        if (rewardItems == null || rewardItems.isEmpty()) {
            messages.add("No item reward.");
            return messages;
        }
        if (room == null) {
            messages.add("Reward could not be placed because no room was provided.");
            return messages;
        }

        messages.add("Puzzle rewards added to room:");
        for (Items item : new java.util.ArrayList<>(rewardItems)) {
            String code = item.getItemName().toUpperCase().replaceAll("\\s+", "_");
            room.getItems().put(code, item);
            item.setLocation(room);
            messages.add("+ " + item.getItemName());
        }
        rewardItems.clear();
        return messages;
    }

    /**
     * Applies the puzzle penalty to the player when attempts run out.
     */
    public java.util.ArrayList<String> applyPenaltyToPlayer(Player player) {
        java.util.ArrayList<String> messages = new java.util.ArrayList<>();
        if (solved || remainingAttempts > 0) {
            return messages;
        }
        if (penalty == null || penalty.trim().isEmpty() || penalty.equalsIgnoreCase("none")) {
            messages.add("No penalty was applied.");
            return messages;
        }

        String lower = penalty.toLowerCase();
        if (player != null && (lower.contains("health") || lower.contains("hp") || lower.contains("damage"))) {
            int amount = parseNumberFromText(penalty);
            if (amount <= 0) amount = 5;
            player.setHealth(Math.max(0, player.getHealth() - amount));
            messages.add("Penalty applied: -" + amount + " HP.");
        } else {
            messages.add("Penalty: " + penalty);
        }
        return messages;
    }

    /**
     * Some progression puzzles represent keycard readers. These require a
     * specific KeyItem to be in the player's inventory before the answer can
     * be accepted. GameWorld sets these requirements when it loads puzzle data.
     */
    public boolean hasRequiredItem(Player player) {
        if (requiredItemCode == null || requiredItemCode.trim().isEmpty() || requiredItemCode.equalsIgnoreCase("NONE")) {
            return true;
        }
        if (player == null || player.getInventory() == null) {
            return false;
        }
        String expectedName = requiredItemName == null ? "" : requiredItemName.trim();
        for (Items item : player.getInventory()) {
            if (item == null) continue;
            if (!expectedName.isEmpty() && item.getItemName().equalsIgnoreCase(expectedName)) {
                return true;
            }
            if (item instanceof KeyItem) {
                KeyItem key = (KeyItem) item;
                String keyType = key.getKeyType() == null ? "" : key.getKeyType().toUpperCase();
                if (requiredItemCode.equalsIgnoreCase("K00") && keyType.contains("ACCESS_F1")) return true;
                if (requiredItemCode.equalsIgnoreCase("K01") && keyType.contains("ACCESS_F2")) return true;
            }
        }
        return false;
    }

    public String getRequiredItemMessage() {
        if (requiredItemCode == null || requiredItemCode.trim().isEmpty() || requiredItemCode.equalsIgnoreCase("NONE")) {
            return "";
        }
        String itemLabel = requiredItemName == null || requiredItemName.trim().isEmpty()
                ? requiredItemCode
                : requiredItemName + " (" + requiredItemCode + ")";
        return "This puzzle requires " + itemLabel + ". Find the key item first.";
    }

    /**
     * Solves a puzzle and returns displayable messages for the UI/console.
     */
    public java.util.ArrayList<String> submitAnswer(String playerAnswer, Player player, Room room) {
        java.util.ArrayList<String> messages = new java.util.ArrayList<>();
        if (solved) {
            messages.add("This puzzle is already solved.");
            return messages;
        }
        if (remainingAttempts <= 0) {
            messages.add(failMessage);
            messages.addAll(applyPenaltyToPlayer(player));
            return messages;
        }

        if (!hasRequiredItem(player)) {
            messages.add(getRequiredItemMessage());
            messages.add("Access denied. The keycard reader will not accept input yet.");
            return messages;
        }

        boolean wasSolved = solvePuzzle(playerAnswer);
        if (wasSolved) {
            messages.add(winMessage);
            messages.add(successResult);
            messages.addAll(grantRewardsToRoom(room));
        } else {
            messages.add(failMessage);
            messages.add("Attempts remaining: " + remainingAttempts);
            if (remainingAttempts <= 0) {
                messages.addAll(applyPenaltyToPlayer(player));
            }
        }
        return messages;
    }

    private int parseNumberFromText(String text) {
        if (text == null) return 0;
        String digits = text.replaceAll("[^0-9]", "");
        if (digits.isEmpty()) return 0;
        try {
            return Integer.parseInt(digits);
        } catch (Exception e) {
            return 0;
        }
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

    public String getRequiredItemCode() {
        return requiredItemCode;
    }

    public void setRequiredItemCode(String requiredItemCode) {
        this.requiredItemCode = requiredItemCode;
    }

    public String getRequiredItemName() {
        return requiredItemName;
    }

    public void setRequiredItemName(String requiredItemName) {
        this.requiredItemName = requiredItemName;
    }

}
