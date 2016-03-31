package org.zalando.zester.file;

import com.intellij.openapi.application.PathManager;
import org.zalando.zester.configuration.PitReportException;

import java.io.File;
import java.util.Arrays;

public class FileFinder {

    private final FileProvider fileProvider;

    public FileFinder(FileProvider fileProvider) {
        this.fileProvider = fileProvider;
    }

    public String getLatestReportFileURL(String reportDirPath, String targetClasses) {
        return Arrays.stream(fileProvider.listFiles(reportDirPath))
                .sorted((o1, o2) -> o2.getName().compareTo(o1.getName()))
                .findFirst()
                .map(reportDir -> getReportFileURL(reportDir, targetClasses))
                .orElseThrow(() -> new PitReportException("Latest report not found"));
    }

    private String getReportFileURL(File reportDirectory, String targetClasses) {
        if(targetClasses.endsWith(".*")) {
            return getTargetClassPackageReportFileURL(reportDirectory);
        } else {
            return getTargetClassFileReportFileURL(reportDirectory, targetClasses);
        }
    }

    private String getTargetClassFileReportFileURL(File reportDirectory, String targetClasses) {
        String javaPackage = targetClasses.substring(0, targetClasses.lastIndexOf("."));
        String className = targetClasses.substring(targetClasses.lastIndexOf(".") + 1);

        return String.format("file://%s/%s/%s.java.html",
                reportDirectory.getAbsolutePath(),
                javaPackage,
                className);
    }

    private String getTargetClassPackageReportFileURL(File reportDirectory) {
        return String.format("file://%s/index.html", reportDirectory.getAbsolutePath());
    }

    public String getPluginsPath() {
        return PathManager.getPluginsPath();
    }

    public String getFileSeparator() {
        return System.getProperty("file.separator");
    }

    public boolean fileExists(String path) {
        return fileProvider.fileExists(path);
    }
}
