package org.zalando.zester.configuration;

import com.intellij.execution.ExecutionException;
import com.intellij.openapi.project.Project;
import org.zalando.zester.file.FileFinder;
import org.zalando.zester.project.ProjectDetector;

public class PitJavaParameters {

    public static final String UNIT_TEST_SUFFIX = "Test";

    private static final String PLUGIN_NAME = "zester";
    private static final String LIB_DIR = "lib";

    private static final String PIT_JAR = "pitest-1.1.9.jar";
    private static final String PIT_COMMAND_LINE_JAR = "pitest-command-line-1.1.9.jar";
    private static final String PIT_MAIN_CLASS = "org.pitest.mutationtest.commandline.MutationCoverageReport";

    private static final String MAVEN_REPORT_PATH = "target/report/zester";
    private static final String GRADLE_REPORT_PATH = "build/reports/zester";

    private final Project project;
    private final String testClassQualifiedName;
    private final String targetClasses;
    private final FileFinder fileFinder;

    PitJavaParameters(Project project,
                      String testClassQualifiedName,
                      String targetClasses,
                      FileFinder fileFinder) {
        this.project = project;
        this.testClassQualifiedName = testClassQualifiedName;
        this.targetClasses = targetClasses;
        this.fileFinder = fileFinder;
    }

    String getSourceDirPath() {
        return project.getBasePath();
    }

    String getReportDirPath() throws ExecutionException {
        if (ProjectDetector.isMavenProject(project)) {
            return getAbsolutePath(project, MAVEN_REPORT_PATH);
        } else if (ProjectDetector.isGradleProject(project)) {
            return getAbsolutePath(project, GRADLE_REPORT_PATH);
        }

        throw new ExecutionException("Only Maven and Gradle projects are supported");
    }

    private String getAbsolutePath(Project project, String pathInProject) {
        return project.getBaseDir().getPath() + fileFinder.getFileSeparator() + pathInProject;
    }

    String getPitMainClass() {
        return PIT_MAIN_CLASS;
    }

    String getPitJarPath() {
        String fileSeparator = fileFinder.getFileSeparator();
        return fileFinder.getPluginsPath() + fileSeparator + PLUGIN_NAME + fileSeparator + LIB_DIR + fileSeparator + PIT_JAR;
    }

    String getPitCommandLineJarPath() {
        String fileSeparator = fileFinder.getFileSeparator();
        return fileFinder.getPluginsPath() + fileSeparator + PLUGIN_NAME + fileSeparator + LIB_DIR + fileSeparator + PIT_COMMAND_LINE_JAR;
    }

    String getTargetClasses() {
        return targetClasses;
    }

    String getTargetTests() {
        return testClassQualifiedName;
    }

}
