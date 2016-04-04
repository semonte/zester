package org.zalando.zester.configuration.refactor;

import com.intellij.execution.JavaExecutionUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.listeners.RefactoringElementAdapter;
import org.jetbrains.annotations.NotNull;
import org.zalando.zester.configuration.ZesterRunConfiguration;

public class TargetClassRefactoringElementAdapter extends RefactoringElementAdapter {

    private final ZesterRunConfiguration zesterRunConfiguration;

    public TargetClassRefactoringElementAdapter(ZesterRunConfiguration zesterRunConfiguration) {
        this.zesterRunConfiguration = zesterRunConfiguration;
    }

    @Override
    protected void elementRenamedOrMoved(@NotNull PsiElement newElement) {
        zesterRunConfiguration.setTargetClasses(JavaExecutionUtil.getRuntimeQualifiedName((PsiClass) newElement));
    }

    @Override
    public void undoElementMovedOrRenamed(@NotNull PsiElement newElement, @NotNull String oldQualifiedName) {
        zesterRunConfiguration.setTargetClasses(oldQualifiedName);
    }
}
