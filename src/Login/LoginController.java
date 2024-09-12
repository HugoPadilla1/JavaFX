package Login;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField nameTF;
    @FXML private PasswordField passwordPF;
    @FXML private Label resultLbl;

    @FXML
    public void login() {
        nameTF.setText("Login");
        passwordPF.setText("Password");
        resultLbl.setText("Login Successful");
    }

    @FXML
    public void cancel() {
        Stage stage = (Stage) nameTF.getScene().getWindow();
        stage.close();
        Platform.exit();
    }
}
