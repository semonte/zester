package org.zalando.zester.file;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifierList;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileDetector {

    private static final String JUNIT_TEST_QUALIFIED_NAME = "org.junit.Test";
    private static final String TESTNG_TEST_QUALIFIED_NAME = "org.testng.annotations.Test";

    private static final Set<String> TEST_ANNOTATIONS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(JUNIT_TEST_QUALIFIED_NAME, TESTNG_TEST_QUALIFIED_NAME)));

    public static boolean isTestClass(PsiClass psiClass) {
        return getMethods(psiClass).stream()
                .map(PsiMethod::getModifierList)
                .map(PsiModifierList::getAnnotations)
                .flatMap(annotations -> Arrays.asList(annotations).stream())
                .map(PsiAnnotation::getQualifiedName)
                .anyMatch(TEST_ANNOTATIONS::contains);
    }

    private static List<PsiMethod> getMethods(PsiClass psiClass) {
        return Arrays.asList(psiClass.getMethods());
    }

}
