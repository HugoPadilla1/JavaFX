import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    TextField userTF;
    @FXML
    PasswordField passwordPF;
    @FXML
    Label loginLbl;

    @FXML
    public void login() {
        userTF.setText("User");
        passwordPF.setText("Password");
        loginLbl.setText("Login button clicked");
    }

    @FXML
    public void cancel() {
        Stage stage = (Stage) userTF.getScene().getWindow();
        stage.close();
        Platform.exit();
    }
}
