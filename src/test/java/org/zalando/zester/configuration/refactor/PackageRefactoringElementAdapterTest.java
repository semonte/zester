package org.zalando.zester.configuration.refactor;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPackage;
import org.junit.Before;
import org.junit.Test;
import org.zalando.zester.configuration.ZesterRunConfiguration;

import java.util.EnumSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class PackageRefactoringElementAdapterTest {

    private static final String TARGET_TEST_CLASS_NAME = "ClassTest";
    private static final String TARGET_CLASS_NAME = "Class";

    private ZesterRunConfiguration zesterRunConfiguration;

    @Before
    public void before() {
        zesterRunConfiguration = mock(ZesterRunConfiguration.class);
    }

    @Test
    public void thatTargetTestClassIsUpdatedWhenPackageIsRenamed() {
        PsiPackage newPackage = mock(PsiPackage.class);
        when(newPackage.getQualifiedName()).thenReturn("org.zalando.zester");

        Set<UpdateField> fields = EnumSet.of(UpdateField.TARGET_TEST_CLASS);

        PackageRefactoringElementAdapter adapter = new PackageRefactoringElementAdapter(zesterRunConfiguration,
                fields, TARGET_TEST_CLASS_NAME, TARGET_CLASS_NAME);

        adapter.elementRenamedOrMoved(newPackage);

        verify(zesterRunConfiguration).setTargetTestClassQualifiedName("org.zalando.zester.ClassTest");
    }

    @Test
    public void thatTargetClassIsNotUpdatedWhenItIsNotIncludedInUpdateFields() {
        PsiPackage newPackage = mock(PsiPackage.class);
        when(newPackage.getQualifiedName()).thenReturn("org.zalando.zester");

        Set<UpdateField> fields = EnumSet.of(UpdateField.TARGET_TEST_CLASS);

        PackageRefactoringElementAdapter adapter = new PackageRefactoringElementAdapter(zesterRunConfiguration,
                fields, TARGET_TEST_CLASS_NAME, TARGET_CLASS_NAME);

        adapter.elementRenamedOrMoved(newPackage);

        verify(zesterRunConfiguration, times(0)).setTargetClasses(anyString());
    }

    @Test
    public void thatTargetClassIsUpdatedWhenPackageIsRenamed() {
        PsiPackage newPackage = mock(PsiPackage.class);
        when(newPackage.getQualifiedName()).thenReturn("org.zalando.zester");

        Set<UpdateField> fields = EnumSet.of(UpdateField.TARGET_CLASS);

        PackageRefactoringElementAdapter adapter = new PackageRefactoringElementAdapter(zesterRunConfiguration,
                fields, TARGET_TEST_CLASS_NAME, TARGET_CLASS_NAME);

        adapter.elementRenamedOrMoved(newPackage);

        verify(zesterRunConfiguration).setTargetClasses("org.zalando.zester.Class");
    }

    @Test
    public void thatTargetTestClassIsNotUpdatedWhenItIsNotIncludedInUpdateFields() {
        PsiPackage newPackage = mock(PsiPackage.class);
        when(newPackage.getQualifiedName()).thenReturn("org.zalando.zester");

        Set<UpdateField> fields = EnumSet.of(UpdateField.TARGET_CLASS);

        PackageRefactoringElementAdapter adapter = new PackageRefactoringElementAdapter(zesterRunConfiguration,
                fields, TARGET_TEST_CLASS_NAME, TARGET_CLASS_NAME);

        adapter.elementRenamedOrMoved(newPackage);

        verify(zesterRunConfiguration, times(0)).setTargetTestClassQualifiedName(anyString());
    }

    @Test
    public void thatUndoUpdatesTargetTestClass() {
        PackageRefactoringElementAdapter adapter = new PackageRefactoringElementAdapter(zesterRunConfiguration,
                EnumSet.of(UpdateField.TARGET_TEST_CLASS), TARGET_TEST_CLASS_NAME, TARGET_CLASS_NAME);

        adapter.undoElementMovedOrRenamed(mock(PsiElement.class), "org.zalando.zester");

        verify(zesterRunConfiguration).setTargetTestClassQualifiedName("org.zalando.zester.ClassTest");
    }

    @Test
    public void thatUndoDoesNotUpdateTargetClassWhenItIsNotIncludedInUpdateFields() {
        PackageRefactoringElementAdapter adapter = new PackageRefactoringElementAdapter(zesterRunConfiguration,
                EnumSet.of(UpdateField.TARGET_TEST_CLASS), TARGET_TEST_CLASS_NAME, TARGET_CLASS_NAME);

        adapter.undoElementMovedOrRenamed(mock(PsiElement.class), "org.zalando.zester");

        verify(zesterRunConfiguration, times(0)).setTargetClasses(anyString());
    }

    @Test
    public void thatUndoUpdatesTargetClass() {
        PackageRefactoringElementAdapter adapter = new PackageRefactoringElementAdapter(zesterRunConfiguration,
                EnumSet.of(UpdateField.TARGET_CLASS), TARGET_TEST_CLASS_NAME, TARGET_CLASS_NAME);

        adapter.undoElementMovedOrRenamed(mock(PsiElement.class), "org.zalando.zester");

        verify(zesterRunConfiguration).setTargetClasses("org.zalando.zester.Class");
    }

    @Test
    public void thatUndoDoesNotUpdateTargetTestClassWhenItIsNotIncludedInUpdateFields() {
        PackageRefactoringElementAdapter adapter = new PackageRefactoringElementAdapter(zesterRunConfiguration,
                EnumSet.of(UpdateField.TARGET_CLASS), TARGET_TEST_CLASS_NAME, TARGET_CLASS_NAME);

        adapter.undoElementMovedOrRenamed(mock(PsiElement.class), "org.zalando.zester");

        verify(zesterRunConfiguration, times(0)).setTargetTestClassQualifiedName(anyString());
    }
}
