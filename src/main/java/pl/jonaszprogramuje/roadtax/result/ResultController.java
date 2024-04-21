package pl.jonaszprogramuje.roadtax.result;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.jonaszprogramuje.roadtax.SpreadsheetRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ResultController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ResultController.class);

    @FXML
    private TextArea emailBodyBox;

    public void initialize() {
        emailBodyBox.setEditable(false);
    }

    public void showTaxes(BigDecimal cost) {


        SpreadsheetRepository repository;
        BigDecimal costForOneHouse = cost.divide(new BigDecimal(6), 2, RoundingMode.HALF_UP);
        try {
            repository = new SpreadsheetRepository();
        } catch (IOException e) {
            LOGGER.error("Error with creating database");
            emailBodyBox.setText("Błąd w tworzeniu otwieraniu pliku excela");
            return;
        }


        List<BigDecimal> lastInvoice = repository.readLastInvoice();

        List<BigDecimal> taxes = new ArrayList<>();
        for (BigDecimal lastCost : lastInvoice) {
            taxes.add(lastCost.subtract(costForOneHouse));
        }

        if (cost.doubleValue() != 0) {
            System.out.println(repository.saveNewInvoice(taxes));
        }

        Email email = new Email(cost, costForOneHouse, taxes);
        emailBodyBox.setText(email.getEmailText());
    }
}
