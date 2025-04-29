package com.excel.grouping;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Groups Nifty records into weekly and monthly buckets based on custom logic.
 * Weekly grouping targets Wednesdays, and monthly grouping targets mid-month (15th or next available date).
 *
 * @author Jayjit Mandal
 * @version 1.0
 * @since 2025-04-30
 */
public class NiftyDataGrouper {

    private final List<NiftyRecord> records;

    public NiftyDataGrouper(List<NiftyRecord> records) {
        // Sort records by date ascending for consistency
        this.records = records.stream()
                .sorted(Comparator.comparing(NiftyRecord::getDate))
                .collect(Collectors.toList());
    }

    // Weekly Grouping: From Wednesday (or next available day) to following Tuesday
    public List<NiftyRecord> groupByWeek() {
        List<NiftyRecord> weeklySummaries = new ArrayList<>();
        int i = 0;

        while (i < records.size()) {
            LocalDate baseDate = findNextWednesdayOrLater(i);
            if (baseDate == null) break;

            LocalDate endDate = baseDate.plusDays(6);
            List<NiftyRecord> weekData = filterRecordsBetween(baseDate, endDate);

            if (!weekData.isEmpty()) {
                weeklySummaries.add(summarizeData(weekData, baseDate));
                // Advance to the next base date past the endDate
                i = findIndexAfterDate(endDate);
            } else {
                i++;
            }
        }

        return weeklySummaries;
    }

    // Monthly Grouping: From 15th (or next available date) to 14th of next month
    public List<NiftyRecord> groupByMonth() {
        List<NiftyRecord> monthlySummaries = new ArrayList<>();
        Set<String> processedMonths = new HashSet<>();

        int i = 0;

        while (i < records.size()) {
            LocalDate baseDate = findMidMonthDate(i);
            if (baseDate == null) break;

            String monthKey = baseDate.getYear() + "-" + baseDate.getMonthValue();
            if (processedMonths.contains(monthKey)) {
                i++;
                continue;
            }

            LocalDate endDate = baseDate.plusMonths(1).withDayOfMonth(14);
            List<NiftyRecord> monthData = filterRecordsBetween(baseDate, endDate);

            if (!monthData.isEmpty()) {
                monthlySummaries.add(summarizeData(monthData, baseDate));
                processedMonths.add(monthKey);
                i = findIndexAfterDate(endDate);
            } else {
                i++;
            }
        }

        return monthlySummaries;
    }

    // ========== Helper Methods ==========

    private LocalDate findNextWednesdayOrLater(int startIndex) {
        for (int i = startIndex; i < records.size(); i++) {
            LocalDate date = records.get(i).getDate();
            if (date.getDayOfWeek().getValue() >= DayOfWeek.WEDNESDAY.getValue()) {
                return date;
            }
        }
        return null;
    }

    private LocalDate findMidMonthDate(int startIndex) {
        for (int i = startIndex; i < records.size(); i++) {
            LocalDate date = records.get(i).getDate();
            if (date.getDayOfMonth() >= 15) {
                return date;
            }
        }
        return null;
    }

    private int findIndexAfterDate(LocalDate date) {
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getDate().isAfter(date)) {
                return i;
            }
        }
        return records.size();
    }

    private List<NiftyRecord> filterRecordsBetween(LocalDate start, LocalDate end) {
        return records.stream()
                .filter(r -> !r.getDate().isBefore(start) && !r.getDate().isAfter(end))
                .collect(Collectors.toList());
    }

    private NiftyRecord summarizeData(List<NiftyRecord> group, LocalDate baseDate) {
        NiftyRecord first = group.get(0);
        NiftyRecord last = group.get(group.size() - 1);

        double open = first.getOpen();
        double close = last.getPrice();
        double high = group.stream().mapToDouble(NiftyRecord::getHigh).max().orElse(0);
        double low = group.stream().mapToDouble(NiftyRecord::getLow).min().orElse(0);

        NiftyRecord summary = new NiftyRecord();
        summary.setDate(baseDate);
        summary.setOpen(open);
        summary.setPrice(close);
        summary.setHigh(high);
        summary.setLow(low);
        // Change % can be set outside, based on previous record
        return summary;
    }
}
