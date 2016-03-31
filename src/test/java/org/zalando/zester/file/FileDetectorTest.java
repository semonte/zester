package org.zalando.zester.file;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileDetectorTest {

    @Test
    public void thatFileWithJUnitAnnotationIsDetectedAsTestFile() {
        PsiClass jUnitClass = getClassWithMethodAnnotation("org.junit.Test");
        assertTrue(FileDetector.isTestClass(jUnitClass));
    }

    @Test
    public void thatFileWithTestNGAnnotationIsDetectedAsTestFile() {
        PsiClass testNGClass = getClassWithMethodAnnotation("org.testng.annotations.Test");
        assertTrue(FileDetector.isTestClass(testNGClass));
    }

    @Test
    public void thatFileWithNoTestAnnotationIsNotDetectedAsTestFile() {
        PsiClass aClass = getClassWithMethodAnnotation("some.annotation.Class");
        assertFalse(FileDetector.isTestClass(aClass));
    }

    private PsiClass getClassWithMethodAnnotation(String qualifiedName) {
        PsiClass psiClass = mock(PsiClass.class);
        PsiMethod method = mock(PsiMethod.class);
        PsiModifierList modifierList = mock(PsiModifierList.class);
        PsiAnnotation methodAnnotation = mock(PsiAnnotation.class);

        when(psiClass.getMethods()).thenReturn(new PsiMethod[]{(method)});
        when(method.getModifierList()).thenReturn(modifierList);
        when(modifierList.getAnnotations()).thenReturn(new PsiAnnotation[]{methodAnnotation});
        when(methodAnnotation.getQualifiedName()).thenReturn(qualifiedName);

        return psiClass;
    }
}
