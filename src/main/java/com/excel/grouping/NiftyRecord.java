package com.excel.grouping;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents a single record of Nifty data with attributes like date, price, open, high, and low.
 * Acts as a data transfer object (DTO) for Nifty records.
 * 
 * This class is typically populated from CSV input.
 * 
 * @author Jayjit Mandal
 * @version 1.0
 * @since 2025-04-30
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NiftyRecord {
    private LocalDate date;  // Date of the record
    private double price;    // Closing price
    private double open;     // Opening price
    private double high;     // Highest price in the period
    private double low;      // Lowest price in the period
    private double changePercentage; // Change percentage (weekly/monthly)

    // Lombok will automatically generate getters, setters, and constructors for the fields.

    // You can also add additional constructors if needed (e.g., without changePercentage)
    public NiftyRecord(LocalDate date, double price, double open, double high, double low) {
        this.date = date;
        this.price = price;
        this.open = open;
        this.high = high;
        this.low = low;
    }
}
