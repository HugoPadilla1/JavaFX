package TeamHydraSpring2026FX;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * JavaFX controller for the Team Hydra game.
 * <p>
 * This controller intentionally uses the existing game model classes:
 * GameWorld, GameMap, Room, Player, Items, Monsters, Puzzle, Attack.
 * No FXML is required; the UI is built programmatically so it is easy to drop into the current project.
 */
public class HydraGameController {
    private GameWorld world;
    private GameMap gameMap;
    private Player player;
    private Room currentRoom;
    private Monsters activeMonster;
    private Puzzle activePuzzle;
    private boolean defending;

    private TextArea outputArea;
    private Label roomTitleLabel;
    private Label roomIdLabel;
    private Label hpLabel;
    private Label damageLabel;
    private Label defenseLabel;
    private Label speedLabel;
    private Label weaponLabel;
    private Label inventoryCapLabel;
    private Label combatLabel;
    private Label puzzleLabel;

    private ListView<String> roomItemsList;
    private ListView<String> inventoryList;
    private ListView<String> monstersList;
    private ListView<String> puzzlesList;
    private TextField commandField;
    private TextField puzzleAnswerField;

    private Button northButton;
    private Button eastButton;
    private Button southButton;
    private Button westButton;
    private Button attackButton;
    private Button defendButton;
    private Button fleeButton;
    private Button submitPuzzleButton;
    private Button hintButton;

