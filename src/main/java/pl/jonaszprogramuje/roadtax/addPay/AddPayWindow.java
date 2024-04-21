package pl.jonaszprogramuje.roadtax.addPay;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class AddPayWindow {
    private final Stage stage;

    public AddPayWindow(Stage stage) {
        this.stage = stage;

    }

    public void open() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AddPayWindow.class.getResource("../add-pay-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 300);
        stage.setTitle("Add pay window");
        stage.setScene(scene);
        stage.show();
    }
}
