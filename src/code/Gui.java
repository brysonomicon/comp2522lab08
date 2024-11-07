import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Gui
        extends Application
{
    @Override
    public void start(Stage primaryStage) {
        // Create label and button with descriptive layout
        Label label = new Label("This is a good layout example:");
        Button btn = new Button("Click Me");

        // Create an HBox to hold the button and add spacing
        HBox hbox = new HBox(10); // 10 pixels spacing
        hbox.getChildren().add(btn);

        // Create a VBox to stack the label and the HBox vertically
        VBox vbox = new VBox(15); // 15 pixels spacing between elements
        vbox.getChildren().addAll(label, hbox);

        // Set padding and alignment in the VBox (optional, for better appearance)
        vbox.setStyle("-fx-padding: 20;");

        // Create the Scene with the VBox as the root layout
        Scene scene = new Scene(vbox, 300, 200);

        // Configure the Stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Good Practice Layout Example");
        primaryStage.show();
    }

}
