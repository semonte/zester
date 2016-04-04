package org.zalando.zester.configuration;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.JavaRunConfigurationModule;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiPackage;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import org.junit.Before;
import org.junit.Test;
import org.zalando.zester.configuration.refactor.PackageRefactoringElementAdapter;
import org.zalando.zester.configuration.refactor.PackageWithWildcardRefactoringElementAdapter;
import org.zalando.zester.configuration.refactor.TargetClassRefactoringElementAdapter;
import org.zalando.zester.configuration.refactor.TargetTestClassRefactoringElementAdapter;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ZesterRunConfigurationTest {

    private ZesterRunConfiguration zesterRunConfiguration;

    @Before
    public void before() {
        JavaRunConfigurationModule mock = mock(JavaRunConfigurationModule.class);
        when(mock.getProject()).thenReturn(mock(Project.class));

        zesterRunConfiguration = new ZesterRunConfiguration("", mock, mock(ConfigurationFactory.class));
    }

    @Test
    public void thatRenamingTargetTestClassReturnsCorrectElementAdapter() {
        PsiClass toBeRenamedClass = mock(PsiClass.class);
        when(toBeRenamedClass.getQualifiedName()).thenReturn("org.zalando.zester.ClassTest");
        zesterRunConfiguration.setTargetTestClassQualifiedName("org.zalando.zester.ClassTest");

        RefactoringElementListener listener = zesterRunConfiguration.getRefactoringElementListener(toBeRenamedClass);

        assertTrue(listener instanceof TargetTestClassRefactoringElementAdapter);
    }

    @Test
    public void thatRenamingTargetClassReturnsCorrectElementAdapter() {
        PsiClass toBeRenamedClass = mock(PsiClass.class);
        when(toBeRenamedClass.getQualifiedName()).thenReturn("org.zalando.zester.Class");
        zesterRunConfiguration.setTargetClasses("org.zalando.zester.Class");

        RefactoringElementListener listener = zesterRunConfiguration.getRefactoringElementListener(toBeRenamedClass);

        assertTrue(listener instanceof TargetClassRefactoringElementAdapter);
    }

    @Test
    public void thatRenamingPackageReturnsCorrectElementAdapterWhenTargetClassWithWildcardIsMatched() {
        PsiPackage toBeRenamedPackage = mock(PsiPackage.class);
        when(toBeRenamedPackage.getQualifiedName()).thenReturn("org.zalando.zester");
        zesterRunConfiguration.setTargetClasses("org.zalando.zester.*");

        RefactoringElementListener listener = zesterRunConfiguration.getRefactoringElementListener(toBeRenamedPackage);

        assertTrue(listener instanceof PackageWithWildcardRefactoringElementAdapter);
    }

    @Test
    public void thatRenamingPackageReturnsCorrectElementAdapterWhenTargetTestClassWithWildcardIsMatched() {
        PsiPackage toBeRenamedPackage = mock(PsiPackage.class);
        when(toBeRenamedPackage.getQualifiedName()).thenReturn("org.zalando.zester");
        zesterRunConfiguration.setTargetTestClassQualifiedName("org.zalando.zester.*");

        RefactoringElementListener listener = zesterRunConfiguration.getRefactoringElementListener(toBeRenamedPackage);

        assertTrue(listener instanceof PackageWithWildcardRefactoringElementAdapter);
    }

    @Test
    public void thatRenamingPackageReturnsCorrectElementAdapterWhenTargetTestClassAndTargetClassWithWildcardsAreMatched() {
        PsiPackage toBeRenamedPackage = mock(PsiPackage.class);
        when(toBeRenamedPackage.getQualifiedName()).thenReturn("org.zalando.zester");
        zesterRunConfiguration.setTargetClasses("org.zalando.zester.*");
        zesterRunConfiguration.setTargetTestClassQualifiedName("org.zalando.zester.*");

        RefactoringElementListener listener = zesterRunConfiguration.getRefactoringElementListener(toBeRenamedPackage);

        assertTrue(listener instanceof PackageWithWildcardRefactoringElementAdapter);
    }

    @Test
    public void thatRenamingPackageReturnsCorrectElementAdapterWhenTargetClassIsMatched() {
        PsiPackage toBeRenamedPackage = mock(PsiPackage.class);
        when(toBeRenamedPackage.getQualifiedName()).thenReturn("org.zalando.zester");
        zesterRunConfiguration.setTargetTestClassQualifiedName("org.zalando.zester.app.AnotherClass");
        zesterRunConfiguration.setTargetClasses("org.zalando.zester.Class");

        RefactoringElementListener listener = zesterRunConfiguration.getRefactoringElementListener(toBeRenamedPackage);

        assertTrue(listener instanceof PackageRefactoringElementAdapter);
    }

    @Test
    public void thatRenamingPackageReturnsCorrectElementAdapterWhenTargetTestClassIsMatched() {
        PsiPackage toBeRenamedPackage = mock(PsiPackage.class);
        when(toBeRenamedPackage.getQualifiedName()).thenReturn("org.zalando.zester");
        zesterRunConfiguration.setTargetTestClassQualifiedName("org.zalando.zester.Class");
        zesterRunConfiguration.setTargetClasses("org.zalando.zester.app.AnotherClass");

        RefactoringElementListener listener = zesterRunConfiguration.getRefactoringElementListener(toBeRenamedPackage);

        assertTrue(listener instanceof PackageRefactoringElementAdapter);
    }

    @Test
    public void thatRenamingPackageReturnsCorrectElementAdapterWhenTargetTestClassAndTargetClassAreMatched() {
        PsiPackage toBeRenamedPackage = mock(PsiPackage.class);
        when(toBeRenamedPackage.getQualifiedName()).thenReturn("org.zalando.zester");
        zesterRunConfiguration.setTargetClasses("org.zalando.zester.Class");
        zesterRunConfiguration.setTargetTestClassQualifiedName("org.zalando.zester.AnotherClass");

        RefactoringElementListener listener = zesterRunConfiguration.getRefactoringElementListener(toBeRenamedPackage);

        assertTrue(listener instanceof PackageRefactoringElementAdapter);
    }
}
