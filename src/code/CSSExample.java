import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CSSExample
    extends Application
{
    @Override
    public void start(final Stage stage)
    {
        final Label  label;
        final Button button;
        final VBox   root;
        final Scene  scene;

        label  = new Label("Hello World");
        button = new Button("Click Me");
        root   = new VBox(label, button);
        scene  = new Scene(root);

        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        button.setOnAction(e -> {
            if("-fx-background-color: #4caf50;".equals(button.getStyle()))
            {
                button.setStyle("-fx-background-color: red;");
            }
            else
            {
                button.setStyle("-fx-background-color: #4caf50;");
            }
        });
        stage.setScene(scene);
        stage.setTitle("CSS Example");
        stage.show();
    }
}
