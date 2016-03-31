package org.zalando.zester.configuration;

import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.JavaCommandLineState;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.configurations.ParametersList;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.util.JavaParametersUtil;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.zalando.zester.file.FileFinder;
import org.zalando.zester.file.FileProvider;

public class ZesterCommandLineState extends JavaCommandLineState {


    private final String testClassQualifiedName;
    private final String targetClasses;
    private ConsoleView consoleView;

    protected ZesterCommandLineState(@NotNull ExecutionEnvironment environment,
                                     String testClassQualifiedName,
                                     String targetClasses) {
        super(environment);
        this.testClassQualifiedName = testClassQualifiedName;
        this.targetClasses = targetClasses;
    }

    @Override
    protected JavaParameters createJavaParameters() throws ExecutionException {
        JavaParameters javaParameters = new JavaParameters();
        Project project = getEnvironment().getProject();

        PitJavaParameters pitJavaParameters = createPitJavaParameters();

        javaParameters.setMainClass(pitJavaParameters.getPitMainClass());
        javaParameters.getClassPath().add(pitJavaParameters.getPitJarPath());
        javaParameters.getClassPath().add(pitJavaParameters.getPitCommandLineJarPath());

        ParametersList programParametersList = javaParameters.getProgramParametersList();

        programParametersList.add("--reportDir");
        programParametersList.add(pitJavaParameters.getReportDirPath());

        programParametersList.add("--sourceDirs");
        programParametersList.add(pitJavaParameters.getSourceDirPath());

        programParametersList.add("--targetClasses");
        programParametersList.add(pitJavaParameters.getTargetClasses());

        programParametersList.add("--targetTests");
        programParametersList.add(pitJavaParameters.getTargetTests());

        JavaParametersUtil.configureProject(project, javaParameters, JavaParameters.JDK_AND_CLASSES_AND_TESTS, null);

        return javaParameters;
    }

    private PitJavaParameters createPitJavaParameters() throws ExecutionException {
        Project project = getEnvironment().getProject();

        return new PitJavaParameters(
                project,
                testClassQualifiedName,
                targetClasses,
                new FileFinder(new FileProvider()));
    }

    @NotNull
    @Override
    protected OSProcessHandler startProcess() throws ExecutionException {
        OSProcessHandler handler = super.startProcess();
        String reportDirPath = createPitJavaParameters().getReportDirPath();
        handler.addProcessListener(new ZesterProcessListener(
                consoleView,
                reportDirPath,
                new FileFinder(new FileProvider()),
                targetClasses));
        return handler;
    }

    @NotNull
    @Override
    public ExecutionResult execute(@NotNull Executor executor, @NotNull ProgramRunner runner) throws ExecutionException {
        final ConsoleView console = createConsole(executor);
        if (console != null) {
            consoleView = console;
        }

        final ProcessHandler processHandler = startProcess();
        if (console != null) {
            console.attachToProcess(processHandler);
        }
        return new DefaultExecutionResult(console, processHandler, createActions(console, processHandler, executor));
    }
}
