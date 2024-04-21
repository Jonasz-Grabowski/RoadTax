package pl.jonaszprogramuje.roadtax.startOption;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.jonaszprogramuje.roadtax.addPay.AddPayController;
import pl.jonaszprogramuje.roadtax.addPay.AddPayWindow;
import pl.jonaszprogramuje.roadtax.inputData.InputDataApplication;

import java.io.IOException;

public class StartOptionApplication extends Application {
    private Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(pl.jonaszprogramuje.roadtax.startOption.StartOptionApplication.class.getResource("../start-option-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        this.stage.setTitle("Choose option");
        this.stage.setScene(scene);
        this.stage.show();

        StartOptionController controller = fxmlLoader.getController();
        controller.setStartOptionApplication(this);
    }

    public void openInputDataWindow() throws IOException {
        new InputDataApplication(stage).open();
    }

    public void openAddPayWindow() throws IOException {
        new AddPayWindow(stage).open();
    }

    public static void main(String[] args) {
        launch();
    }
}
