package org.zalando.zester.project;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public class ProjectDetector {


    public static boolean isMavenProject(Project project) {
        return findFileInBaseDir(project, "pom.xml") != null;
    }

    public static boolean isGradleProject(Project project) {
        return findFileInBaseDir(project, "build.gradle") != null;
    }

    private static VirtualFile findFileInBaseDir(Project project, String name) {
        return project.getBaseDir().findChild(name);
    }

}
