package org.zalando.zester.configuration;

import com.intellij.execution.ExecutionException;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.junit.Before;
import org.junit.Test;
import org.zalando.zester.file.FileFinder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PitJavaParametersTest {

    private static final String TARGET_TESTS_WITH_CLASS = "de.zalando.app.AppTest";
    private static final String TARGET_CLASSES_WITH_CLASS = "de.zalando.app.App";

    private final Project project = mock(Project.class);
    private final FileFinder fileFinder = mock(FileFinder.class);

    private PitJavaParameters pitJavaParameters;

    @Before
    public void before() {
        when(fileFinder.getFileSeparator()).thenReturn("/");
        pitJavaParameters = new PitJavaParameters(
                project,
                TARGET_TESTS_WITH_CLASS,
                TARGET_CLASSES_WITH_CLASS,
                fileFinder);
    }

    @Test
    public void thatSourceDirPathIsGenerated() {
        when(project.getBasePath()).thenReturn("/path/to/project");

        String sourceDirPath = pitJavaParameters.getSourceDirPath();

        assertEquals("/path/to/project", sourceDirPath);
    }

    @Test
    public void thatReportDirPathIsGeneratedForMavenProject() throws ExecutionException {
        VirtualFile baseDir = mock(VirtualFile.class);
        VirtualFile pomFile = mock(VirtualFile.class);

        when(project.getBaseDir()).thenReturn(baseDir);
        when(baseDir.findChild("pom.xml")).thenReturn(pomFile);
        when(baseDir.getPath()).thenReturn("/path/folder");

        String reportDirPath = pitJavaParameters.getReportDirPath();

        assertEquals("/path/folder/target/report/zester", reportDirPath);
    }

    @Test
    public void thatReportDirPathIsGeneratedForGradleProject() throws ExecutionException {
        VirtualFile baseDir = mock(VirtualFile.class);
        VirtualFile pomFile = mock(VirtualFile.class);

        when(project.getBaseDir()).thenReturn(baseDir);
        when(baseDir.findChild("build.gradle")).thenReturn(pomFile);
        when(baseDir.getPath()).thenReturn("/path/folder");

        String reportDirPath = pitJavaParameters.getReportDirPath();

        assertEquals("/path/folder/build/reports/zester", reportDirPath);
    }

    @Test(expected = ExecutionException.class)
    public void thatExceptionIsThrownWhenProjectIsNeitherMavenNorGradle() throws ExecutionException {
        VirtualFile baseDir = mock(VirtualFile.class);

        when(baseDir.findChild("build.gradle")).thenReturn(null);
        when(baseDir.findChild("pom.xml")).thenReturn(null);
        when(project.getBaseDir()).thenReturn(baseDir);

        pitJavaParameters.getReportDirPath();
    }

    @Test
    public void thatTargetTestsAreReturned() {
        assertEquals(TARGET_TESTS_WITH_CLASS, pitJavaParameters.getTargetTests());
    }

    @Test
    public void thatTargetClassesAreReturned() throws ExecutionException {
        when(fileFinder.fileExists("/path/to/module/src/main/java/de/zalando/app/App.java")).thenReturn(true);

        assertEquals(TARGET_CLASSES_WITH_CLASS, pitJavaParameters.getTargetClasses());
    }

    @Test
    public void thatPitMainClassIsReturned() {
        assertEquals("org.pitest.mutationtest.commandline.MutationCoverageReport", pitJavaParameters.getPitMainClass());
    }

    @Test
    public void thatPitJarPathIsReturned() {
        when(fileFinder.getPluginsPath()).thenReturn("/plugins");

        assertEquals("/plugins/zester/lib/pitest-1.1.9.jar", pitJavaParameters.getPitJarPath());
    }

    @Test
    public void thatPitCommandLineJarPathIsReturned() {
        when(fileFinder.getPluginsPath()).thenReturn("/plugins");

        assertEquals("/plugins/zester/lib/pitest-command-line-1.1.9.jar", pitJavaParameters.getPitCommandLineJarPath());
    }

}