    public Parent createView() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(12));
        root.setStyle("-fx-background-color: #15171c;");

        root.setTop(createHeader());
        root.setCenter(createCenterPane());
        root.setLeft(createNavigationPane());
        root.setRight(createActionPane());
        root.setBottom(createCommandPane());

        return root;
    }

    public void startNewGame() {
        try {
            world = new GameWorld(
                    "Rooms.txt",
                    "itemData",
                    "itemLocationData",
                    "Data/basedata/Monsters.txt",
                    "Data/basedata/MonsterAttackData.txt",
                    "puzzles.txt",
                    "puzzleLocationData",
                    "puzzles_outcomes.txt",
                    "puzzleRewardItems"
            );
            gameMap = world.getGameMap();
            player = new Player();
            currentRoom = gameMap.getRoom("R02");
            if (currentRoom == null) currentRoom = gameMap.getRoom("R01");
            if (currentRoom == null) throw new IOException("No starting room R02 or R01 found.");

            currentRoom.setVisited(true);
            player.setLocation(currentRoom);
            activeMonster = null;
            activePuzzle = null;
            defending = false;

            clearOutput();
            append("========== TEAM HYDRA ==========");
            append("JavaFX interface initialized. Use movement buttons, room lists, and action buttons to play.");
            append("Tip: EXPLORE refreshes room details. Debug command GIVE itemID still works from the command box.");
            appendRoomEntry();
            refreshAll();
        } catch (IOException e) {
            append("ERROR loading game world: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Parent createHeader() {
        VBox box = new VBox(6);
        box.setPadding(new Insets(0, 0, 10, 0));

        Label title = new Label("Team Hydra - Hospital Escape");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        roomTitleLabel = new Label("Room");
        roomTitleLabel.setStyle("-fx-text-fill: #d7e3ff; -fx-font-size: 18px; -fx-font-weight: bold;");

        roomIdLabel = new Label("Room ID");
        roomIdLabel.setStyle("-fx-text-fill: #9aa3b2;");

        box.getChildren().addAll(title, roomTitleLabel, roomIdLabel);
        return box;
    }

    private Parent createCenterPane() {
        VBox center = new VBox(10);
        center.setPadding(new Insets(0, 12, 0, 12));

        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);
        outputArea.setFont(Font.font("Consolas", 14));
        outputArea.setStyle("-fx-control-inner-background: #0f1117; -fx-text-fill: #f1f5f9; -fx-highlight-fill: #334155;");
        VBox.setVgrow(outputArea, Priority.ALWAYS);

        TitledPane statusPane = new TitledPane("Player Status", createStatusGrid());
        statusPane.setCollapsible(false);

        center.getChildren().addAll(outputArea, statusPane);
        return center;
    }

    private Parent createStatusGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(18);
        grid.setVgap(6);
        grid.setPadding(new Insets(8));

        hpLabel = statusValueLabel();
        damageLabel = statusValueLabel();
        defenseLabel = statusValueLabel();
        speedLabel = statusValueLabel();
        weaponLabel = statusValueLabel();
        inventoryCapLabel = statusValueLabel();
        hpLabel.setStyle("-fx-text-fill: black;");
        damageLabel.setStyle("-fx-text-fill: black;");
        defenseLabel.setStyle("-fx-text-fill: black;");
        speedLabel.setStyle("-fx-text-fill: black;");
        weaponLabel.setStyle("-fx-text-fill: black;");
        inventoryCapLabel.setStyle("-fx-text-fill: black;");

        grid.add(label("HP:"), 0, 0);
        grid.add(hpLabel, 1, 0);
        grid.add(label("Damage:"), 2, 0);
        grid.add(damageLabel, 3, 0);
        grid.add(label("Defense:"), 0, 1);
        grid.add(defenseLabel, 1, 1);
        grid.add(label("Speed:"), 2, 1);
        grid.add(speedLabel, 3, 1);
        grid.add(label("Weapon:"), 0, 2);
        grid.add(weaponLabel, 1, 2);
        grid.add(label("Inventory:"), 2, 2);
        grid.add(inventoryCapLabel, 3, 2);

        return grid;
    }

    private Parent createNavigationPane() {
        VBox nav = panelBox("Movement");
        nav.setPrefWidth(170);

        GridPane compass = new GridPane();
        compass.setHgap(6);
        compass.setVgap(6);

        northButton = actionButton("N");
        eastButton = actionButton("E");
        southButton = actionButton("S");
        westButton = actionButton("W");

        northButton.setOnAction(e -> move("N"));
        eastButton.setOnAction(e -> move("E"));
        southButton.setOnAction(e -> move("S"));
        westButton.setOnAction(e -> move("W"));

        compass.add(northButton, 1, 0);
        compass.add(westButton, 0, 1);
        compass.add(eastButton, 2, 1);
        compass.add(southButton, 1, 2);

        Button lookButton = wideButton("Look");
        lookButton.setOnAction(e -> appendRoomEntry());

        Button exploreButton = wideButton("Explore");
        exploreButton.setOnAction(e -> exploreRoom());

        Button restButton = wideButton("Rest");
        restButton.setOnAction(e -> rest());

        Button newGameButton = wideButton("New Game");
        newGameButton.setOnAction(e -> startNewGame());

        nav.getChildren().addAll(compass, lookButton, exploreButton, restButton, new Separator(), newGameButton);
        return nav;
    }

    private Parent createActionPane() {
        VBox right = panelBox("Actions");
        right.setPrefWidth(330);

        roomItemsList = new ListView<>();
        inventoryList = new ListView<>();
        monstersList = new ListView<>();
        puzzlesList = new ListView<>();

        roomItemsList.setPrefHeight(105);
        inventoryList.setPrefHeight(125);
        monstersList.setPrefHeight(90);
        puzzlesList.setPrefHeight(90);

        Button grabButton = wideButton("Grab Selected Room Item");
        grabButton.setOnAction(e -> grabSelectedItem());

        Button dropButton = wideButton("Drop Selected Inventory Item");
        dropButton.setOnAction(e -> dropSelectedItem());

        Button useButton = wideButton("Use Selected Inventory Item");
        useButton.setOnAction(e -> useSelectedItem());

        Button equipButton = wideButton("Equip Selected Inventory Item");
        equipButton.setOnAction(e -> equipSelectedItem());

        Button startCombatButton = wideButton("Fight Selected Monster");
        startCombatButton.setOnAction(e -> startCombat());

        combatLabel = new Label("Combat: none");
        combatLabel.setStyle("-fx-text-fill: #f8fafc; -fx-font-weight: bold;");

        HBox combatButtons = new HBox(6);
        attackButton = smallButton("Attack");
        defendButton = smallButton("Defend");
        fleeButton = smallButton("Flee");
        attackButton.setOnAction(e -> playerAttack());
        defendButton.setOnAction(e -> playerDefend());
        fleeButton.setOnAction(e -> fleeCombat());
        combatButtons.getChildren().addAll(attackButton, defendButton, fleeButton);

        Button openPuzzleButton = wideButton("Open Selected Puzzle");
        openPuzzleButton.setOnAction(e -> openSelectedPuzzle());

        puzzleLabel = new Label("Puzzle: none");
        puzzleLabel.setWrapText(true);
        puzzleLabel.setStyle("-fx-text-fill: #f8fafc; -fx-font-weight: bold;");

        puzzleAnswerField = new TextField();
        puzzleAnswerField.setPromptText("Puzzle answer");

        submitPuzzleButton = smallButton("Submit");
        submitPuzzleButton.setOnAction(e -> submitPuzzleAnswer());

        hintButton = smallButton("Hint");
        hintButton.setOnAction(e -> showPuzzleHint());

        HBox puzzleButtons = new HBox(6, submitPuzzleButton, hintButton);

        right.getChildren().addAll(
                section("Room Items", roomItemsList, grabButton),
                section("Inventory", inventoryList, new HBox(6, useButton, equipButton), dropButton),
                section("Monsters", monstersList, startCombatButton, combatLabel, combatButtons),
                section("Puzzles", puzzlesList, openPuzzleButton, puzzleLabel, puzzleAnswerField, puzzleButtons)
        );
        return right;
    }

    private Parent createCommandPane() {
        HBox bar = new HBox(8);
        bar.setPadding(new Insets(10, 0, 0, 0));

        commandField = new TextField();
        commandField.setPromptText("Optional command box: EXPLORE, STATUS, GIVE I00, CATALOG, WORLD, HELP...");
        HBox.setHgrow(commandField, Priority.ALWAYS);
        commandField.setOnAction(e -> runCommand());

        Button runButton = new Button("Run Command");
        runButton.setOnAction(e -> runCommand());

        bar.getChildren().addAll(commandField, runButton);
        return bar;
    }

    private VBox section(String title, javafx.scene.Node... nodes) {
        Label header = new Label(title);
        header.setStyle("-fx-text-fill: #d7e3ff; -fx-font-weight: bold;");
        VBox box = new VBox(5);
        box.getChildren().add(header);
        box.getChildren().addAll(nodes);
        return box;
    }

    private VBox panelBox(String title) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: #1e2430; -fx-background-radius: 10;");
        Label label = new Label(title);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        box.getChildren().add(label);
        return box;
    }

    private Label label(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #9aa3b2; -fx-font-weight: bold;");
        return label;
    }

    private Label statusValueLabel() {
        Label label = new Label("-");
        label.setStyle("-fx-text-fill: #f8fafc;");
        return label;
    }

    private Button actionButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(48);
        button.setPrefHeight(42);
        return button;
    }

    private Button wideButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    private Button smallButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    private void move(String direction) {
        if (currentRoom == null) return;
        direction = direction.toUpperCase();

        if (!currentRoom.hasExit(direction)) {
            append("You cannot go " + direction + " from here.");
            return;
        }

        if (!currentRoom.getMonsters().isEmpty()) {
            append("A monster blocks your escape. Defeat it first or try Flee during combat.");
            return;
        }

        Room nextRoom = gameMap.getRoom(currentRoom.getExit(direction));
        if (nextRoom == null) {
            append("That exit points to a missing room.");
            return;
        }

        currentRoom = nextRoom;
        currentRoom.setVisited(true);
        player.setLocation(currentRoom);
        activeMonster = null;
        activePuzzle = null;
        defending = false;
        appendRoomEntry();
        refreshAll();

        if (!currentRoom.getMonsters().isEmpty()) {
            append("Danger: A monster is here. Select it and press Fight.");
        }
    }

    private void exploreRoom() {
        append("\n-- Exits --");
        if (currentRoom.getDoorDescriptions().isEmpty()) {
            append("No visible exits.");
        } else {
            for (Map.Entry<String, String> entry : currentRoom.getDoorDescriptions().entrySet()) {
                String dir = entry.getKey();
                String destinationID = currentRoom.getExit(dir);
                Room dest = gameMap.getRoom(destinationID);
                String extra = dest != null && dest.isVisited() ? " Leads to: " + dest.getRoomName() : "";
                append("[" + dir + "] " + entry.getValue() + extra);
            }
        }
        append("Items: " + (currentRoom.getItems().isEmpty() ? "none" : currentRoom.getItems().keySet()));
        append("Puzzles: " + (currentRoom.getPuzzles().isEmpty() ? "none" : currentRoom.getPuzzles().keySet()));
        append("Monsters: " + (currentRoom.getMonsters().isEmpty() ? "none" : currentRoom.getMonsters().keySet()));
        refreshAll();
    }

    private void grabSelectedItem() {
        String code = selectedCode(roomItemsList);
        if (code == null) {
            append("Select a room item first.");
            return;
        }
        Items item = currentRoom.getItems().remove(code);
        if (item == null) {
            append("That item is no longer in this room.");
            refreshAll();
            return;
        }
        if (player.getInventory().size() >= player.getInventorySpace()) {
            currentRoom.getItems().put(code, item);
            append("Inventory full.");
            return;
        }
        player.getInventory().add(item);
        append("Picked up " + code + " - " + item.getItemName() + ".");
        refreshAll();
    }

    private void dropSelectedItem() {
        Items item = selectedInventoryItem();
        if (item == null) {
            append("Select an inventory item first.");
            return;
        }
        if (!item.getDroppable()) {
            append("You cannot drop " + item.getItemName() + ".");
            return;
        }
        String code = safeItemCode(item);
        player.getInventory().remove(item);
        currentRoom.getItems().put(code, item);
        append("Dropped " + code + " - " + item.getItemName() + ".");
        refreshAll();
    }

    private void useSelectedItem() {
        Items item = selectedInventoryItem();
        if (item == null) {
            append("Select an inventory item first.");
            return;
        }

        if (item instanceof Consumable) {
            player.useItem(item, player.getInventory());
            append("Used " + item.getItemName() + ".");
        } else if (item instanceof KeyItem) {
            KeyItem key = (KeyItem) item;
            key.itemEquipEffect(player);
            append("Activated " + item.getItemName() + ".");
            if (key.getKeyType() != null && key.getKeyType().equalsIgnoreCase("INV_CAP_20")) {
                player.setInventorySpace(20);
                append("Inventory capacity increased to 20.");
            }
        } else {
            append("That item is not usable. Try Equip.");
        }
        refreshAll();
    }

    private void equipSelectedItem() {
        Items item = selectedInventoryItem();
        if (item == null) {
            append("Select an inventory item first.");
            return;
        }
        if (item instanceof Weapon || item instanceof Wearable) {
            player.equipItem(item, player.getInventory());
            append("Equipped " + item.getItemName() + ".");
        } else {
            append("That item cannot be equipped.");
        }
        refreshAll();
    }

    private void startCombat() {
        String code = selectedCode(monstersList);
        if (code == null && !currentRoom.getMonsters().isEmpty()) {
            code = currentRoom.getMonsters().keySet().iterator().next();
        }
        if (code == null) {
            append("No monster in this room.");
            return;
        }
        activeMonster = currentRoom.getMonsters().get(code);
        defending = false;
        append("\n========== COMBAT: " + activeMonster.getMonsterName() + " ==========");
        append("Use Attack, Defend, Flee, or a consumable from Inventory > Use.");
        refreshAll();
    }

    private void playerAttack() {
        if (!ensureActiveMonster()) return;
        int damage = Math.max(0, player.attack() - activeMonster.getDefense());
        activeMonster.setHealth(Math.max(0, activeMonster.getHealth() - damage));
        append("You strike " + activeMonster.getMonsterName() + " for " + damage + " damage.");
        afterPlayerCombatAction();
    }

    private void playerDefend() {
        if (!ensureActiveMonster()) return;
        defending = true;
        append("You brace for impact. Incoming attack damage is reduced.");
        afterPlayerCombatAction();
    }

    private void fleeCombat() {
        if (!ensureActiveMonster()) return;
        int chance = 50 + player.getSpeed() - activeMonster.getSpeed();
        if ((int) (Math.random() * 100) < chance) {
            append("You escaped combat, but the monster still blocks the room exit.");
            activeMonster = null;
            defending = false;
        } else {
            append("You failed to escape.");
            monsterTurn();
        }
        refreshAll();
    }

    private void afterPlayerCombatAction() {
        if (activeMonster.getHealth() <= 0) {
            append("Defeated: " + activeMonster.getMonsterName());
            giveMonsterDrops(activeMonster);
            removeMonsterFromCurrentRoom(activeMonster);
            activeMonster = null;
            defending = false;
            refreshAll();
            return;
        }
        monsterTurn();
        defending = false;
        if (player.getHealth() <= 0) {
            append("\nYou have died. The plague spreads...");
            disableGameControls();
        }
        refreshAll();
    }

    private void monsterTurn() {
        if (activeMonster == null) return;
        Attack attack = activeMonster.spinMonsterAttack();
        if (attack.getFlavorText() != null && !attack.getFlavorText().isEmpty()) {
            append(attack.getFlavorText());
        } else {
            append(activeMonster.getMonsterName() + " uses " + attack.getAttackName() + ".");
        }

        if (attack.getStatusEffect().equalsIgnoreCase("heal")) {
            int healed = attack.heal(activeMonster);
            append(activeMonster.getMonsterName() + " heals " + healed + " HP.");
            return;
        }
        if (attack.getStatusEffect().equalsIgnoreCase("defense")) {
            int boosted = attack.addDefense(activeMonster);
            append(activeMonster.getMonsterName() + " raises defense by " + boosted + ".");
            return;
        }

        int damage = attack.calculateDamage(activeMonster, player);
        if (defending) damage /= 2;
        player.setHealth(Math.max(0, player.getHealth() - damage));
        append(activeMonster.getMonsterName() + " deals " + damage + " damage.");
    }

    private void giveMonsterDrops(Monsters enemy) {
        if (enemy.getMonsterInventory() == null || enemy.getMonsterInventory().isEmpty()) {
            append("No item drops.");
            return;
        }
        append("Drops added to the room:");
        for (Items item : enemy.getMonsterInventory()) {
            String code = safeItemCode(item);
            currentRoom.getItems().put(code, item);
            append("+ " + code + " - " + item.getItemName());
        }
    }

    private void removeMonsterFromCurrentRoom(Monsters enemy) {
        String removeKey = null;
        for (Map.Entry<String, Monsters> entry : currentRoom.getMonsters().entrySet()) {
            if (entry.getValue() == enemy) {
                removeKey = entry.getKey();
                break;
            }
        }
        if (removeKey != null) currentRoom.getMonsters().remove(removeKey);
    }

    private void openSelectedPuzzle() {
        String code = selectedCode(puzzlesList);
        if (code == null && !currentRoom.getPuzzles().isEmpty()) {
            code = currentRoom.getPuzzles().keySet().iterator().next();
        }
        if (code == null) {
            append("No puzzle in this room.");
            return;
        }
        activePuzzle = currentRoom.getPuzzles().get(code);
        activePuzzle.setCurrentRoom(currentRoom);
        activePuzzle.setPlayer(player);
        append("\n========== PUZZLE " + code + " ==========");
        append(activePuzzle.getDescription());
        append("Attempts remaining: " + activePuzzle.getRemainingAttempts());
        puzzleAnswerField.requestFocus();
        refreshAll();
    }

    private void submitPuzzleAnswer() {
        if (activePuzzle == null) {
            append("Open a puzzle first.");
            return;
        }
        if (activePuzzle.isSolved()) {
            append("This puzzle is already solved.");
            return;
        }
        String answer = puzzleAnswerField.getText();
        boolean solved = activePuzzle.solvePuzzle(answer);
        puzzleAnswerField.clear();

        if (solved) {
            append(activePuzzle.getWinMessage());
            append(activePuzzle.getSuccessResult());
            grantPuzzleRewards(activePuzzle);
        } else {
            append(activePuzzle.getFailMessage());
            append("Attempts remaining: " + activePuzzle.getRemainingAttempts());
            if (activePuzzle.getRemainingAttempts() <= 0) {
                applyPuzzlePenalty(activePuzzle);
            }
        }
        refreshAll();
    }

    private void showPuzzleHint() {
        if (activePuzzle == null) {
            append("Open a puzzle first.");
            return;
        }
        append("Hint: " + activePuzzle.getHintMessage());
    }

    private void grantPuzzleRewards(Puzzle puzzle) {
        if (puzzle.getRewardItems() == null || puzzle.getRewardItems().isEmpty()) {
            append("No item reward.");
            return;
        }
        append("Puzzle rewards added to room:");
        for (Items item : new ArrayList<>(puzzle.getRewardItems())) {
            String code = safeItemCode(item);
            currentRoom.getItems().put(code, item);
            append("+ " + code + " - " + item.getItemName());
        }
        puzzle.getRewardItems().clear();
    }

    private void applyPuzzlePenalty(Puzzle puzzle) {
        String penalty = puzzle.getPenalty();
        if (penalty == null || penalty.trim().isEmpty() || penalty.equalsIgnoreCase("none")) {
            append("No penalty was applied.");
            return;
        }
        String lower = penalty.toLowerCase();
        if (lower.contains("health") || lower.contains("hp") || lower.contains("damage")) {
            int amount = Math.max(5, parseNumericID(penalty));
            player.setHealth(Math.max(0, player.getHealth() - amount));
            append("Penalty applied: -" + amount + " HP.");
        } else {
            append("Penalty: " + penalty);
        }
    }

    private void rest() {
        if (currentRoom.isSafeRoom()) {
            player.rest(currentRoom);
            append("You rested in the safe room.");
        } else {
            append("You can only fully rest in a safe room.");
        }
        refreshAll();
    }

    private void runCommand() {
        String input = commandField.getText() == null ? "" : commandField.getText().trim();
        commandField.clear();
        if (input.isEmpty()) return;
        append("\n> " + input);

        String[] parts = input.split("\\s+", 2);
        String command = parts[0].toUpperCase();
        String argument = parts.length > 1 ? parts[1].trim() : "";

        switch (command) {
            case "N":
            case "E":
            case "S":
            case "W":
                move(command);
                break;
            case "MOVE":
                if (!argument.isEmpty()) move(argument.substring(0, 1));
                else append("Usage: MOVE N/E/S/W");
                break;
            case "LOOK":
            case "ROOM":
                appendRoomEntry();
                break;
            case "EXPLORE":
                exploreRoom();
                break;
            case "ITEMS":
                appendList("Room Items", roomItemsList.getItems());
                break;
            case "MONSTERS":
                appendList("Monsters", monstersList.getItems());
                break;
            case "PUZZLES":
                appendList("Puzzles", puzzlesList.getItems());
                break;
            case "INV":
            case "INVENTORY":
                appendList("Inventory", inventoryList.getItems());
                break;
            case "STATUS":
                appendStatus();
                break;
            case "REST":
                rest();
                break;
            case "GRAB":
                grabByCode(argument);
                break;
            case "DROP":
                dropByCode(argument);
                break;
            case "USE":
                useByCode(argument);
                break;
            case "EQUIP":
                equipByCode(argument);
                break;
            case "FIGHT":
                selectMonsterByCode(argument);
                startCombat();
                break;
            case "PUZZLE":
            case "SOLVE":
                selectPuzzleByCode(argument);
                openSelectedPuzzle();
                break;
            case "ATTACK":
                playerAttack();
                break;
            case "DEFEND":
                playerDefend();
                break;
            case "FLEE":
                fleeCombat();
                break;
            case "GIVE":
                giveItem(argument);
                break;
            case "CATALOG":
                displayCatalog();
                break;
            case "WORLD":
                displayWorldSummary();
                break;
            case "HELP":
                displayHelp();
                break;
            default:
                append("Unknown command. Try HELP.");
                break;
        }
        refreshAll();
    }

    private void grabByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            append("Usage: GRAB itemID");
            return;
        }
        roomItemsList.getSelectionModel().select(findListRow(roomItemsList, code));
        grabSelectedItem();
    }

    private void dropByCode(String code) {
        inventoryList.getSelectionModel().select(findListRow(inventoryList, code));
        dropSelectedItem();
    }

    private void useByCode(String code) {
        inventoryList.getSelectionModel().select(findListRow(inventoryList, code));
        useSelectedItem();
    }

    private void equipByCode(String code) {
        inventoryList.getSelectionModel().select(findListRow(inventoryList, code));
        equipSelectedItem();
    }

    private void selectMonsterByCode(String code) {
        if (code != null && !code.trim().isEmpty())
            monstersList.getSelectionModel().select(findListRow(monstersList, code));
    }

    private void selectPuzzleByCode(String code) {
        if (code != null && !code.trim().isEmpty())
            puzzlesList.getSelectionModel().select(findListRow(puzzlesList, code));
    }

    private void giveItem(String itemCode) {
        if (itemCode == null || itemCode.trim().isEmpty()) {
            append("Usage: GIVE itemID");
            return;
        }
        Items item = world.getCatalogItem(itemCode.trim().toUpperCase());
        if (item == null) {
            append("No item exists with that ID.");
            return;
        }
        player.getInventory().add(item);
        append("Debug added: " + itemCode.toUpperCase() + " - " + item.getItemName());
    }

    private void displayCatalog() {
        append("\n-- Loaded Item Catalog --");
        for (Map.Entry<String, Items> entry : world.getItemCatalog().entrySet()) {
            append(entry.getKey() + " - " + formatItem(entry.getValue()));
        }
    }

    private void displayWorldSummary() {
        append("\n-- World Summary --");
        append("Items loaded: " + world.getItemCatalog().size());
        append("Attacks loaded: " + world.getAttackCatalog().size());
        append("Monsters loaded: " + world.getMonsterCatalog().size());
        append("Puzzles loaded: " + world.getPuzzleCatalog().size());
        append("Monster drop groups: " + world.getMonsterDrops().size());
        append("Puzzle reward groups: " + world.getPuzzleRewards().size());
    }

    private void displayHelp() {
        append("\nCommands: N/E/S/W, MOVE, LOOK, EXPLORE, ITEMS, MONSTERS, PUZZLES, GRAB itemID, DROP itemID,");
        append("INVENTORY, USE itemID, EQUIP itemID, FIGHT monsterID, ATTACK, DEFEND, FLEE, PUZZLE puzzleID,");
        append("STATUS, REST, CATALOG, GIVE itemID, WORLD, HELP.");
    }

    private void appendRoomEntry() {
        append("\n=== " + currentRoom.getRoomName() + " ===");
        append(currentRoom.getRoomDescription());
        if (currentRoom.isSafeRoom()) append("[Safe room: REST is available.]");
        append("Room ID: " + currentRoom.getRoomID());
        append("Available exits: " + currentRoom.getExits().keySet());
        if (!currentRoom.getItems().isEmpty()) append("Items here: " + currentRoom.getItems().keySet());
        if (!currentRoom.getPuzzles().isEmpty()) append("Puzzles here: " + currentRoom.getPuzzles().keySet());
        if (!currentRoom.getMonsters().isEmpty()) append("Monsters here: " + currentRoom.getMonsters().keySet());
    }

    private void appendStatus() {
        append("\n-- Player Status --");
        append("Health: " + player.getHealth());
        append("Damage: " + player.getDamage());
        append("Defense: " + player.getDefense());
        append("Speed: " + player.getSpeed());
        append("Weapon: " + (player.getWeapon() == null || player.getWeapon().isEmpty() ? "None" : player.getWeapon()));
        append("Inventory: " + player.getInventory().size() + "/" + player.getInventorySpace());
    }

    private void appendList(String title, Iterable<String> rows) {
        append("\n-- " + title + " --");
        boolean any = false;
        for (String row : rows) {
            append(row);
            any = true;
        }
        if (!any) append("None.");
    }

    private boolean ensureActiveMonster() {
        if (activeMonster == null) {
            append("Select a monster and press Fight first.");
            return false;
        }
        if (player.getHealth() <= 0) {
            append("You are unable to fight.");
            return false;
        }
        return true;
    }

    private Items selectedInventoryItem() {
        String row = inventoryList.getSelectionModel().getSelectedItem();
        if (row == null) return null;
        String code = extractCode(row);
        for (Items item : player.getInventory()) {
            String itemCode = safeItemCode(item);
            if (itemCode.equalsIgnoreCase(code) || item.getItemName().equalsIgnoreCase(code)) return item;
        }
        return null;
    }

    private String selectedCode(ListView<String> list) {
        String row = list.getSelectionModel().getSelectedItem();
        return row == null ? null : extractCode(row);
    }

    private int findListRow(ListView<String> list, String code) {
        if (code == null) return -1;
        String clean = code.trim().toUpperCase();
        for (int i = 0; i < list.getItems().size(); i++) {
            String row = list.getItems().get(i);
            if (extractCode(row).equalsIgnoreCase(clean) || row.toUpperCase().contains(clean)) return i;
        }
        return -1;
    }

    private String extractCode(String row) {
        if (row == null) return "";
        int idx = row.indexOf(" - ");
        return idx >= 0 ? row.substring(0, idx).trim().toUpperCase() : row.trim().toUpperCase();
    }

    private String safeItemCode(Items item) {
        String code = world.getItemCode(item);
        if (code == null || code.isEmpty()) code = item.getItemName().toUpperCase().replaceAll("\\s+", "_");
        return code;
    }

    private String formatItem(Items item) {
        if (item == null) return "null";
        return item.getItemName() + " | " + item.getClass().getSimpleName() + " | " + item.getItemDescription();
    }

    private int parseNumericID(String text) {
        if (text == null) return 0;
        String digits = text.replaceAll("[^0-9]", "");
        return digits.isEmpty() ? 0 : Integer.parseInt(digits);
    }

    private void refreshAll() {
        if (currentRoom == null || player == null) return;
        roomTitleLabel.setText(currentRoom.getRoomName());
        roomIdLabel.setText(currentRoom.getRoomID() + " | Exits: " + currentRoom.getExits().keySet());

        hpLabel.setText(String.valueOf(player.getHealth()));
        damageLabel.setText(String.valueOf(player.getDamage()));
        defenseLabel.setText(String.valueOf(player.getDefense()));
        speedLabel.setText(String.valueOf(player.getSpeed()));
        weaponLabel.setText(player.getWeapon() == null || player.getWeapon().isEmpty() ? "None" : player.getWeapon());
        inventoryCapLabel.setText(player.getInventory().size() + "/" + player.getInventorySpace());

        northButton.setDisable(!currentRoom.hasExit("N") || !currentRoom.getMonsters().isEmpty());
        eastButton.setDisable(!currentRoom.hasExit("E") || !currentRoom.getMonsters().isEmpty());
        southButton.setDisable(!currentRoom.hasExit("S") || !currentRoom.getMonsters().isEmpty());
        westButton.setDisable(!currentRoom.hasExit("W") || !currentRoom.getMonsters().isEmpty());

        roomItemsList.setItems(FXCollections.observableArrayList(roomItemRows()));
        inventoryList.setItems(FXCollections.observableArrayList(inventoryRows()));
        monstersList.setItems(FXCollections.observableArrayList(monsterRows()));
        puzzlesList.setItems(FXCollections.observableArrayList(puzzleRows()));

        combatLabel.setText(activeMonster == null ? "Combat: none" : "Combat: " + activeMonster.getMonsterName() + " HP " + activeMonster.getHealth());
        boolean inCombat = activeMonster != null && activeMonster.getHealth() > 0 && player.getHealth() > 0;
        attackButton.setDisable(!inCombat);
        defendButton.setDisable(!inCombat);
        fleeButton.setDisable(!inCombat);

        puzzleLabel.setText(activePuzzle == null ? "Puzzle: none" : "Puzzle: " + activePuzzle.getDescription() + " | Attempts " + activePuzzle.getRemainingAttempts() + (activePuzzle.isSolved() ? " | SOLVED" : ""));
        boolean puzzleActive = activePuzzle != null && !activePuzzle.isSolved() && activePuzzle.getRemainingAttempts() > 0;
        submitPuzzleButton.setDisable(!puzzleActive);
        hintButton.setDisable(activePuzzle == null);
    }

    private ArrayList<String> roomItemRows() {
        ArrayList<String> rows = new ArrayList<>();
        for (Map.Entry<String, Items> entry : currentRoom.getItems().entrySet()) {
            rows.add(entry.getKey() + " - " + entry.getValue().getItemName());
        }
        return rows;
    }

    private ArrayList<String> inventoryRows() {
        ArrayList<String> rows = new ArrayList<>();
        for (Items item : player.getInventory()) {
            rows.add(safeItemCode(item) + " - " + item.getItemName());
        }
        return rows;
    }

    private ArrayList<String> monsterRows() {
        ArrayList<String> rows = new ArrayList<>();
        for (Map.Entry<String, Monsters> entry : currentRoom.getMonsters().entrySet()) {
            Monsters monster = entry.getValue();
            rows.add(entry.getKey() + " - " + monster.getMonsterName() + " HP:" + monster.getHealth());
        }
        return rows;
    }

    private ArrayList<String> puzzleRows() {
        ArrayList<String> rows = new ArrayList<>();
        for (Map.Entry<String, Puzzle> entry : currentRoom.getPuzzles().entrySet()) {
            Puzzle puzzle = entry.getValue();
            rows.add(entry.getKey() + " - " + (puzzle.isSolved() ? "[SOLVED] " : "") + puzzle.getDescription());
        }
        return rows;
    }

    private void disableGameControls() {
        northButton.setDisable(true);
        eastButton.setDisable(true);
        southButton.setDisable(true);
        westButton.setDisable(true);
        attackButton.setDisable(true);
        defendButton.setDisable(true);
        fleeButton.setDisable(true);
        submitPuzzleButton.setDisable(true);
    }

    private void clearOutput() {
        outputArea.clear();
    }

    private void append(String text) {
        outputArea.appendText(text + "\n");
        outputArea.setScrollTop(Double.MAX_VALUE);
    }
}
