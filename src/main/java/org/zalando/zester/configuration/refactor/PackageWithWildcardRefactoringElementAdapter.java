package org.zalando.zester.configuration.refactor;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPackage;
import com.intellij.refactoring.listeners.RefactoringElementAdapter;
import org.jetbrains.annotations.NotNull;
import org.zalando.zester.configuration.ZesterRunConfiguration;

import java.util.Set;

public class PackageWithWildcardRefactoringElementAdapter extends RefactoringElementAdapter {

    private final ZesterRunConfiguration zesterRunConfiguration;
    private final Set<UpdateField> fields;

    public PackageWithWildcardRefactoringElementAdapter(ZesterRunConfiguration zesterRunConfiguration,
                                                        Set<UpdateField> fields) {
        this.zesterRunConfiguration = zesterRunConfiguration;
        this.fields = fields;
    }

    @Override
    protected void elementRenamedOrMoved(@NotNull PsiElement newElement) {
        if (fields.contains(UpdateField.TARGET_TEST_CLASS)) {
            zesterRunConfiguration.setTargetTestClassQualifiedName(((PsiPackage) newElement).getQualifiedName() + ".*");
        }

        if (fields.contains(UpdateField.TARGET_CLASS)) {
            zesterRunConfiguration.setTargetClasses(((PsiPackage) newElement).getQualifiedName() + ".*");
        }
    }

    @Override
    public void undoElementMovedOrRenamed(@NotNull PsiElement newElement, @NotNull String oldQualifiedName) {
        if (fields.contains(UpdateField.TARGET_TEST_CLASS)) {
            zesterRunConfiguration.setTargetTestClassQualifiedName(oldQualifiedName + ".*");
        }

        if (fields.contains(UpdateField.TARGET_CLASS)) {
            zesterRunConfiguration.setTargetClasses(oldQualifiedName + ".*");
        }
    }
}
