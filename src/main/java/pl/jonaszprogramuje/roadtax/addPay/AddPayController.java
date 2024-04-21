package pl.jonaszprogramuje.roadtax.addPay;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jonaszprogramuje.roadtax.SpreadsheetRepository;
import pl.jonaszprogramuje.roadtax.result.ResultController;

import java.io.IOException;
import java.math.BigDecimal;


public class AddPayController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ResultController.class);

    @FXML
    private TextField houseNumber;

    @FXML
    private TextField payNumber;



    @FXML
    protected void enterPayAction(){
        try {
            SpreadsheetRepository repository = new SpreadsheetRepository();
            System.out.println(repository.addPay(Integer.parseInt(houseNumber.getText()), BigDecimal.valueOf(Double.parseDouble(payNumber.getText().replace(",", ".")))));

            houseNumber.clear();
            payNumber.clear();
            houseNumber.requestFocus();
        } catch (IOException e) {
            LOGGER.error("create repository error or save new pay error");
            houseNumber.setText("to nie liczba");
        }
    }
}
