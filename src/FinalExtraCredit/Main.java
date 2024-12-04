package FinalExtraCredit;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class Main extends Application {
    private Map<String, GameCharacter> characterMap = new HashMap<>();
    private ComboBox<String> typeComboBox = new ComboBox<>();
    private TextField nameField = new TextField();
    private TextField hitPointsField = new TextField();
    private TextField armorClassField = new TextField();
    private TextField damageField = new TextField();
    private ComboBox<String> weaponComboBox = new ComboBox<>();
    private ComboBox<String> spellComboBox = new ComboBox<>();
    private TextArea specialAbilitiesArea = new TextArea();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Character Entry");

        // Layout and Input Fields
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Labels and Inputs
        grid.add(new Label("Character Type:"), 0, 0);
        typeComboBox.getItems().addAll(Arrays.stream(CharacterType.values()).map(Enum::name).toList());
        typeComboBox.setOnAction(e -> toggleWeaponOrSpell());
        grid.add(typeComboBox, 1, 0);

        grid.add(new Label("Character Name:"), 0, 1);
        grid.add(nameField, 1, 1);

        grid.add(new Label("Hit Points (0-100):"), 0, 2);
        grid.add(hitPointsField, 1, 2);

        grid.add(new Label("Armor Class (0-10):"), 0, 3);
        grid.add(armorClassField, 1, 3);

        grid.add(new Label("Damage (0-25):"), 0, 4);
        grid.add(damageField, 1, 4);

        grid.add(new Label("Weapon/Spell:"), 0, 5);
        weaponComboBox.getItems().addAll(Arrays.stream(WeaponType.values()).map(Enum::name).toList());
        grid.add(weaponComboBox, 1, 5);

        spellComboBox.getItems().addAll(Arrays.stream(SpellType.values()).map(Enum::name).toList());
        grid.add(spellComboBox, 1, 5);
        spellComboBox.setVisible(false); // Default visibility

        grid.add(new Label("Special Abilities:"), 0, 6);
        specialAbilitiesArea.setPrefRowCount(3);
        grid.add(specialAbilitiesArea, 1, 6);

        // Buttons
        HBox buttonBox = new HBox(10);
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> addCharacter());
        Button saveButton = new Button("Save Characters");
        saveButton.setOnAction(e -> saveCharacters());
        Button readButton = new Button("Read Characters");
        readButton.setOnAction(e -> readCharacters());
        Button displayButton = new Button("Display Characters");
        displayButton.setOnAction(e -> displayCharacters());
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> Platform.exit());
        buttonBox.getChildren().addAll(addButton, displayButton, saveButton, readButton, exitButton);

        // Main Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(grid, buttonBox);

        Scene scene = new Scene(layout, 700, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void toggleWeaponOrSpell() {
        String selectedType = typeComboBox.getValue();
        if ("MAGIC_USER".equals(selectedType)) {
            weaponComboBox.setVisible(false);
            spellComboBox.setVisible(true);
        } else {
            weaponComboBox.setVisible(true);
            spellComboBox.setVisible(false);
        }
    }

    private void addCharacter() {
        try {
            String name = nameField.getText().trim();
            // emptyName validation
            if (name.isEmpty()) throw new IllegalArgumentException("Name cannot be blank.");

            CharacterType type = CharacterType.valueOf(typeComboBox.getValue());

            // hitPoints validation (requires INT)
            int hitPoints;
            try {
                hitPoints = Integer.parseInt(hitPointsField.getText());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Hit points must be an integer between 0 & 100.");
            }
            if (hitPoints < 0 || hitPoints > 100) throw new IllegalArgumentException("Hit points must be between 0 and 100.");

            // armorClass validation (requires INT as well)
            int armorClass;
            try {
                armorClass = Integer.parseInt(armorClassField.getText());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Armor Points must be an integer between 0 & 25.");
            }
            if (armorClass < 0 || armorClass > 10) throw new IllegalArgumentException("Armor class must be between 0 and 10.");

            // damage validation (requires integer)
            int damage;
            try{
                damage = Integer.parseInt(damageField.getText());
            } catch (NumberFormatException e){
                throw new IllegalArgumentException("Damage must be an integer between 0 & 25.");
            }
            if (damage < 0 || damage > 25) throw new IllegalArgumentException("Damage must be between 0 and 25.");

            WeaponType weapon = null;
            SpellType spell = null;
            if ("MAGIC_USER".equals(type.name())) {
                if (spellComboBox.getValue() == null) throw new IllegalArgumentException("You are required to select a spell.");
                spell = SpellType.valueOf(spellComboBox.getValue());
            } else {
                if (weaponComboBox.getValue() == null) throw new IllegalArgumentException("You are required to select a weapon.");
                weapon = WeaponType.valueOf(weaponComboBox.getValue());
            }

            String specialAbility = specialAbilitiesArea.getText().trim();
            GameCharacter character = new GameCharacter(name, type, hitPoints, armorClass, damage, weapon, specialAbility);
            character.setSpell(spell);

            characterMap.put(name, character);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Character added successfully!");
        } catch (IllegalArgumentException | NullPointerException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    private void saveCharacters() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("gameChars.dat"))) {
            oos.writeObject(new ArrayList<>(characterMap.values()));
            // Debug
//            System.out.println("Saving " + characterMap.size() + " characters.");
            showAlert(Alert.AlertType.INFORMATION, "Success", "Characters saved successfully!");
        } catch (IOException e) {
//            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save characters.");
        }
    }

    private List<GameCharacter> readCharacters() {
        List<GameCharacter> characters = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("gameChars.dat"))) {
            characters = (List<GameCharacter>) ois.readObject();
            characterMap.clear();
            for (GameCharacter character : characters) {
                characterMap.put(character.getName(), character);
            }
            showAlert(Alert.AlertType.INFORMATION, "Success", "Characters loaded successfully!");
        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to read characters.");
        }
        return characters;
    }

    private void displayCharacters() {
        StringBuilder displayText = new StringBuilder("Characters:\n");
        Iterator<GameCharacter> iterator = characterMap.values().iterator();
        while (iterator.hasNext()) {
            displayText.append(iterator.next()).append("\n\n");
        }
        showAlert(Alert.AlertType.INFORMATION, "Character List", displayText.toString());
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
