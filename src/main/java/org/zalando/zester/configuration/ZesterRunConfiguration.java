package org.zalando.zester.configuration;

import com.intellij.execution.ExecutionBundle;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.JavaExecutionUtil;
import com.intellij.execution.JavaRunConfigurationExtensionManager;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.JavaRunConfigurationModule;
import com.intellij.execution.configurations.ModuleBasedConfiguration;
import com.intellij.execution.configurations.RefactoringListenerProvider;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.options.SettingsEditorGroup;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizer;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPackage;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zalando.zester.configuration.refactor.PackageRefactoringElementAdapter;
import org.zalando.zester.configuration.refactor.PackageWithWildcardRefactoringElementAdapter;
import org.zalando.zester.configuration.refactor.TargetClassRefactoringElementAdapter;
import org.zalando.zester.configuration.refactor.TargetTestClassRefactoringElementAdapter;
import org.zalando.zester.configuration.refactor.UpdateField;
import org.zalando.zester.settings.ZesterSettingsEditor;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ZesterRunConfiguration extends ModuleBasedConfiguration<JavaRunConfigurationModule> implements RunConfiguration, RefactoringListenerProvider {

    private static final String TEST_CLASS_PATH = "TEST_CLASS_PATH";
    private static final String TARGET_CLASS_PATH = "TARGET_CLASS_PATH";

    private String targetTestClassQualifiedName;
    private String targetClasses;

    public ZesterRunConfiguration(String name,
                                  @NotNull JavaRunConfigurationModule configurationModule,
                                  @NotNull ConfigurationFactory factory) {
        super(name, configurationModule, factory);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        SettingsEditorGroup<ZesterRunConfiguration> group = new SettingsEditorGroup<>();
        group.addEditor(ExecutionBundle.message("run.configuration.configuration.tab.title"),
                new ZesterSettingsEditor(getProject()));
        JavaRunConfigurationExtensionManager.getInstance().appendEditors(this, group);
        return group;
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor,
                                    @NotNull ExecutionEnvironment environment) throws ExecutionException {
        return new ZesterCommandLineState(environment, getTargetTestClassQualifiedName(), targetClasses);
    }

    public void setTargetTestClassQualifiedName(String targetTestClassQualifiedName) {
        this.targetTestClassQualifiedName = targetTestClassQualifiedName;
    }

    public String getTargetTestClassQualifiedName() {
        return targetTestClassQualifiedName;
    }

    public void setTargetClasses(String targetClasses) {
        this.targetClasses = targetClasses;
    }

    public String getTargetClasses() {
        return targetClasses;
    }


    @Override
    public void writeExternal(final Element element) throws WriteExternalException {
        JDOMExternalizer.write(element, TEST_CLASS_PATH, targetTestClassQualifiedName);
        JDOMExternalizer.write(element, TARGET_CLASS_PATH, targetClasses);
    }

    @Override
    public void readExternal(final Element element) throws InvalidDataException {
        targetTestClassQualifiedName = JDOMExternalizer.readString(element, TEST_CLASS_PATH);
        targetClasses = JDOMExternalizer.readString(element, TARGET_CLASS_PATH);
    }

    @Override
    public Collection<Module> getValidModules() {
        return Arrays.asList(ModuleManager.getInstance(getProject()).getModules());
    }

    @Override
    public String suggestedName() {
        if (targetTestClassQualifiedName.endsWith(".*")) {
            return targetTestClassQualifiedName;
        } else {
            return targetTestClassQualifiedName.substring(targetTestClassQualifiedName.lastIndexOf(".") + 1);
        }
    }

    @Override
    public RefactoringElementListener getRefactoringElementListener(PsiElement element) {
        if (element instanceof PsiClass) {
            if (StringUtil.equals(JavaExecutionUtil.getRuntimeQualifiedName((PsiClass) element), targetTestClassQualifiedName)) {
                return new TargetTestClassRefactoringElementAdapter(this);
            } else if (StringUtil.equals(JavaExecutionUtil.getRuntimeQualifiedName((PsiClass) element), targetClasses)) {
                return new TargetClassRefactoringElementAdapter(this);
            }
        } else if (element instanceof PsiPackage) {
            PsiPackage psiPackage = (PsiPackage) element;
            String packageWithWildcard = psiPackage.getQualifiedName() + ".*";

            boolean targetTestClassPackageWithWildcardMatches = StringUtil.equals(packageWithWildcard, targetTestClassQualifiedName);
            boolean targetClassPackageWithWildcardMatches = StringUtil.equals(packageWithWildcard, targetClasses);

            if (targetTestClassPackageWithWildcardMatches || targetClassPackageWithWildcardMatches) {
                Set<UpdateField> fields = new HashSet<>();
                if (targetTestClassPackageWithWildcardMatches) {
                    fields.add(UpdateField.TARGET_TEST_CLASS);
                }
                if (targetClassPackageWithWildcardMatches) {
                    fields.add(UpdateField.TARGET_CLASS);
                }

                return new PackageWithWildcardRefactoringElementAdapter(this, fields);

            } else {
                String targetTestClassPackage = targetTestClassQualifiedName.substring(0, targetTestClassQualifiedName.lastIndexOf("."));
                String targetTestClassName = targetTestClassQualifiedName.substring(targetTestClassQualifiedName.lastIndexOf(".") + 1);

                String targetClassPackage = targetClasses.substring(0, targetClasses.lastIndexOf("."));
                String targetClassClassName = targetClasses.substring(targetClasses.lastIndexOf(".") + 1);

                boolean targetTestClassPackageMatches = StringUtil.equals(psiPackage.getQualifiedName(), targetTestClassPackage);
                boolean targetClassPackageMatches = StringUtil.equals(psiPackage.getQualifiedName(), targetClassPackage);

                if (targetTestClassPackageMatches || targetClassPackageMatches) {
                    Set<UpdateField> fields = new HashSet<>();
                    if (targetTestClassPackageMatches) {
                        fields.add(UpdateField.TARGET_TEST_CLASS);
                    }
                    if (targetClassPackageMatches) {
                        fields.add(UpdateField.TARGET_CLASS);
                    }

                    return new PackageRefactoringElementAdapter(this, fields, targetTestClassName, targetClassClassName);
                }
            }
        }
        return null;
    }
}
