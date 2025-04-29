package com.excel.grouping;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CSVUtils {
    public static List<NiftyRecord> readCSV(String filePath) {
        List<NiftyRecord> records = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US); // Handles commas

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.replaceAll("\"", "").split(",");

                if (parts.length < 5) continue;

                try {
                    LocalDate date = LocalDate.parse(parts[0], dateFormatter);
                    double price = numberFormat.parse(parts[1]).doubleValue();
                    double open = numberFormat.parse(parts[2]).doubleValue();
                    double high = numberFormat.parse(parts[3]).doubleValue();
                    double low = numberFormat.parse(parts[4]).doubleValue();

                    records.add(new NiftyRecord(date, price, open, high, low));
                } catch (ParseException | NumberFormatException e) {
                    System.err.println("Skipping invalid record: " + line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return records;
    }
}
