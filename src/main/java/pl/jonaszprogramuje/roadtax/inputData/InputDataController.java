package pl.jonaszprogramuje.roadtax.inputData;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.math.BigDecimal;

public class InputDataController {
    private InputDataApplication inputDataApplication;

    @FXML
    private TextField cost;

    @FXML
    protected void onCostEntered() {
        try{
            String costString = cost.getText().replace(",", ".");
            BigDecimal costDouble = BigDecimal.valueOf(Double.parseDouble(costString));

            inputDataApplication.openResultWindow(costDouble);
        }catch (Exception e) {

            cost.clear();
            cost.setPromptText("Podana wartość nie jest liczbą, podaj kwotę");
        }
    }

    public void setInputDataApplication(InputDataApplication inputDataApplication){
        this.inputDataApplication = inputDataApplication;
    }
}