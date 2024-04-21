package pl.jonaszprogramuje.result;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.jonaszprogramuje.roadtax.result.Email;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class EmailTest {
    @Test
    public void getEmailTextTest(){
        BigDecimal bigDecimal = new BigDecimal("173.53");
        Email email = new Email(bigDecimal, bigDecimal.divide(new BigDecimal(6), 2, RoundingMode.HALF_UP), List.of(BigDecimal.TWO, BigDecimal.ONE, new BigDecimal(-10), BigDecimal.ZERO, new BigDecimal(-100)));
        String text = """
                Witam,
                bieżąca faktura została wystawiona na kwotę 173,53 zł, czyli 28,92 zł/Rodzinę.
                Do zapłaty:
                - Asia, Paweł - (nadpłata) 0 zł
                - Kamila, Tomek - (nadpłata) 0 zł
                - Magda, Łukasz - 10,00 zł
                - Ewa, Michał - (nadpłata) 0 zł
                - Ewelina - 100,00 zł

                Pozdrawiam,
                EG

                p.s. Faktura w załączeniu
                """;
    assertEquals(email.getEmailText(), text);
}

}
