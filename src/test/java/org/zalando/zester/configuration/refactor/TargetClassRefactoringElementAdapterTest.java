package org.zalando.zester.configuration.refactor;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import org.junit.Before;
import org.junit.Test;
import org.zalando.zester.configuration.ZesterRunConfiguration;

import static org.mockito.Mockito.*;

public class TargetClassRefactoringElementAdapterTest {


    private ZesterRunConfiguration zesterRunConfiguration;

    @Before
    public void before() {
        zesterRunConfiguration = mock(ZesterRunConfiguration.class);
    }

    @Test
    public void thatTargetClassIsUpdatedWhenClassIsRenamed() {
        PsiClass newClass = mock(PsiClass.class);
        when(newClass.getQualifiedName()).thenReturn("org.zalando.zester.NewClass");

        TargetClassRefactoringElementAdapter adapter = new TargetClassRefactoringElementAdapter(zesterRunConfiguration);

        adapter.elementRenamedOrMoved(newClass);

        verify(zesterRunConfiguration).setTargetClasses("org.zalando.zester.NewClass");
    }

    @Test
    public void thatTargetTestClassIsNotUpdated() {
        PsiClass newClass = mock(PsiClass.class);
        when(newClass.getQualifiedName()).thenReturn("org.zalando.zester.NewClass");

        TargetClassRefactoringElementAdapter adapter = new TargetClassRefactoringElementAdapter(zesterRunConfiguration);

        adapter.elementRenamedOrMoved(newClass);

        verify(zesterRunConfiguration, times(0)).setTargetTestClassQualifiedName(anyString());
    }

    @Test
    public void thatUndoResetsTargetClass() {
        TargetClassRefactoringElementAdapter adapter = new TargetClassRefactoringElementAdapter(zesterRunConfiguration);

        adapter.undoElementMovedOrRenamed(mock(PsiElement.class), "org.zalando.zester.OldClass");

        verify(zesterRunConfiguration).setTargetClasses("org.zalando.zester.OldClass");
    }

    @Test
    public void thatUndoDoesNotResetTargetTestClass() {
        TargetClassRefactoringElementAdapter adapter = new TargetClassRefactoringElementAdapter(zesterRunConfiguration);

        adapter.undoElementMovedOrRenamed(mock(PsiElement.class), "org.zalando.zester.OldClass");

        verify(zesterRunConfiguration, times(0)).setTargetTestClassQualifiedName(anyString());
    }

}
