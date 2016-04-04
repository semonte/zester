package org.zalando.zester.configuration.refactor;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPackage;
import com.intellij.refactoring.listeners.RefactoringElementAdapter;
import org.jetbrains.annotations.NotNull;
import org.zalando.zester.configuration.ZesterRunConfiguration;

import java.util.Set;

public class PackageRefactoringElementAdapter extends RefactoringElementAdapter {

    private final ZesterRunConfiguration zesterRunConfiguration;
    private final Set<UpdateField> fields;
    private final String targetTestClassName;
    private final String targetClassClassName;

    public PackageRefactoringElementAdapter(ZesterRunConfiguration zesterRunConfiguration,
                                            Set<UpdateField> fields,
                                            String targetTestClassName,
                                            String targetClassClassName) {
        this.zesterRunConfiguration = zesterRunConfiguration;
        this.fields = fields;
        this.targetTestClassName = targetTestClassName;
        this.targetClassClassName = targetClassClassName;
    }

    @Override
    protected void elementRenamedOrMoved(@NotNull PsiElement newElement) {
        if (fields.contains(UpdateField.TARGET_TEST_CLASS)) {
            zesterRunConfiguration.setTargetTestClassQualifiedName(((PsiPackage) newElement).getQualifiedName() + "." + targetTestClassName);
        }
        if (fields.contains(UpdateField.TARGET_CLASS)) {
            zesterRunConfiguration.setTargetClasses(((PsiPackage) newElement).getQualifiedName() + "." + targetClassClassName);
        }
    }

    @Override
    public void undoElementMovedOrRenamed(@NotNull PsiElement newElement, @NotNull String oldQualifiedName) {
        if (fields.contains(UpdateField.TARGET_TEST_CLASS)) {
            zesterRunConfiguration.setTargetTestClassQualifiedName(oldQualifiedName + "." + targetTestClassName);
        }
        if (fields.contains(UpdateField.TARGET_CLASS)) {
            zesterRunConfiguration.setTargetClasses(oldQualifiedName + "." + targetClassClassName);
        }
    }
}
