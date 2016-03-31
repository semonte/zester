package org.zalando.zester.file;

import org.zalando.zester.configuration.PitReportException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileFinderTest {

    private FileProvider fileProvider;
    private FileFinder fileFinder;

    @Before
    public void before() {
        fileProvider = mock(FileProvider.class);
        fileFinder = new FileFinder(fileProvider);
    }

    @Test
    public void thatLatestReportDirectoryURLIsGeneratedWhenTargetClassIsFile() {
        File oldReport = mock(File.class);
        File newReport = mock(File.class);

        when(oldReport.getName()).thenReturn("201503140902");
        when(newReport.getName()).thenReturn("201603140902");
        when(newReport.getAbsolutePath()).thenReturn("/path/to/reports/201603140902");
        when(fileProvider.listFiles("/path/to/reports")).thenReturn(new File[]{oldReport, newReport});

        String latestReportDirectoryURL = fileFinder.getLatestReportFileURL("/path/to/reports", "de.zalando.app.App");

        assertEquals("file:///path/to/reports/201603140902/de.zalando.app/App.java.html", latestReportDirectoryURL);
    }

    @Test
    public void thatLatestReportDirectoryURLIsGeneratedWhenTargetClassIsPackage() {
        File oldReport = mock(File.class);
        File newReport = mock(File.class);

        when(oldReport.getName()).thenReturn("201503140902");
        when(newReport.getName()).thenReturn("201603140902");
        when(newReport.getAbsolutePath()).thenReturn("/path/to/reports/201603140902");
        when(fileProvider.listFiles("/path/to/reports")).thenReturn(new File[]{oldReport, newReport});

        String latestReportDirectoryURL = fileFinder.getLatestReportFileURL("/path/to/reports", "de.zalando.app.*");

        assertEquals("file:///path/to/reports/201603140902/index.html", latestReportDirectoryURL);
    }

    @Test(expected = PitReportException.class)
    public void thatExceptionIsThrownWhenReportDirectoryIsNotFound() {
        when(fileProvider.listFiles("/path/to/reports")).thenReturn(new File[]{});

        fileFinder.getLatestReportFileURL("/path/to/reports", "de.zalando.app.AppTest");
    }

}
