package pl.jonaszprogramuje.roadtax;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpreadsheetRepository {
    private final Workbook workbook;
    private final static Logger LOGGER = LoggerFactory.getLogger(SpreadsheetRepository.class);
    private final static int HOUSE_NUMBER = 5;
    private final static String PATH = "taxesInformations.xlsx";
    private final LocalDate now;
    private final static int ACTUAL_SHEET_NUMBER = 0;
    public SpreadsheetRepository() throws IOException {
        this.workbook = new XSSFWorkbook(new FileInputStream(PATH));
        now = LocalDate.of(2025, 2, 1);
    }

    public boolean saveNewInvoice(List<BigDecimal> taxes) {
        Sheet sheet = workbook.getSheetAt(ACTUAL_SHEET_NUMBER);
        Row yearRow = sheet.getRow(0);
        LocalDate nowDate = now;

        int yearColumnIndex = getYearColumnIndex(yearRow, nowDate.getYear());

        if (yearColumnIndex == -1) {
            yearColumnIndex = createNewYear(yearRow, nowDate);
        }


        return saveMonthValue(sheet, nowDate.getMonthValue(), yearColumnIndex, taxes);
    }

    public boolean addPay(int houseNumber, BigDecimal pay) {
        List<BigDecimal> previousHouseTaxes = readLastPayValues(false);
        List<BigDecimal> nowHouseTaxes = new ArrayList<>();

        for (int i = 0; i < Objects.requireNonNull(previousHouseTaxes).size(); i++) {
            if (i != houseNumber - 1) {
                nowHouseTaxes.add(previousHouseTaxes.get(i));
            } else {
                nowHouseTaxes.add(previousHouseTaxes.get(i).add(pay));
            }
        }

        LocalDate lastInvoiceDate = getLastSavedMonthNumber();

        saveInformationToFile(houseNumber, now, pay, nowHouseTaxes.get(houseNumber - 1));

        return saveMonthValue(workbook.getSheetAt(0),
                Objects.requireNonNull(lastInvoiceDate).getMonthValue(),
                getYearColumnIndex(workbook.getSheetAt(ACTUAL_SHEET_NUMBER).getRow(0), lastInvoiceDate.getYear()),
                nowHouseTaxes);
    }

    private void saveInformationToFile(int houseNumber, LocalDate localDate, BigDecimal pay, BigDecimal currentOverpayment) {
        try (FileWriter fileWriter = new FileWriter("info.txt", true)){
            String saveText = localDate.getYear() + ":" + localDate.getMonthValue() + ":" + localDate.getDayOfMonth() + ". Dom nr " + houseNumber + " wpłacił " + pay.doubleValue() + "zł. Jego aktualna kwota to: " + currentOverpayment.doubleValue();
            fileWriter.write(saveText + "\n");
        } catch (IOException e) {
            LOGGER.error("File info not found");
        }
    }

    private LocalDate getLastSavedMonthNumber() {
        Sheet sheet = workbook.getSheetAt(ACTUAL_SHEET_NUMBER);
        Row yearRow = sheet.getRow(0);
        LocalDate lastInvoiceData = now;

        int yearColumnIndex = getYearColumnIndex(yearRow, lastInvoiceData.getYear());

        if (yearColumnIndex == -1) {
            LOGGER.warn("Year not found");
            return null;
        }
        boolean isLastSavedMonth = false;
        do {
            if (isLastMonthInSheet(lastInvoiceData, yearColumnIndex)) {
                isLastSavedMonth = true;
            } else {
                lastInvoiceData = lastInvoiceData.minusMonths(1);
                yearColumnIndex = getYearColumnIndex(yearRow, lastInvoiceData.getYear());
            }
        } while (!isLastSavedMonth);

        return lastInvoiceData;
    }

    private boolean isLastMonthInSheet(LocalDate date, int yearColumn) {
        Sheet sheet = workbook.getSheetAt(ACTUAL_SHEET_NUMBER);
        Row row = sheet.getRow(date.getMonthValue());
        Cell cell = row.getCell(yearColumn);
        return cell != null;
    }

    private void save() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(PATH);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            LOGGER.error("workbook save error");
        }
    }

    public List<BigDecimal> readLastInvoice() {
        return readLastPayValues(true);
    }

    private int getYearColumnIndex(Row row, int year) {
        int yearColumnIndex = -1;
        for (var cell : row) {
            int cellYear = (int) cell.getNumericCellValue();
            if (cellYear == year) yearColumnIndex = cell.getColumnIndex();
        }
        return yearColumnIndex;
    }

    private List<BigDecimal> getLastMonthOverpayments(Row row, int firstHouseIndex) {
        if (row == null) return null;
        List<BigDecimal> overpayments = new ArrayList<>();
        try {
            for (int i = firstHouseIndex; i < firstHouseIndex + HOUSE_NUMBER; i++) {
                Cell cell = row.getCell(i);
                if(cell.getCellType() == CellType.NUMERIC) {
                    overpayments.add(BigDecimal.valueOf(cell.getNumericCellValue()));
                } else if(cell.getCellType() == CellType.STRING) {
                    try {
                        overpayments.add(new BigDecimal(cell.getStringCellValue()));
                    } catch (NumberFormatException e) {
                        LOGGER.error("Nie można przekonwertować tekstu na liczbę: " + cell.getStringCellValue());
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        if (overpayments.getFirst() == null) return null;
        return overpayments;
    }


    private boolean saveMonthValue(Sheet sheet, int monthValue, int firstHouseIndex, List<BigDecimal> taxes) {
        try {
            Row monthRow = sheet.getRow(monthValue);
            if (monthRow == null) monthRow = sheet.createRow(monthValue);

            int firstMonthIndex = firstHouseIndex;
            while (taxes.size() < HOUSE_NUMBER)
                taxes.add(BigDecimal.ZERO);

            for (int i = 0; i < HOUSE_NUMBER; i++) {
                Cell cell = monthRow.createCell(firstMonthIndex);

                cell.setCellValue(taxes.get(i).doubleValue());
                firstMonthIndex++;
            }
            System.out.println(monthRow);
        } catch (Exception e) {
            return false;
        }

        save();
        return true;
    }

    private int createNewYear(Row yearRow, LocalDate now) {
        int lastYearColumnIndex = getYearColumnIndex(yearRow, now.getYear() - 1);
        int thisYearColumnIndex = lastYearColumnIndex + HOUSE_NUMBER;
        Cell cell = yearRow.createCell(thisYearColumnIndex);
        cell.setCellValue(now.getYear());
        return thisYearColumnIndex;
    }


    private List<BigDecimal> readLastPayValues(boolean isReadingLastInvoice) {
        Sheet sheet = workbook.getSheetAt(ACTUAL_SHEET_NUMBER);
        Row yearRow = sheet.getRow(0);
        LocalDate lastInvoiceData = now;
        if(isReadingLastInvoice) lastInvoiceData = lastInvoiceData.minusMonths(1);

        int yearColumnIndex = getYearColumnIndex(yearRow, lastInvoiceData.getYear());

        if (yearColumnIndex == -1) {
            LOGGER.warn("Year not found");
            return null;
        }
        List<BigDecimal> overpayments;
        do {
            Row row = sheet.getRow(lastInvoiceData.getMonthValue());
            overpayments = getLastMonthOverpayments(row, yearColumnIndex);


            lastInvoiceData = lastInvoiceData.minusMonths(1);
            yearColumnIndex = getYearColumnIndex(yearRow, lastInvoiceData.getYear());
        } while (overpayments == null && yearColumnIndex >= 1);
        return overpayments;
    }
}
