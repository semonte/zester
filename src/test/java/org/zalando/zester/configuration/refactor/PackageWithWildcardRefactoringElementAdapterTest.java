package org.zalando.zester.configuration.refactor;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPackage;
import org.junit.Before;
import org.junit.Test;
import org.zalando.zester.configuration.ZesterRunConfiguration;

import java.util.EnumSet;
import java.util.Set;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class PackageWithWildcardRefactoringElementAdapterTest {


    private ZesterRunConfiguration zesterRunConfiguration;

    @Before
    public void before() {
        zesterRunConfiguration = mock(ZesterRunConfiguration.class);
    }

    @Test
    public void thatTargetTestClassIsRenamedWhenPackageIsRenamed() {
        PsiPackage newPackage = mock(PsiPackage.class);
        when(newPackage.getQualifiedName()).thenReturn("org.zalando.zester");

        PackageWithWildcardRefactoringElementAdapter adapter =
                new PackageWithWildcardRefactoringElementAdapter(zesterRunConfiguration, EnumSet.of(UpdateField.TARGET_TEST_CLASS));

        adapter.elementRenamedOrMoved(newPackage);

        verify(zesterRunConfiguration).setTargetTestClassQualifiedName("org.zalando.zester.*");
    }

    @Test
    public void thatTargetClassIsNotUpdatedWhenItIsNotIncludedInUpdateFields() {
        PsiPackage newPackage = mock(PsiPackage.class);
        when(newPackage.getQualifiedName()).thenReturn("org.zalando.zester");

        PackageWithWildcardRefactoringElementAdapter adapter =
                new PackageWithWildcardRefactoringElementAdapter(zesterRunConfiguration, EnumSet.of(UpdateField.TARGET_TEST_CLASS));

        adapter.elementRenamedOrMoved(newPackage);

        verify(zesterRunConfiguration, times(0)).setTargetClasses(anyString());
    }

    @Test
    public void thatTargetClassIsRenamedWhenPackageIsRenamed() {
        PsiPackage newPackage = mock(PsiPackage.class);
        when(newPackage.getQualifiedName()).thenReturn("org.zalando.zester");

        PackageWithWildcardRefactoringElementAdapter adapter =
                new PackageWithWildcardRefactoringElementAdapter(zesterRunConfiguration, EnumSet.of(UpdateField.TARGET_CLASS));

        adapter.elementRenamedOrMoved(newPackage);

        verify(zesterRunConfiguration).setTargetClasses("org.zalando.zester.*");
    }

    @Test
    public void thatTargetTestClassIsNotUpdatedWhenItIsNotIncludedInUpdateFields() {
        PsiPackage newPackage = mock(PsiPackage.class);
        when(newPackage.getQualifiedName()).thenReturn("org.zalando.zester");

        PackageWithWildcardRefactoringElementAdapter adapter =
                new PackageWithWildcardRefactoringElementAdapter(zesterRunConfiguration, EnumSet.of(UpdateField.TARGET_CLASS));

        adapter.elementRenamedOrMoved(newPackage);

        verify(zesterRunConfiguration, times(0)).setTargetTestClassQualifiedName(anyString());
    }

    @Test
    public void thatUndoUpdatesTargetTestClass() {
        PackageWithWildcardRefactoringElementAdapter adapter =
                new PackageWithWildcardRefactoringElementAdapter(zesterRunConfiguration, EnumSet.of(UpdateField.TARGET_TEST_CLASS));

        adapter.undoElementMovedOrRenamed(mock(PsiElement.class), "org.zalando.zester");

        verify(zesterRunConfiguration).setTargetTestClassQualifiedName("org.zalando.zester.*");
    }

    @Test
    public void thatUndoDoesNotUpdateTargetClassWhenItIsNotIncludedInUpdateFields() {
        PackageWithWildcardRefactoringElementAdapter adapter =
                new PackageWithWildcardRefactoringElementAdapter(zesterRunConfiguration, EnumSet.of(UpdateField.TARGET_TEST_CLASS));

        adapter.undoElementMovedOrRenamed(mock(PsiElement.class), "org.zalando.zester");

        verify(zesterRunConfiguration, times(0)).setTargetClasses(anyString());
    }

    @Test
    public void thatUndoUpdatesTargetClass() {
        PackageWithWildcardRefactoringElementAdapter adapter =
                new PackageWithWildcardRefactoringElementAdapter(zesterRunConfiguration, EnumSet.of(UpdateField.TARGET_CLASS));

        adapter.undoElementMovedOrRenamed(mock(PsiElement.class), "org.zalando.zester");

        verify(zesterRunConfiguration).setTargetClasses("org.zalando.zester.*");
    }

    @Test
    public void thatUndoDoesNotUpdateTargetTestClassWhenItIsNotIncludedInUpdateFields() {
        PackageWithWildcardRefactoringElementAdapter adapter =
                new PackageWithWildcardRefactoringElementAdapter(zesterRunConfiguration, EnumSet.of(UpdateField.TARGET_CLASS));

        adapter.undoElementMovedOrRenamed(mock(PsiElement.class), "org.zalando.zester");

        verify(zesterRunConfiguration, times(0)).setTargetTestClassQualifiedName(anyString());
    }

}
