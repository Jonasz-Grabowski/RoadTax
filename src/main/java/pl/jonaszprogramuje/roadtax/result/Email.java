package pl.jonaszprogramuje.roadtax.result;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Email {
    private final BigDecimal cost;
    private final BigDecimal costForOneHouse;
    private final List<BigDecimal> taxes;
    private static final String emailBodyTemplate = """
            Witam,
            bieżąca faktura została wystawiona na kwotę %s zł, czyli %s zł/Rodzinę.
            Do zapłaty:
            %s
            Pozdrawiam,
            EG

            p.s. Faktura w załączeniu
            """;

    public Email(BigDecimal cost, BigDecimal costForOneHouse, List<BigDecimal> taxes) {
        this.cost = cost;
        this.costForOneHouse = costForOneHouse;
        this.taxes = taxes;
    }

    public String getEmailText() {
        StringBuilder paymentDetails = new StringBuilder();
        for (int i = 0; i < taxes.size(); i++) {
            paymentDetails.append("- ").append(getRecipientName(i)).append(" - ").append(getEmailCountOfTaxForHouse(taxes.get(i))).append(" zł\n");
        }
        return String.format(emailBodyTemplate, cost.setScale(2, RoundingMode.HALF_UP).toString().replace(".", ","), costForOneHouse.toString().replace(".", ","), paymentDetails);
    }

    private String getRecipientName(int index) {
        return switch (index) {
            case 0 -> "Asia, Paweł";
            case 1 -> "Kamila, Tomek";
            case 2 -> "Magda, Łukasz";
            case 3 -> "Ewa, Michał";
            case 4 -> "Ewelina";
            default -> "";
        };
    }

    private String getEmailCountOfTaxForHouse(BigDecimal tax){
        if(tax.compareTo(BigDecimal.ZERO) < 0) return tax.multiply(new BigDecimal(-1)).setScale(2, RoundingMode.HALF_UP).toString().replace(".", ",");
        else return "(nadpłata) 0";
    }
}

