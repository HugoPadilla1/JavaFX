package ICs.IC3;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Search {
    @FXML private TextArea searchTA;
    @FXML private TextField searchTF;
    ArrayList<Media> mediaArrayList;

    public void init(ArrayList<Media> mediaArrayList) {
        this.mediaArrayList = mediaArrayList;
    }

    /**
     * Method:searchById()
     * This method looks at each item in the libraryItems array list and if its
     * idNumber attribute matches the input parameter id, that item is returned
     * to the caller. It returns null if item is not found.
     *
     */
    public void searchById()
    {
        Media item = null;
        try {
            for (Media temp : mediaArrayList) {
                if (temp.getIdNumber() == Integer.parseInt(searchTF.getText())) {
                    item = temp;
                }
            }
            if (item == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("No such media found");
                alert.showAndWait();
            }
            else {
                searchTA.setText(item.toString());
            }

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("You must enter an integer for the ID");
        }
    }

    @FXML
    public void exit() {
        Stage stage = (Stage) searchTA.getScene().getWindow();
        stage.close();
    }

}
