package com.excel.grouping;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

public class ExcelUtils {

    public static void writeWeeklyAndMonthlyToOneFile(String outputFilePath,
                                                      Map<String, List<NiftyRecord>> weeklyGroups,
                                                      Map<String, List<NiftyRecord>> monthlyGroups) {
        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet weeklySheet = workbook.createSheet("Weekly");
            Sheet monthlySheet = workbook.createSheet("Monthly");

            writeGroupData(weeklySheet, weeklyGroups);
            writeGroupData(monthlySheet, monthlyGroups);

            try (FileOutputStream fileOut = new FileOutputStream(outputFilePath)) {
                workbook.write(fileOut);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeGroupData(Sheet sheet, Map<String, List<NiftyRecord>> groups) {
        int rowNum = 0;
        Row header = sheet.createRow(rowNum++);
        header.createCell(0).setCellValue("Date");
        header.createCell(1).setCellValue("Price");
        header.createCell(2).setCellValue("Open");
        header.createCell(3).setCellValue("High");
        header.createCell(4).setCellValue("Low");

        for (Map.Entry<String, List<NiftyRecord>> entry : groups.entrySet()) {
            for (NiftyRecord record : entry.getValue()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(record.getDate());
                row.createCell(1).setCellValue(record.getPrice());
                row.createCell(2).setCellValue(record.getOpen());
                row.createCell(3).setCellValue(record.getHigh());
                row.createCell(4).setCellValue(record.getLow());
            }
        }
    }
}
