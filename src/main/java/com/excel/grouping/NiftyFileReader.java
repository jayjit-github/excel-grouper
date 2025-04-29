package com.excel.grouping;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NiftyFileReader {

    public static List<NiftyRecord> read(String filePath) throws CsvValidationException, NumberFormatException {
        List<NiftyRecord> records = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] parts;
            boolean isFirstLine = true;

            while ((parts = csvReader.readNext()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip header
                    continue;
                }

                if (parts.length < 5) continue;

                // Parse date with correct format
                LocalDate date = LocalDate.parse(parts[0].trim(), formatter);

                // Parse numeric fields and handle commas
                double price = Double.parseDouble(parts[1].replace(",", "").trim());
                double open = Double.parseDouble(parts[2].replace(",", "").trim());
                double high = Double.parseDouble(parts[3].replace(",", "").trim());
                double low = Double.parseDouble(parts[4].replace(",", "").trim());

                records.add(new NiftyRecord(date, price, open, high, low));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return records;
    }
}
