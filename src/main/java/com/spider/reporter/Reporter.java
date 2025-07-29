package com.spider.reporter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class Reporter {
    public abstract String report(ReportArgs args);

    private static String getExtension(String filename) {
        String extension = "";
        int i = filename.lastIndexOf('.');
        if (i > 0) {
            extension = filename.substring(i + 1);
        }

        return extension;
    }

    public static SupportedReportType getReportType(String filename) {
        String extension = getExtension(filename);
        if (extension.equals("")) {
            throw new IllegalArgumentException("Failed to determine report type from report name: " + filename);
        }

        String type = extension.toUpperCase();
        return switch (type) {
            case "CSV" -> SupportedReportType.CSV;
            case "JSON" -> SupportedReportType.JSON;
            default -> throw new IllegalArgumentException("Unsupported report type: " + type);
        };
    }

    public static void saveReport(String content, String path) throws Exception {
        Path filePath = Paths.get(path);
        Files.writeString(filePath, content);
    }
}
