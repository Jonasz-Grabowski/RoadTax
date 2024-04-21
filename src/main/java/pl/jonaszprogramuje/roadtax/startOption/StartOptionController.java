package pl.jonaszprogramuje.roadtax.startOption;

import javafx.fxml.FXML;

import java.io.IOException;

public class StartOptionController {
    private StartOptionApplication startOptionApplication;

    @FXML
    protected void onAddInvoice() throws IOException {
        startOptionApplication.openInputDataWindow();
    }

    @FXML
    protected void onAddPay() throws IOException {
        startOptionApplication.openAddPayWindow();
    }

    public void setStartOptionApplication(StartOptionApplication startOptionApplication) {
        this.startOptionApplication = startOptionApplication;
    }
}
