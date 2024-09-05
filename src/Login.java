    import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.io.File;


public class Login extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Login");
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(25,25, 25, 25));
        Text sceneTitleTXT = new Text("Welcome");
        sceneTitleTXT.setFont(new Font(24));
        sceneTitleTXT.setId("welcome-text");
        pane.add(sceneTitleTXT, 0, 0, 2, 1);

        Label userNameLbl = new Label("User Name");
        pane.add(userNameLbl, 0, 1);

        TextField userNameTF = new TextField();
        pane.add(userNameTF, 1, 1);

        Label pwLbl = new Label("Password");
        pane.add(pwLbl, 0, 2);

        PasswordField pwFld = new PasswordField();
        pane.add(pwFld, 1, 2);

        Button loginBtn = new Button("Login");
        loginBtn.setOnAction(event -> {
            User user = new User();
            try {
                user.setName(userNameTF.getText());
                user.setPassword(pwFld.getText());
                userNameTF.setText("User Name");
                pwFld.setText("Password");
            }
            catch(UserException ue) {
                Alert alert = new Alert(Alert.AlertType.ERROR, ue.getMessage());
                alert.showAndWait();
            }
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(event -> {
            stage.close();
            Platform.exit();
        });

        HBox btnHb = new HBox(10);
        btnHb.setAlignment(Pos.BOTTOM_RIGHT);
        btnHb.getChildren().addAll(loginBtn,cancelBtn);

        pane.add(btnHb, 1,3);


        Scene scene = new Scene(pane, 300, 275);
//		scene.getStylesheets().add(getClass().getResource("login.css").toExternalForm());
        File cssFile = new File("src/Examples/JavaFX/login.css");
        //If you do not have spaces in your path
//        scene.getStylesheets().clear();
//        scene.getStylesheets().add("file:///" + cssFile.getAbsolutePath().replace("\\", "/"));

        //If you have spaces in your path. This will also work without spaces
        String path = cssFile.getAbsolutePath();
        path.replace("\\", "/");
        path = "file:" + path.replaceAll(" ", "%20");
        scene.getStylesheets().add(path);

        BackgroundImage bi = new BackgroundImage(new Image("examples/JavaFX/background.jpg"), null, null,null,null);
        pane.setBackground(new Background(bi));
        stage.setScene(scene);
        stage.show();
    }

        public static void main(String[] args) {
        launch(args);
    }
}
