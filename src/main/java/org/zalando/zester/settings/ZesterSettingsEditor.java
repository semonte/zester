package org.zalando.zester.settings;

import com.intellij.ide.util.TreeClassChooser;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.RawCommandLineEditor;
import org.jetbrains.annotations.NotNull;
import org.zalando.zester.configuration.ZesterRunConfiguration;

import javax.swing.*;

public class ZesterSettingsEditor extends SettingsEditor<ZesterRunConfiguration> {

    private TextFieldWithBrowseButton targetTestClassQualifiedName;
    private TextFieldWithBrowseButton targetClasses;
    private JPanel mainPanel;
    private RawCommandLineEditor vmOptions;

    public ZesterSettingsEditor(Project project) {
        setupTestClassBrowser(project);
        setupTargetClassesBrowser(project);
        vmOptions.setDialogCaption("VM Options");
    }

    @Override
    protected void resetEditorFrom(ZesterRunConfiguration zesterRunConfiguration) {
        targetTestClassQualifiedName.setText(zesterRunConfiguration.getTargetTestClassQualifiedName());
        targetClasses.setText(zesterRunConfiguration.getTargetClasses());
        vmOptions.setText(zesterRunConfiguration.getVmOptions());
    }

    @Override
    protected void applyEditorTo(ZesterRunConfiguration zesterRunConfiguration) throws ConfigurationException {
        zesterRunConfiguration.setTargetTestClassQualifiedName(getTargetTestClassQualifiedNameAsText());
        zesterRunConfiguration.setTargetClasses(getTargetClassesAsText());
        zesterRunConfiguration.setVmOptions(getVmOptionsAsText());
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return mainPanel;
    }

    private String getTargetTestClassQualifiedNameAsText() {
        return targetTestClassQualifiedName.getText();
    }

    private String getTargetClassesAsText() {
        return targetClasses.getText();
    }

    private String getVmOptionsAsText() {
        return vmOptions.getText();
    }

    private void setupTestClassBrowser(Project project) {
        targetTestClassQualifiedName.addActionListener(listener -> {
            PsiClass selected = createTestClassChooser(project, targetTestClassQualifiedName.getText());
            if (selected != null) {
                targetTestClassQualifiedName.setText(selected.getQualifiedName());
            }
        });
    }

    private void setupTargetClassesBrowser(Project project) {
        targetClasses.addActionListener(listener -> {
            PsiClass selected = createTargetClassChooser(project, targetClasses.getText());
            if (selected != null) {
                targetClasses.setText(selected.getQualifiedName());
            }
        });
    }

    private static PsiClass createTestClassChooser(Project project, String initialClassName) {
        TreeClassChooserFactory chooserFactory = TreeClassChooserFactory.getInstance(project);
        GlobalSearchScope searchScope = GlobalSearchScope.projectScope(project);
        PsiClass aClass = initialClassName != null
                ? JavaPsiFacade.getInstance(project).findClass(initialClassName, searchScope)
                : null;
        TreeClassChooser chooser =
                chooserFactory.createNoInnerClassesScopeChooser(
                        "Select Test Class", searchScope, new TestClassFilter(), aClass);
        chooser.showDialog();
        return chooser.getSelected();
    }

    private static PsiClass createTargetClassChooser(Project project, String initialClassName) {
        TreeClassChooserFactory chooserFactory = TreeClassChooserFactory.getInstance(project);
        GlobalSearchScope searchScope = GlobalSearchScope.projectScope(project);
        PsiClass aClass = initialClassName != null
                ? JavaPsiFacade.getInstance(project).findClass(initialClassName, searchScope)
                : null;
        TreeClassChooser chooser =
                chooserFactory.createNoInnerClassesScopeChooser(
                        "Select Target Class", searchScope, new SourceClassFilter(), aClass);
        chooser.showDialog();
        return chooser.getSelected();
    }

}
