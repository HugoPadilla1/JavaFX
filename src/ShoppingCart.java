import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.awt.*;
public class ShoppingCart extends Application {
    @Override
    public void start(Stage stage) throws Exception{
        BorderPane root = new BorderPane();

        Label idLbl = new Label("Customer ID: ");
        Label nameLbl = new Label("Name: ");
        Label addressLbl = new Label("Address: ");
        Label itemLbl = new Label("Item: ");

        TextField idTf = new TextField();
        TextField nameTf = new TextField();
        TextField addressTf = new TextField();
        TextField itemTf = new TextField();

        HBox idHb = new HBox();
        HBox nameHb = new HBox();
        HBox addressHb = new HBox();
        HBox itemHb = new HBox();

        

        Scene scene = new Scene(root, 300, 250);
        stage.setScene(scene);
        stage.setTitle("Shopping Cart");
        stage.show();
    }
}
