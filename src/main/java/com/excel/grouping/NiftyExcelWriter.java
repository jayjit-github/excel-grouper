package com.excel.grouping;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class NiftyExcelWriter {

    // Method to write weekly and monthly data into separate sheets
    public static void writeToExcelSeparateSheets(List<NiftyRecord> weeklyRecords,
                                                  List<NiftyRecord> monthlyRecords,
                                                  String outputPath) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        // Create date cell style
        CreationHelper creationHelper = workbook.getCreationHelper();
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd"));

        // Write weekly data
        Sheet weeklySheet = workbook.createSheet("Weekly Data");
        writeSheet(weeklySheet, weeklyRecords, dateCellStyle);

        // Write monthly data
        Sheet monthlySheet = workbook.createSheet("Monthly Data");
        writeSheet(monthlySheet, monthlyRecords, dateCellStyle);

        // Save the workbook
        try (FileOutputStream fileOut = new FileOutputStream(new File(outputPath))) {
            workbook.write(fileOut);
        } finally {
            workbook.close();
        }
    }

    // Helper method to write data to a sheet
    private static void writeSheet(Sheet sheet, List<NiftyRecord> records, CellStyle dateCellStyle) {
        int rowNum = 0;

        // Header
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("Date");
        headerRow.createCell(1).setCellValue("Price");
        headerRow.createCell(2).setCellValue("Open");
        headerRow.createCell(3).setCellValue("High");
        headerRow.createCell(4).setCellValue("Low");

        // Data rows
        for (NiftyRecord record : records) {
            Row row = sheet.createRow(rowNum++);

            Cell dateCell = row.createCell(0);
            dateCell.setCellValue(record.getDate());
            dateCell.setCellStyle(dateCellStyle);

            row.createCell(1).setCellValue(record.getPrice());
            row.createCell(2).setCellValue(record.getOpen());
            row.createCell(3).setCellValue(record.getHigh());
            row.createCell(4).setCellValue(record.getLow());
        }

        // Auto-size columns
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
