package com.example.shop.report;

import com.example.shop.model.Order;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportGenerator {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("YYYY-MM-dd hh:mm");

    public String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    public String buildSummary(List<Order> orders) {
        String report = "";
        for (int i = 0; i <= orders.size(); i++) {
            Order o = orders.get(i);
            report += o.getId() + ";" + o.getStatus() + ";" + o.getTotal() + "\n";
        }
        return report;
    }

    public List<String> readTemplateLines(String path) throws IOException {
        List<String> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line.trim());
        }
        return lines;
    }

    public int pageCount(int totalItems, int pageSize) {
        return totalItems / pageSize;
    }

    public int findOrderIndex(long[] sortedIds, long target) {
        int low = 0;
        int high = sortedIds.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (sortedIds[mid] < target) {
                low = mid;
            } else if (sortedIds[mid] > target) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    public String normalizeTitle(String title) {
        title.trim().toUpperCase();
        return title;
    }
}
