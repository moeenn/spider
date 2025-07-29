package com.spider.reporter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class Reporter {
    public abstract String report(ReportArgs args);

    public static void saveReport(String content, String path) throws Exception {
        Path filePath = Paths.get(path);
        Files.writeString(filePath, content);
    }
}
