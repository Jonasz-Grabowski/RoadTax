package pl.jonaszprogramuje.roadtax.result;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class ResultWindow {
    private final Stage stage;
    private final BigDecimal cost;

    public ResultWindow( Stage stage, BigDecimal cost) {
        this.stage = stage;
        this.cost = cost;
    }

    public void open() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ResultWindow.class.getResource("../result-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 300);
        stage.setTitle("Result window");
        stage.setScene(scene);
        stage.show();

        ResultController controller = fxmlLoader.getController();
        controller.showTaxes(cost);
    }
}
