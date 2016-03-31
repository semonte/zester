package org.zalando.zester.project;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProjectDetectorTest {

    @Test
    public void thatMavenProjectIsDetected() {
        Project project = getProjectWithFileInBaseDir("pom.xml");

        assertTrue(ProjectDetector.isMavenProject(project));
    }

    @Test
    public void thatMavenProjectIsNotDetected() {
        Project project = getProjectWithFileInBaseDir("file.xml");

        assertFalse(ProjectDetector.isMavenProject(project));
    }

    @Test
    public void thatGradleProjectIsDetected() {
        Project project = getProjectWithFileInBaseDir("build.gradle");

        assertTrue(ProjectDetector.isGradleProject(project));
    }

    @Test
    public void thatGradleProjectIsNotDetected() {
        Project project = getProjectWithFileInBaseDir("file.gradle");

        assertFalse(ProjectDetector.isGradleProject(project));
    }

    private Project getProjectWithFileInBaseDir(String fileName) {
        Project project = mock(Project.class);
        VirtualFile baseDir = mock(VirtualFile.class);
        VirtualFile pomFile = mock(VirtualFile.class);

        when(project.getBaseDir()).thenReturn(baseDir);
        when(baseDir.findChild(fileName)).thenReturn(pomFile);
        return project;
    }

}
