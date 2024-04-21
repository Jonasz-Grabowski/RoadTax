package pl.jonaszprogramuje.roadtax.inputData;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.jonaszprogramuje.roadtax.result.ResultWindow;

import java.io.IOException;
import java.math.BigDecimal;

public class InputDataApplication {
    private final Stage stage;

    public InputDataApplication(Stage stage) {
        this.stage = stage;
    }

    public void open() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InputDataApplication.class.getResource("../input-data-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        this.stage.setTitle("Input Window");
        this.stage.setScene(scene);
        this.stage.show();

        InputDataController controller = fxmlLoader.getController();
        controller.setInputDataApplication(this);
    }

    public void openResultWindow(BigDecimal cost) throws IOException {
        new ResultWindow(stage, cost).open();
    }

}