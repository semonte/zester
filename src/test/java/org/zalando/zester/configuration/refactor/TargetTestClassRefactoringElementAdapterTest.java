package org.zalando.zester.configuration.refactor;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import org.junit.Before;
import org.junit.Test;
import org.zalando.zester.configuration.ZesterRunConfiguration;

import static org.mockito.Mockito.*;

public class TargetTestClassRefactoringElementAdapterTest {


    private ZesterRunConfiguration zesterRunConfiguration;

    @Before
    public void before() {
        zesterRunConfiguration = mock(ZesterRunConfiguration.class);
    }

    @Test
    public void thatTargetTestClassIsUpdatedWhenClassIsRenamed() {
        PsiClass newClass = mock(PsiClass.class);
        when(newClass.getQualifiedName()).thenReturn("org.zalando.zester.NewClass");

        TargetTestClassRefactoringElementAdapter adapter = new TargetTestClassRefactoringElementAdapter(zesterRunConfiguration);

        adapter.elementRenamedOrMoved(newClass);

        verify(zesterRunConfiguration).setTargetTestClassQualifiedName("org.zalando.zester.NewClass");
    }

    @Test
    public void thatTargetClassIsNotUpdated() {
        PsiClass newClass = mock(PsiClass.class);
        when(newClass.getQualifiedName()).thenReturn("org.zalando.zester.NewClass");

        TargetTestClassRefactoringElementAdapter adapter = new TargetTestClassRefactoringElementAdapter(zesterRunConfiguration);

        adapter.elementRenamedOrMoved(newClass);

        verify(zesterRunConfiguration, times(0)).setTargetClasses(anyString());
    }

    @Test
    public void thatUndoResetsTargetTestClass() {
        TargetTestClassRefactoringElementAdapter adapter = new TargetTestClassRefactoringElementAdapter(zesterRunConfiguration);

        adapter.undoElementMovedOrRenamed(mock(PsiElement.class), "org.zalando.zester.OldClass");

        verify(zesterRunConfiguration).setTargetTestClassQualifiedName("org.zalando.zester.OldClass");
    }

    @Test
    public void thatUndoDoesNotResetTargetClass() {
        TargetTestClassRefactoringElementAdapter adapter = new TargetTestClassRefactoringElementAdapter(zesterRunConfiguration);

        adapter.undoElementMovedOrRenamed(mock(PsiElement.class), "org.zalando.zester.OldClass");

        verify(zesterRunConfiguration, times(0)).setTargetClasses(anyString());
    }
}
