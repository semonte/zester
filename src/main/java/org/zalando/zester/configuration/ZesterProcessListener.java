package org.zalando.zester.configuration;

import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.ide.browsers.OpenUrlHyperlinkInfo;
import org.zalando.zester.file.FileFinder;

public class ZesterProcessListener extends ProcessAdapter {

    private final ConsoleView consoleView;
    private final String reportDirPath;
    private final FileFinder fileFinder;
    private final String targetClasses;

    public ZesterProcessListener(ConsoleView consoleView,
                                 String reportDirPath,
                                 FileFinder fileFinder,
                                 String targetClasses) {
        this.consoleView = consoleView;
        this.reportDirPath = reportDirPath;
        this.fileFinder = fileFinder;
        this.targetClasses = targetClasses;
    }

    @Override
    public void processTerminated(ProcessEvent event) {
        super.processTerminated(event);
        consoleView.print("\n", ConsoleViewContentType.NORMAL_OUTPUT);
        consoleView.printHyperlink("Open mutation test report in browser",
                new OpenUrlHyperlinkInfo(fileFinder.getLatestReportFileURL(reportDirPath, targetClasses)));
        consoleView.print("\n", ConsoleViewContentType.NORMAL_OUTPUT);
    }

}
