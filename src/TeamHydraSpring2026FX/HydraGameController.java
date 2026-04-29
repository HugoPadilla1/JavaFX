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
    private static final String ROOMS_PATH = "Rooms.txt";
    private static final String ITEM_DATA_PATH = "itemData";
    private static final String ITEM_LOCATION_PATH = "itemLocationData";
    private static final String MONSTERS_PATH = "Data/basedata/Monsters.txt";
    private static final String MONSTER_ATTACK_PATH = "Data/basedata/MonsterAttackData.txt";
    private static final String PUZZLES_PATH = "puzzles.txt";
    private static final String PUZZLE_LOCATION_PATH = "puzzleLocationData";
    private static final String PUZZLE_OUTCOMES_PATH = "puzzles_outcomes.txt";
    private static final String PUZZLE_REWARDS_PATH = "puzzleRewardItems";
    private static final String SAVE_SLOT_1 = "hydra_save_slot_1.txt";
    private static final String SAVE_SLOT_2 = "hydra_save_slot_2.txt";

    private GameWorld world;
    private GameMap gameMap;
    private Player player;
    private Room currentRoom;
    private Combat activeCombat;
    private Puzzle activePuzzle;

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
    private Button upButton;
    private Button downButton;
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
            world = createNewWorld();
            gameMap = world.getGameMap();
            player = new Player();
            currentRoom = gameMap.getRoom("R02");
            if (currentRoom == null) currentRoom = gameMap.getRoom("R01");
            if (currentRoom == null) throw new IOException("No starting room R02 or R01 found.");

            currentRoom.setVisited(true);
            player.setLocation(currentRoom);
            activeCombat = null;
            activePuzzle = null;

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

    private GameWorld createNewWorld() throws IOException {
        return new GameWorld(
                ROOMS_PATH,
                ITEM_DATA_PATH,
                ITEM_LOCATION_PATH,
                MONSTERS_PATH,
                MONSTER_ATTACK_PATH,
                PUZZLES_PATH,
                PUZZLE_LOCATION_PATH,
                PUZZLE_OUTCOMES_PATH,
                PUZZLE_REWARDS_PATH
        );
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
        nav.setPrefWidth(190);

        GridPane compass = new GridPane();
        compass.setHgap(6);
        compass.setVgap(6);

        northButton = actionButton("N");
        eastButton = actionButton("E");
        southButton = actionButton("S");
        westButton = actionButton("W");
        upButton = actionButton("↑");
        downButton = actionButton("↓");

        northButton.setOnAction(e -> move("N"));
        eastButton.setOnAction(e -> move("E"));
        southButton.setOnAction(e -> move("S"));
        westButton.setOnAction(e -> move("W"));
        upButton.setOnAction(e -> move("U"));
        downButton.setOnAction(e -> move("D"));

        VBox verticalMoveBox = new VBox(3, upButton, downButton);
        verticalMoveBox.setStyle("-fx-alignment: center;");

        compass.add(northButton, 1, 0);
        compass.add(westButton, 0, 1);
        compass.add(verticalMoveBox, 1, 1);
        compass.add(eastButton, 2, 1);
        compass.add(southButton, 1, 2);

        Button lookButton = wideButton("Look");
        lookButton.setOnAction(e -> appendRoomEntry());

        Button exploreButton = wideButton("Explore");
        exploreButton.setOnAction(e -> exploreRoom());

        Button restButton = wideButton("Rest");
        restButton.setOnAction(e -> rest());

        Button tutorialButton = wideButton("Tutorial");
        tutorialButton.setOnAction(e -> displayTutorial());

        Button newGameButton = wideButton("New Game");
        newGameButton.setOnAction(e -> startNewGame());

        Label saveLabel = new Label("Save / Load");
        saveLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Button saveSlot1Button = wideButton("Save Slot 1");
        Button loadSlot1Button = wideButton("Load Slot 1");
        Button saveSlot2Button = wideButton("Save Slot 2");
        Button loadSlot2Button = wideButton("Load Slot 2");

        saveSlot1Button.setOnAction(e -> saveGame(1));
        loadSlot1Button.setOnAction(e -> loadGame(1));
        saveSlot2Button.setOnAction(e -> saveGame(2));
        loadSlot2Button.setOnAction(e -> loadGame(2));

        GridPane saveGrid = new GridPane();
        saveGrid.setHgap(5);
        saveGrid.setVgap(5);
        saveGrid.add(saveSlot1Button, 0, 0);
        saveGrid.add(loadSlot1Button, 1, 0);
        saveGrid.add(saveSlot2Button, 0, 1);
        saveGrid.add(loadSlot2Button, 1, 1);

        nav.getChildren().addAll(
                compass,
                lookButton,
                exploreButton,
                restButton,
                tutorialButton,
                new Separator(),
                newGameButton,
                saveLabel,
                saveGrid
        );
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

        Button useButton = wideButton("Use");
        useButton.setOnAction(e -> useSelectedItem());

        Button equipButton = wideButton("Equip");
        equipButton.setOnAction(e -> equipSelectedItem());

        Button dropButton = wideButton("Drop");
        dropButton.setOnAction(e -> dropSelectedItem());

        Button examineButton = wideButton("Examine");
        examineButton.setOnAction(e -> examineSelectedItem());

        GridPane itemActionGrid = new GridPane();
        itemActionGrid.setHgap(6);
        itemActionGrid.setVgap(6);
        itemActionGrid.add(useButton, 0, 0);
        itemActionGrid.add(equipButton, 1, 0);
        itemActionGrid.add(dropButton, 0, 1);
        itemActionGrid.add(examineButton, 1, 1);

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
                section("Inventory", inventoryList, itemActionGrid),
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

        String blockReason = world.getVerticalExitBlockReason(player, currentRoom, direction);
        if (!blockReason.isEmpty()) {
            append(blockReason);
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
        activeCombat = null;
        activePuzzle = null;
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
        if (currentRoom.hasExit("U")) {
            String reason = world.getVerticalExitBlockReason(player, currentRoom, "U");
            append("Up stairs: " + (reason.isEmpty() ? "unlocked" : reason));
        }
        if (currentRoom.hasExit("D")) {
            String reason = world.getVerticalExitBlockReason(player, currentRoom, "D");
            append("Down stairs: " + (reason.isEmpty() ? "unlocked" : reason));
        }
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
            if (activeCombat != null && activeCombat.isActive()) {
                appendAll(activeCombat.playerUseConsumable(item));
                handlePostCombatState();
            } else {
                player.useItem(item, player.getInventory());
                append("Used " + item.getItemName() + ".");
            }
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
            append(player.equipItem(item, player.getInventory()));
        } else {
            append("That item cannot be equipped.");
        }
        refreshAll();
    }

    private void examineSelectedItem() {
        Items item = selectedInventoryItem();
        if (item == null) {
            append("Select an inventory item first.");
            return;
        }
        append("\n-- Examine Item --");
        append(player.examineItem(item));
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

        Monsters enemy = currentRoom.getMonsters().get(code);
        activeCombat = world.createCombat();
        appendAll(activeCombat.start(player, enemy, currentRoom));
        append("Use Attack, Defend, Flee, or a consumable from Inventory > Use.");
        refreshAll();
    }

    private void playerAttack() {
        if (!ensureActiveCombat()) return;
        appendAll(activeCombat.playerAttack());
        handlePostCombatState();
    }

    private void playerDefend() {
        if (!ensureActiveCombat()) return;
        appendAll(activeCombat.playerDefend());
        handlePostCombatState();
    }

    private void fleeCombat() {
        if (!ensureActiveCombat()) return;
        appendAll(activeCombat.tryFlee());
        handlePostCombatState();
    }

    private void handlePostCombatState() {
        if (player.getHealth() <= 0) {
            disableGameControls();
        }
        if (activeCombat != null && !activeCombat.isActive()) {
            activeCombat = null;
        }
        refreshAll();
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
        String answer = puzzleAnswerField.getText();
        puzzleAnswerField.clear();
        appendAll(activePuzzle.submitAnswer(answer, player, currentRoom));
        refreshAll();
    }

    private void showPuzzleHint() {
        if (activePuzzle == null) {
            append("Open a puzzle first.");
            return;
        }
        append("Hint: " + activePuzzle.getHintMessage());
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

    private void saveGame(int slot) {
        String path = slot == 1 ? SAVE_SLOT_1 : SAVE_SLOT_2;
        append(GameSaveManager.save(world, player, currentRoom, path));
    }

    private void loadGame(int slot) {
        String path = slot == 1 ? SAVE_SLOT_1 : SAVE_SLOT_2;
        try {
            world = createNewWorld();
            gameMap = world.getGameMap();
            player = new Player();
            currentRoom = GameSaveManager.load(world, player, path);
            activeCombat = null;
            activePuzzle = null;
            clearOutput();
            append("Loaded save slot " + slot + ".");
            appendRoomEntry();
            refreshAll();
        } catch (Exception e) {
            append("Load failed: " + e.getMessage());
        }
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
            case "U":
            case "D":
                move(command);
                break;
            case "MOVE":
                if (!argument.isEmpty()) move(argument.substring(0, 1));
                else append("Usage: MOVE N/E/S/W/U/D");
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
            case "EXAMINE":
            case "INSPECT":
                examineByCode(argument);
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
            case "SAVE":
                if (argument.equals("2")) saveGame(2);
                else saveGame(1);
                break;
            case "LOAD":
                if (argument.equals("2")) loadGame(2);
                else loadGame(1);
                break;
            case "TUTORIAL":
                displayTutorial();
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

    private void examineByCode(String code) {
        inventoryList.getSelectionModel().select(findListRow(inventoryList, code));
        examineSelectedItem();
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

    private void displayTutorial() {
        append("\n========== TUTORIAL ==========");
        append("Objective: Escape the infected hospital by exploring rooms, collecting supplies, solving puzzles, and surviving combat.");
        append("Movement: Use the compass buttons for N/E/S/W. In lobby/stair rooms, the center up/down buttons appear, but they stay locked until the correct keycard puzzle has been solved.");
        append("Items: Select an item in the room list and press Grab. Inventory actions are arranged Use / Equip / Drop / Examine. Examine shows item details without changing game state.");
        append("Equipment: Equipped weapons move into the weapon slot and are removed from visible inventory. Replacing a weapon returns the old weapon to inventory and prevents stat stacking.");
        append("Puzzles: Keycard readers are progression locks: K01 solves puzzle 7 for second-floor traversal, and K00 solves puzzle 8 for first-floor traversal.");
        append("Combat: Select a monster and press Fight. Attack, Defend, Flee, or use a consumable from inventory during battle.");
        append("Saving: Use Save Slot 1/2 to preserve your current room, stats, inventory, solved puzzles, room items, and remaining monsters.");
        append("Console box: You can also type commands like STATUS, INVENTORY, SAVE 1, LOAD 2, TUTORIAL, or HELP.");
    }

    private void displayHelp() {
        append("\nCommands: N/E/S/W/U/D, MOVE, LOOK, EXPLORE, ITEMS, MONSTERS, PUZZLES, GRAB itemID, DROP itemID,");
        append("INVENTORY, USE itemID, EQUIP itemID, DROP itemID, EXAMINE itemID, FIGHT monsterID, ATTACK, DEFEND, FLEE, PUZZLE puzzleID,");
        append("STATUS, REST, SAVE 1/2, LOAD 1/2, TUTORIAL, CATALOG, GIVE itemID, WORLD, HELP.");
    }

    private void appendRoomEntry() {
        append("\n=== " + currentRoom.getRoomName() + " ===");
        append(currentRoom.getRoomDescription());
        if (currentRoom.isSafeRoom()) append("[Safe room: REST is available.]");
        append("Room ID: " + currentRoom.getRoomID());
        append("Available exits: " + currentRoom.getExits().keySet());
        if (currentRoom.hasExit("U") || currentRoom.hasExit("D")) {
            if (currentRoom.hasExit("U")) {
                String reason = world.getVerticalExitBlockReason(player, currentRoom, "U");
                append("Up stairs: " + (reason.isEmpty() ? "unlocked" : "locked - " + reason));
            }
            if (currentRoom.hasExit("D")) {
                String reason = world.getVerticalExitBlockReason(player, currentRoom, "D");
                append("Down stairs: " + (reason.isEmpty() ? "unlocked" : "locked - " + reason));
            }
        }
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

    private boolean ensureActiveCombat() {
        if (activeCombat == null || !activeCombat.isActive()) {
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

        boolean monsterBlocksExit = !currentRoom.getMonsters().isEmpty();
        northButton.setDisable(!currentRoom.hasExit("N") || monsterBlocksExit);
        eastButton.setDisable(!currentRoom.hasExit("E") || monsterBlocksExit);
        southButton.setDisable(!currentRoom.hasExit("S") || monsterBlocksExit);
        westButton.setDisable(!currentRoom.hasExit("W") || monsterBlocksExit);

        boolean hasUp = currentRoom.hasExit("U");
        boolean hasDown = currentRoom.hasExit("D");
        upButton.setVisible(hasUp);
        upButton.setManaged(hasUp);
        downButton.setVisible(hasDown);
        downButton.setManaged(hasDown);
        upButton.setDisable(!hasUp || monsterBlocksExit || !world.canUseVerticalExit(player, currentRoom, "U"));
        downButton.setDisable(!hasDown || monsterBlocksExit || !world.canUseVerticalExit(player, currentRoom, "D"));

        roomItemsList.setItems(FXCollections.observableArrayList(roomItemRows()));
        inventoryList.setItems(FXCollections.observableArrayList(inventoryRows()));
        monstersList.setItems(FXCollections.observableArrayList(monsterRows()));
        puzzlesList.setItems(FXCollections.observableArrayList(puzzleRows()));

        combatLabel.setText(activeCombat == null || activeCombat.getEnemy() == null ? "Combat: none" : "Combat: " + activeCombat.getEnemy().getMonsterName() + " HP " + activeCombat.getEnemy().getHealth());
        boolean inCombat = activeCombat != null && activeCombat.isActive() && player.getHealth() > 0;
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
        upButton.setDisable(true);
        downButton.setDisable(true);
        attackButton.setDisable(true);
        defendButton.setDisable(true);
        fleeButton.setDisable(true);
        submitPuzzleButton.setDisable(true);
    }

    private void appendAll(java.util.List<String> messages) {
        if (messages == null) return;
        for (String message : messages) {
            append(message);
        }
    }

    private void clearOutput() {
        outputArea.clear();
    }

    private void append(String text) {
        outputArea.appendText(text + "\n");
        outputArea.setScrollTop(Double.MAX_VALUE);
    }
}
