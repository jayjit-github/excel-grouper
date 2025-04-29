package com.excel.grouping;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.opencsv.exceptions.CsvValidationException;

public class NiftyGroupingMain {

    public static void main(String[] args) throws Exception, NumberFormatException {
        // Default file paths
        String inputFilePath = "";
        boolean mergeToOneFile = false;

        // Case 1: Running from command-line
        if (args.length >= 1) {
            inputFilePath = args[0];
            if (args.length > 1 && args[1].equalsIgnoreCase("--merge")) {
                mergeToOneFile = true;
            }
        }
        // Case 2: Running by directly providing input (for testing)
        else {
            inputFilePath = "/Users/jayjitmandal/codebase/excel-grouper/history-data.csv"; // Change default for testing
            mergeToOneFile = false;
        }

        try {
            // Reading data from input file
            List<NiftyRecord> records = NiftyFileReader.read(inputFilePath);
            System.out.println("Total records read: " + records.size());

            // Grouping records by weekly and monthly intervals
            NiftyDataGrouper dataGrouper = new NiftyDataGrouper(records);
            List<NiftyRecord> weeklyGroups = dataGrouper.groupByWeek();
            List<NiftyRecord> monthlyGroups = dataGrouper.groupByMonth();

            // Printing the number of weekly and monthly groups
            System.out.println("Weekly groups: " + weeklyGroups.size());
            System.out.println("Monthly groups: " + monthlyGroups.size());

            // Writing grouped data to separate sheets in Excel file
            String outputFilePath = "output/grouped_nifty_data_weekly_monthly_separate.xlsx";
            NiftyExcelWriter.writeToExcelSeparateSheets(weeklyGroups, monthlyGroups, outputFilePath);
            System.out.println("Data written to separate sheets in: " + outputFilePath);

        } catch (IOException e) {
            System.err.println("Error writing to Excel file: " + e.getMessage());
        }
    }
}
