import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloWorld
    extends Application
{

    public static void main(final String[] args)
    {
        launch(args);
    }

    @Override
    public void start(final Stage stage)
    {
        final Label helloLabel;
        final VBox  root;
        final Scene scene;

        root  = createLayout();
        scene = new Scene(root, 400, 200);

        stage.setScene(scene);
        stage.setTitle("Hello JavaFX");
        stage.show();
    }

    private VBox createLayout()
    {
        Label  label;
        final Button button;

        label  = new Label("Hello World!");
        button = new Button("Click me!");

        button.setOnAction(event -> label.setText("GoodBye Cruel World!"));

        return new VBox(label, button);
    }
}
